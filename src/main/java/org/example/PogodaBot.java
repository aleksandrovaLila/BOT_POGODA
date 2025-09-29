package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PogodaBot extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return "Pogoda Bot";
    }

    @Override
    public String getBotToken() {
        return "8481929275:AAHBEnnmRq9rnlHf56q3a2caJSciI8cbtvo";
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
    }
}
