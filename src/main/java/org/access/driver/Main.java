package org.access.driver;

import jssc.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

public class Main {

    static SerialPort serialPort;
    private static final int CONNECT_ATTEMPTS = 10;

    public static void main(String[] args) {
        System.out.println("Demon started on [" + new Date().toString() + "]");
        int attempt = 0;
        try {
            while (true) {

                /*
                 * Логика ожидания подключения и монитор порта
                 */
                while (!Arrays.asList(SerialPortList.getPortNames()).contains(args[0])) {
                    attempt++;
                    if (serialPort != null) {
                        System.out.println("Device was disconnected !");
                        throw new SerialPortException(args[0], "main", "Device disconnected!");
                    }
                    System.out.println("Waiting for device on serial port ["
                            + args[0] + "] (" + attempt + "/" + CONNECT_ATTEMPTS + ");");
                    if (attempt == CONNECT_ATTEMPTS) return;
                    Thread.sleep(5000);
                }

                if (serialPort == null) {
                    /*
                     * Передаём в конструктор имя порта
                     */
                    serialPort = new SerialPort(args[0]);

                    /*
                     * Открываем порт
                     */
                    serialPort.openPort();
                    attempt = 0;
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

                }
                Thread.sleep(10000);
            }
        } catch (InterruptedException | SerialPortException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Demon terminated on [" + new Date().toString() + "]");
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
                        if (b == '&') {
                            receivingMessage = true;
                            arduinoMessage.setLength(0);
                        } else if (receivingMessage) {
                            if (b == '\r') {
                                receivingMessage = false;
                                String rf_id = arduinoMessage.toString();
                                System.out.println(rf_id);

                                //String rf_id = "F4R3G5E2";
                                // Database
                                Employee employee;
                                try {
                                    employee = findByRfId(rf_id);

                                    if (employee.getId() == -1) {
                                        System.out.println("Entity not found -> create new user");

                                        PreparedStatement st = roomController.getInstance().prepareStatement("INSERT INTO employee (rf_id) VALUES (?)");
                                        st.setString(1, rf_id);
                                        st.executeUpdate();
                                        st.close();
                                        employee = findByRfId(rf_id);
                                    }


                                    if (employee.getAccessLevel() != 0) {
                                        System.out.println("Пустить!");
                                        serialPort.writeByte((byte) 5);

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

                                        st = roomController.getInstance().prepareStatement(
                                                "UPDATE employee SET " +
                                                        (((employee.getOpenCount() + 1) % 2 == 0) ? "last_out" : "last_enter") +
                                                        " = ? WHERE id = ?");
                                        st.setObject(1, localDateTime);
                                        st.setLong(2, employee.getId());
                                        st.executeUpdate();
                                        st.close();
                                        continue;
                                    }
                                } catch (SQLException |
                                        ClassNotFoundException throwable) {
                                    throwable.printStackTrace();
                                }
                                System.out.println("Не пустить!");
                                serialPort.writeByte((byte) 0);
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
            PreparedStatement st =
                    new RoomController().getInstance().prepareStatement(
                            "SELECT e.id, e.access_level, COUNT(vl.id) FROM public.employee e " +
                                    "LEFT JOIN public.visit_log vl ON vl.employee_id = e.id  WHERE e.rf_id = ? " +
                                    "GROUP BY e.id");
            st.setString(1, rf_id);
            ResultSet resultSet = st.executeQuery();

            long id = -1;
            int access_level = 0;
            long open_count = 0;
            if (resultSet != null && resultSet.next()) {
                id = resultSet.getLong(1);
                access_level = resultSet.getInt(2);
                open_count = resultSet.getLong(3);
                System.out.println("id: " + id);
                System.out.println("rf_id: " + rf_id);
                System.out.println("access_level: " + access_level);
                System.out.println("open_count: " + open_count);
                System.out.println("===================");
            }
            st.close();
            return new Employee(id, access_level, rf_id, open_count);
        }
    }
}
