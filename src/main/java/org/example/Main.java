package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    public static void main(String[] args) throws TelegramApiException { //запуск
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class); //Создает экземпляр API для работы с Telegram
        botsApi.registerBot(new PogodaBot()); //Регистрация бота, получение сообщений от пользователей
    }

}
