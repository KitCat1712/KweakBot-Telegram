package org.kweakkweak.KweakBot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.UnknownHostException;

public class CheckConnection {
    private static boolean isWarned = false;
    public static void start() throws InterruptedException, IOException {
        HttpURLConnection connection;
        URL telegramWeb = new URL("https://telegram.org");
        if (isWarned) {
            while (true) {
                try {
                    connection = (HttpURLConnection) telegramWeb.openConnection();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        System.out.println("Подключение восстановлено");
                        connection.disconnect();
                        isWarned = false;
                        return;
                    }
                } catch (UnknownHostException | NoRouteToHostException ignore) {}
                Thread.sleep(1500);
            }
        } else {
            try {
                connection = (HttpURLConnection) telegramWeb.openConnection();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    warn();
                }
                connection.disconnect();
            } catch (UnknownHostException | NoRouteToHostException e) {
                warn();
            }
        }
    }
    private static void warn() throws InterruptedException, IOException {
        System.out.println("Проблемы с подключением!");
        isWarned = true;
        start();
    }
}