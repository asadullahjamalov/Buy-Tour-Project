package code.az.buytourproject.services;

import code.az.buytourproject.models.TelegramSession;
import code.az.buytourproject.repositories.OperationRepo;
import code.az.buytourproject.services.interfaces.MessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public SendMessage sendWrongAnswerMessage(long chatId, String language){
        switch (language){
            case "Azərbaycan":
                return new SendMessage(chatId, "Cavabı doğru daxil edin.");
            case "Pусский":
                return new SendMessage(chatId, "Введите правильный ответ.");
            default:
                return new SendMessage(chatId, "Enter the correct answer.");
        }
    }

    @Override
    public SendMessage sendSelectLanguageMessage(long chatId){
        return new SendMessage(chatId, "Zəhmət olmasa dil seçin." + "\n" +
                "Please, select a language." + "\n" + "Пожалуйста, выберите язык.");
    }

    @Override
    public SendMessage sendStartAndSelectLanguageMessage(long chatId){
        return new SendMessage(chatId, "Zəhmət olmasa /start edin və dili seçin." + "\n" +
                "Please /start and select a language." + "\n" + "Пожалуйста, /start и выберите язык.");
    }

    @Override
    public SendMessage sendStartBeforeStopMessage(long chatId){
        return new SendMessage(chatId, "/stop -dan əvvəl /start etməlisiniz." + "\n" +
                "You must /start before /stop." + "\n" + "Вы должны /start до /stop .");
    }

    @Override
    public SendMessage sendStopSessionMessage(long chatId, String language, TelegramSession telegramSession) {
        telegramSession.setLocale(null);
        switch (language){
            case "Azərbaycan":
                return new SendMessage(chatId, "Sessiyanı dayandırdınız. Başlamaq üçün /start əmrini yerinə yetirin.");
            case "Pусский":
                return new SendMessage(chatId, "Вы остановили сеанс. Выполните команду /start ,чтобы начать.");
            default:
                return new SendMessage(chatId, "You cancelled the session. Execute the /start command to get started.");
        }
    }

    @Override
    public SendMessage sendStartAgainAndSelectLanguageMessage(long chatId){
        return new SendMessage(chatId, "Zəhmət olmasa yenidən /start edin və dili seçin." + "\n" +
                "Please /start again and select a language." + "\n" + "Пожалуйста, /start еще раз и выберите язык.");
    }

    @Override
    public SendMessage sendSessionAlreadyStartedMessage(long chatId, String language) {
        switch (language){
            case "Azərbaycan":
                return new SendMessage(chatId, "Siz artıq sessiyanı başlatmısız. Ləğv etmək üçün /stop əmrini yerinə yetirin");
            case "Pусский":
                return new SendMessage(chatId, "Вы уже начали сеанс. Для отмены выполните команду /stop .");
            default:
                return new SendMessage(chatId, "You have already started the session. Execute the /stop command to cancel.");
        }
    }

}
