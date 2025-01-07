package org.kweakkweak.KweakBot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.UnknownHostException;

public class CheckConnection {
    static boolean isWarned = false;
    public static void start() throws InterruptedException, IOException {
        if (isWarned) {
            while (true) {
                try {
                    HttpURLConnection as = (HttpURLConnection) new URL("https://telegram.org").openConnection();
                    if (as.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        System.out.println("Подключение восстановлено");
                        as.disconnect();
                        isWarned = false;
                        return;
                    }
                } catch (UnknownHostException | NoRouteToHostException ignore) {}
                Thread.sleep(1500);
            }
        } else {
            try {
                HttpURLConnection as = (HttpURLConnection) new URL("https://telegram.org").openConnection();
                if (as.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    warn();
                }
                as.disconnect();
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