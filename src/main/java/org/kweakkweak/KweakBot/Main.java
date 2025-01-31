package org.kweakkweak.KweakBot;

import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Random;

public class Main {
    static final String version = "1.0.2";
    static final String error = "!: Ошибка синтаксиса\n-- Воспользуйся командой help";
    public static void main(String[] args) throws Exception {
        try (SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory()) {
            System.out.println("KweakBot " + version + " by KweakKweak");
            CheckConnection.start();
            TDLibSettings settings = TDLibSettings.create(new APIToken(APIID, "APIHash"));
            // README.md > Информация для разработчиков
            settings.setApplicationVersion(version);
            Path sessionPath = Paths.get("KweakBotSession");
            settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
            try (var app = new BuildClient(clientFactory.builder(settings))) {
                app.getClient().getMeAsync().get();
                while (true) {
                    System.out.print("?: ");
                    String command = new BufferedReader(new InputStreamReader(System.in, "Cp866")).readLine();
                    if (!command.isBlank()) {
                        String[] commandArgs = resolveCommand(command);
                        CheckConnection.start();
                        switch (commandArgs[0]) {
                            case "sendvoice":
                                String[] voicePath;
                                long id;
                                boolean confirmedToSend = true;
                                try {
                                    id = Long.parseLong(commandArgs[1]);
                                    voicePath = command.split("\"");
                                    if (!new File(voicePath[1]).exists()) {
                                        System.err.println("!: Файл не найден");
                                        break;
                                    }
                                    if (voicePath[1].endsWith(".ogg")) {
                                        System.out.println("""
                                                -- Telegram не сможет отобразить длину голосового
                                                -- Сконвертируй файл в .mp3
                                                -- Все равно отправить? [1 - да, 0 - нет]""");
                                        while (true) {
                                            System.out.print("?: ");
                                            String confirmReader = new BufferedReader(new InputStreamReader(System.in)).readLine();
                                            if (confirmReader.equals("1")) {
                                                break;
                                            }
                                            if (confirmReader.equals("0")) {
                                                confirmedToSend = false;
                                                break;
                                            }
                                        }
                                    }
                                } catch (InputMismatchException | NullPointerException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
                                    System.err.println(error);
                                    break;
                                }
                                byte[] waveform = new byte[40];
                                new Random().nextBytes(waveform);
                                if (confirmedToSend) app.getClient().send(new TdApi.SendMessage(id, 0, null, null, null, new TdApi.InputMessageVoiceNote(new TdApi.InputFileLocal(voicePath[1]), -1, waveform, null, null)));
                                break;
                            case "simulate":
                                long idForSimulating;
                                String action;
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
                                System.out.println("Чтобы остановить, нажми Enter");
                                BufferedReader asas = new BufferedReader(new InputStreamReader(System.in));
                                while (true) {
                                    if (asas.ready()) {
                                        if (asas.readLine().isBlank()) {
                                            break;
                                        }
                                    }
                                    app.getClient().send(new TdApi.SendChatAction(idForSimulating, 0, chatAction));
                                    Thread.sleep(1350);
                                    CheckConnection.start();
                                }
                                System.out.println("Завершено!");
                                break;
                            case "logout":
                                app.getClient().logOutAsync();
                                System.exit(0);
                                break;
                            case "about":
                                System.out.println("""
                                Консольная программка, способная выполнять некоторые забавные манипуляции с вашего аккаунта Telegram
                                GitHub программы - https://github.com/KitCat1712/KweakBot-Telegram
                                Telegram разработчика - https://t.me/ByKweakKweak
                                Made with love""");
                                break;
                            case "help":
                                System.out.println("""
                                        Доступные команды:
                                        sendvoice [ID чата] "путь к файлу" - отправить файл как голосовое
                                        simulate [typing, playing, recordingvideonote, recordingvoice, recordingvideo, sendingvideo, sendingphoto, sendingdoc, choosingcontact, choosingsticker, choosinglocation] [ID чата] - симуляция действий пользователя в чате
                                        about - о программе
                                        exit - завершить работу
                                        logout - выйти с аккаунта и завершить работу""");
                                break;
                            case "exit":
                                app.getClient().close();
                                System.exit(0);
                                break;
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