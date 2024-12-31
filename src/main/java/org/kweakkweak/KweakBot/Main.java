package org.kweakkweak.KweakBot;

import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;

public class Main {
    static final String version = "1.0.0";
    static final String error = "!: Ошибка синтаксиса\n-- Воспользуйся командой help";
    public static void main(String[] args) throws Exception {
        try (SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory()) {
            System.out.println("KweakBot " + version + " by KweakKweak");
            checkConnection();
            TDLibSettings settings = TDLibSettings.create(new APIToken(APIID, "APIHash"));
            // README.md > Информация для разработчиков
            settings.setApplicationVersion(version);
            Path sessionPath = Paths.get("KweakBotSession");
            settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
            try (var app = new BuildClient(clientFactory.builder(settings))) {
                while (true) {
                    System.out.print("?: ");
                    String command = new BufferedReader(new InputStreamReader(System.in, "Cp866")).readLine();
                    if (!command.isBlank()) {
                        String[] commandArgs = resolveCommand(command);
                        switch (commandArgs[0]) {
                            case "sendvoice":
                                String[] voicePath;
                                long id;
                                int duration;
                                try {
                                    id = Long.parseLong(commandArgs[1]);
                                    voicePath = command.split("\"");
                                } catch (InputMismatchException | NullPointerException |
                                         ArrayIndexOutOfBoundsException e) {
                                    System.err.println(error);
                                    break;
                                }
                                try {
                                    duration = Integer.parseInt(commandArgs[2]);
                                } catch (NullPointerException | ArrayIndexOutOfBoundsException |
                                         NumberFormatException e) {
                                    duration = -1;
                                }
                                app.getClient().send(new TdApi.SendMessage(id, 0, null, null, null, new TdApi.InputMessageVoiceNote(new TdApi.InputFileLocal(voicePath[1]), duration, new byte[1], null, null)));
                                break;
                            case "simulate":
                                long idForSimulating;
                                String action;
                                try {
                                    idForSimulating = Long.parseLong(commandArgs[2]);
                                    action = String.format("В чате %s видно, что ты " + switch (commandArgs[1]) {
                                        case "typing" -> "печатаешь";
                                        case "playing" -> "играешь в игру";
                                        case "recordingvideonote" -> "записываешь кружок";
                                        case "recordingvoice" -> "записываешь голосовое";
                                        case "sendingvideo" -> "отправляешь видео";
                                        case "recordingvideo" -> "снимаешь видео";
                                        case "sendingphoto" -> "отправляешь фото";
                                        case "sendingdoc" -> "отправляешь документ";
                                        case "choosingcontact" -> "выбираешь контакт";
                                        case "choosingsticker" -> "выбираешь стикер";
                                        case "choosinglocation" -> "выбираешь геолокацию";
                                        default -> null;
                                    }, idForSimulating);
                                } catch (NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
                                    System.err.println(error);
                                    break;
                                }
                                System.out.println(action);
                                System.out.println("Напиши stop для остановки");
                                TdApi.ChatAction chatAction = switch (commandArgs[1]) {
                                    case "typing" -> new TdApi.ChatActionTyping();
                                    case "playing" -> new TdApi.ChatActionStartPlayingGame();
                                    case "recordingvideonote" -> new TdApi.ChatActionRecordingVideoNote();
                                    case "recordingvoice" -> new TdApi.ChatActionRecordingVoiceNote();
                                    case "sendingvideo" -> new TdApi.ChatActionUploadingVideo();
                                    case "recordingvideo" -> new TdApi.ChatActionRecordingVideo();
                                    case "sendingphoto" -> new TdApi.ChatActionUploadingPhoto();
                                    case "sendingdoc" -> new TdApi.ChatActionUploadingDocument();
                                    case "choosingcontact" -> new TdApi.ChatActionChoosingContact();
                                    case "choosingsticker" -> new TdApi.ChatActionChoosingSticker();
                                    case "choosinglocation" -> new TdApi.ChatActionChoosingLocation();
                                    default -> null;
                                };
                                if (chatAction == null) {
                                    System.err.println(error);
                                    break;
                                }
                                BufferedReader asas = new BufferedReader(new InputStreamReader(System.in));
                                while (true) {
                                    if (asas.ready()) {
                                        if (asas.readLine().equalsIgnoreCase("stop"))
                                            break;
                                    } else {
                                        Thread.sleep(1500);
                                    }
                                    app.getClient().send(new TdApi.SendChatAction(idForSimulating, 0, chatAction));
                                }
                                System.out.println("Завершено!");
                                break;
                            case "logout":
                                app.getClient().logOutAsync();
                                System.exit(0);
                                break;
                            case "about":
                                System.out.println("Консольная програмка, способная выполнять некоторые забавные манипуляции с вашего аккаунта Telegram\nGitHub программы - https://github.com/KitCat1712/KweakBot-Telegram\nTelegram разработчика - https://t.me/ByKweakKweak\nMade with love");
                                break;
                            case "help":
                                System.out.println("Доступные команды:\nsendvoice [ID чата] [длительность в секундах (для отображения)]* \"путь к файлу\" - отправить файл как голосовое\nsimulate [typing, playing, recordingvideonote, recordingvoice, recordingvideo, sendingvideo, sendingphoto, sendingdoc, choosingcontact, choosingsticker, choosinglocation] [ID чата] - симуляция действий пользователя в чате\nabout - о программе\nexit - завершить работу\nlogout - выйти с аккаунта и завершить работу\n\n* - необязательный параметр");
                                break;
                            case "exit":
                                System.exit(0);
                            default:
                                System.err.println(error);
                                break;
                        }
                    }
                }
            }
        }
    }
    private static String[] resolveCommand(String command) {
        try {
            return command.toLowerCase().split(" ");
        } catch (InputMismatchException e) {
            return new String[]{command};
        }
    }
    private static void checkConnection() throws InterruptedException, IOException {
        try {
            HttpURLConnection as = (HttpURLConnection) new URL("https://telegram.org").openConnection();
            if (as.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("Проблемы с подключением!\nПрограмма недоступна");
                Thread.sleep(5000);
                System.exit(0);
            }
            as.disconnect();
        } catch (UnknownHostException e) {
            System.out.println("Проблемы с подключением!\nПрограмма недоступна");
            Thread.sleep(5000);
            System.exit(0);
        }
    }
}

class BuildClient implements AutoCloseable {
    private final SimpleTelegramClient client;
    public BuildClient(SimpleTelegramClientBuilder clientBuilder) {
        this.client = clientBuilder.build(AuthenticationSupplier.qrCode());
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

    public SimpleTelegramClient getClient() {
        return client;
    }
}