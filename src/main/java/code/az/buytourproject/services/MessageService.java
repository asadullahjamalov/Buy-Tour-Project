package code.az.buytourproject.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessageService {

    public SendMessage sendMessage(long chatId, String message){
        return new SendMessage(chatId, message);
    }

    public SendMessage sendMessage(long chatId, String message, long langId){
        return new SendMessage(chatId, message);
    }
}
