# Функции

- Отправка любого файла как голосовое сообщение.
- Симуляция действий - вы можете показать чату, что вы печатаете, играете, отправляете видео и т.д., когда вы этого не делаете.

  *На этом пока всё...*

# Как найти ID чата?

- Сначала показ ID чатов необходимо включить
- Для Telegram Desktop: Настройки > Продвинутые настройки > Экспериментальные настройки > Show peer ID in Profile - включите.
- Для Unigram: Настройки > Продвинутые настройки > Показывать ID чатов - поставить галочку.
- Теперь ID чатов будет отображаться в их же профиле

## Информация для разработчиков

### Прежде чем начать работать над программой

- Программа сделана под [модифицированную TDLight-Java](https://github.com/KitCat1712/KweakBot-Telegram/blob/main/tdlight-java-3.0.0.0-SNAPSHOT.jar) библиотеку, из-за чего рекомендуется скачать и использовать именно её, вместо [оригинальной.](https://github.com/tdlight-team/tdlight-java)
- Когда вы создаете свое приложение для Telegram, вы должны его зарегистрировать. [Сделайте это здесь.](https://core.telegram.org/api/obtaining_api_id)
- Найдите в файле Main.java строку:

```java
TDLibSettings settings = TDLibSettings.create(new APIToken(APIID, "APIHash"));
```

- Зарегистрировав приложение, скопируйте содержимое в полях "App api_id", "App api_hash" и вставьте на место "APIID" и "APIHash" (не удаляя кавычки) соответственно.

  *После этих действий, программа будет полноценно готова к работе.*

### Лицензии
TDLight is licensed by Andrea Cavalli andrea@cavallium.it under the terms of the GNU Lesser General Public License 3

В программе применяются библиотеки Slf4j, использующие [лицензию MIT](https://github.com/qos-ch/slf4j/blob/master/LICENSE.txt)

В программе применена библиотека ZXing, использующая [лицензию Apache 2.0](https://github.com/zxing/zxing/blob/master/LICENSE)

В программе применена библиотека atlassian-util-concurrent, использующая [лицензию Apache 2.0](https://github.com/andyglick/atlassian-util-concurrent/blob/master/LICENSE.txt)

В программе применена библиотека reactive-streams, использующая [лицензию MIT](https://github.com/reactive-streams/reactive-streams-jvm/blob/master/LICENSE)
