package org.example;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Main {


    public static void main(String[] args) {

        /*
         * Передаём в конструктор имя порта
         */
        SerialPort serialPort = new SerialPort(args[0]);
        try {
            /*
             * Открываем порт
             */
            serialPort.openPort();
            /*
             * Выставляем параметры
             */
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            /*
             * Готовим маску, на основании неё мы будем получать сообщения о событиях,
             * которые произошли. Ну например, нам необходимо знать что пришли
             * какие-то данные, т.о. в маске должна присутствовать следующая величина:
             * MASK_RXCHAR. Если нам, например, ещё нужно знать об изменении состояния
             * линий CTS и DSR, то маска уже будет выглядеть так:
             * SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR
             */
            int mask = SerialPort.MASK_RXCHAR;
            /*
             * Выставляем подготовленную маску
             */
            serialPort.setEventsMask(mask);
            /*
             * Добавляем собственно интерфейс через который мы и будем узнавать о
             * нужных нам событиях
             */
            serialPort.addEventListener(new SerialPortReader(serialPort));
        } catch (SerialPortException ex) {
            ex.printStackTrace();
        }
    }

    static class SerialPortReader implements SerialPortEventListener {

        private final SerialPort serialPort;
        private final RoomController roomController;
        private final StringBuilder arduinoMessage = new StringBuilder();
        private Boolean receivingMessage = false;

        public SerialPortReader(SerialPort serialPort) {
            roomController = new RoomController();
            this.serialPort = serialPort;
        }

        public void serialEvent(SerialPortEvent event) {


            /*
             * Объект типа SerialPortEvent несёт в себе информацию о том какое событие
             * произошло и значение. Так например если пришли данные то метод
             * event.getEventValue() вернёт нам количество байт во входном буфере.
             */

            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    byte[] buffer = serialPort.readBytes();
                    for (byte b : buffer) {
                        System.out.print((char) b);
                        if (b == '&') {
                            receivingMessage = true;
                            arduinoMessage.setLength(0);
                        } else if (receivingMessage) {
                            if (b == '\n') {
                                receivingMessage = false;
                                String rf_id = arduinoMessage.toString();
                                System.out.println(rf_id);

                                //String rf_id = "F4R3G5E2";
                                // Database
                                Employee employee = null;
                                try {
                                    employee = findByRfId(rf_id);

                                    if (employee.getId() == -1) {
                                        System.out.println("Entity not found -> create new user");
                                        PreparedStatement st = roomController.getInstance().prepareStatement("INSERT INTO employee (rf_id) VALUES (?);");
                                        st.setString(1, rf_id);
                                        st.executeUpdate();
                                        st.close();
                                        employee = findByRfId(rf_id);
                                    }

                                    // Add log row
                                    System.out.println("Add log row");
                                    LocalDateTime localDateTime = Instant.ofEpochMilli(new java.util.Date().getTime())
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDateTime();
                                    PreparedStatement st = roomController.getInstance().prepareStatement("INSERT INTO visit_log (time, employee_id) VALUES (?, ?)");
                                    st.setObject(1, localDateTime);
                                    st.setLong(2, employee.getId());
                                    st.executeUpdate();
                                    st.close();

                                } catch (SQLException |
                                        ClassNotFoundException throwable) {
                                    throwable.printStackTrace();
                                }

                                if (employee != null && employee.getAccess_level() != 0) {
                                    System.out.println("Пустить!");
                                    serialPort.writeByte((byte) 5);
                                } else {
                                    System.out.println("Не пустить!");
                                    serialPort.writeByte((byte) 0);
                                }


                            } else {
                                arduinoMessage.append((char) b);
                            }
                        }
                    }
                } catch (SerialPortException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private Employee findByRfId(String rf_id) throws SQLException, ClassNotFoundException {

            System.out.println("Try search user with rf_id");
            PreparedStatement st = roomController.getInstance().prepareStatement("SELECT id, access_level FROM employee WHERE rf_id = ?");
            st.setString(1, rf_id);
            ResultSet resultSet = st.executeQuery();

            long id = -1;
            int access_level = 0;
            if (resultSet != null && resultSet.next()) {
                id = resultSet.getLong(1);
                access_level = resultSet.getInt(2);
                System.out.println("id: " + id);
                System.out.println("rf_id: " + rf_id);
                System.out.println("access_level: " + access_level);
                System.out.println("===================");
            }
            st.close();
            return new Employee(id, access_level, rf_id);
        }
    }
}
