package org.example;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.sql.*;

public class Main {
    static final String DATABASE_URL = "jdbc:postgresql://jdroid.ru/access_db";

    static final String USER = "operator1";
    static final String PASSWORD = "Ja8UAw8YTQ";

    public static void main(String[] args) throws SQLException {


        /*
         * Передаём в конструктор имя порта
         */
        SerialPort serialPort = new SerialPort("/dev/ttyUSB0");
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


        // DB side
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");

            System.out.println("Creating connection to database...");
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            System.out.println("Getting records...");
            statement = connection.createStatement();

            String SQL = "SELECT * FROM employee";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String rf_id = resultSet.getString(2);
                String access_level = resultSet.getString(3);

                System.out.println("id: " + id);
                System.out.println("rf_id: " + rf_id);
                System.out.println("access_level: " + access_level);
                System.out.println("===================\n");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    static class SerialPortReader implements SerialPortEventListener {

        private final SerialPort serialPort;

        public SerialPortReader(SerialPort serialPort) {
            this.serialPort = serialPort;
        }

        StringBuilder message = new StringBuilder();
        Boolean receivingMessage = false;

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
                        System.out.println(b);
                        System.out.println((char) b);
                        if (b == '&') {
                            receivingMessage = true;
                            message.setLength(0);
                        } else if (receivingMessage) {
                            if (b == '\n') {
                                receivingMessage = false;
                                String toProcess = message.toString();
                                System.out.println(toProcess);
                                // Database
                                serialPort.writeByte((byte) 1);
                            } else {
                                message.append((char) b);
                            }
                        }
                    }
                } catch (SerialPortException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
