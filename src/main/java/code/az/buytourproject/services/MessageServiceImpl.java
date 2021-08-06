package code.az.buytourproject.services;

import code.az.buytourproject.daos.interfaces.QuestionDAO;
import code.az.buytourproject.models.Question;
import code.az.buytourproject.models.TelegramSession;
import code.az.buytourproject.services.interfaces.KeyboardService;
import code.az.buytourproject.services.interfaces.MessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class MessageServiceImpl implements MessageService {

    QuestionDAO questionDAO;
    KeyboardService keyboardService;

    public MessageServiceImpl(QuestionDAO questionDAO, KeyboardService keyboardService) {
        this.questionDAO = questionDAO;
        this.keyboardService = keyboardService;
    }

    @Override
    public SendMessage sendWrongAnswerMessage(long chatId, String locale) {
        switch (locale) {
            case "Azərbaycan":
                return new SendMessage(chatId, "Cavabı doğru daxil edin.");
            case "Pусский":
                return new SendMessage(chatId, "Введите правильный ответ.");
            default:
                return new SendMessage(chatId, "Enter the correct answer.");
        }
    }

    @Override
    public SendMessage sendSelectLanguageMessage(long chatId) {
        return new SendMessage(chatId, "Zəhmət olmasa dil seçin." + "\n" +
                "Please, select a language." + "\n" + "Пожалуйста, выберите язык.");
    }

    @Override
    public SendMessage sendStartAndSelectLanguageMessage(long chatId) {
        return new SendMessage(chatId, "Zəhmət olmasa /start edin və dili seçin." + "\n" +
                "Please /start and select a language." + "\n" + "Пожалуйста, /start и выберите язык.");
    }

    @Override
    public SendMessage sendStartBeforeStopMessage(long chatId) {
        return new SendMessage(chatId, "/stop -dan əvvəl /start etməlisiniz." + "\n" +
                "You must /start before /stop." + "\n" + "Вы должны /start до /stop .");
    }

    @Override
    public SendMessage sendStopSessionMessage(long chatId, String locale, TelegramSession telegramSession) {
        telegramSession.setLocale(null);
        switch (locale) {
            case "Azərbaycan":
                return new SendMessage(chatId, "Sessiyanı dayandırdınız. Başlamaq üçün /start əmrini yerinə yetirin.");
            case "Pусский":
                return new SendMessage(chatId, "Вы остановили сеанс. Выполните команду /start ,чтобы начать.");
            default:
                return new SendMessage(chatId, "You cancelled the session. Execute the /start command to get started.");
        }
    }

    @Override
    public SendMessage sendStartAgainAndSelectLanguageMessage(long chatId) {
        return new SendMessage(chatId, "Zəhmət olmasa yenidən /start edin və dili seçin." + "\n" +
                "Please /start again and select a language." + "\n" + "Пожалуйста, /start еще раз и выберите язык.");
    }

    @Override
    public SendMessage sendSessionAlreadyStartedMessage(long chatId, String locale) {
        switch (locale) {
            case "Azərbaycan":
                return new SendMessage(chatId, "Siz artıq sessiyanı başlatmısız. Ləğv etmək üçün /stop əmrini yerinə yetirin");
            case "Pусский":
                return new SendMessage(chatId, "Вы уже начали сеанс. Для отмены выполните команду /stop .");
            default:
                return new SendMessage(chatId, "You have already started the session. Execute the /stop command to cancel.");
        }
    }

    @Override
    public SendMessage sendFirstMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        Question first_question = questionDAO.findFirstQuestion();
        String first_question_str = first_question.getQuestion_az() + "\n" + first_question.getQuestion_en() +
                "\n" + first_question.getQuestion_ru();
        ReplyKeyboardMarkup languageButtons = keyboardService.getLocaleKeyboard();
        sendMessage.setChatId(chatId).setText(first_question_str).setReplyMarkup(languageButtons);
        return sendMessage;
    }

    @Override
    public SendMessage sendSurveyFinishedMessage(long chatId, String locale, String finishedSurvey) {
        switch (locale) {
            case "Azərbaycan":
                return new SendMessage(chatId, "Anket başa çatıb. \n" );
            case "Pусский":
                return new SendMessage(chatId, "Опрос окончен. \n" );
            default:
                return new SendMessage(chatId, "The survey was finished. \n" );
        }
    }

    @Override
    public String getCaptionByLocale(String locale) {
        switch (locale) {
            case "Azərbaycan":
                return  "Təklifi bəyəndinizsə, əlaqə nömrənizi 'reply' olaraq qeyd edin.";
            case "Pусский":
                return  "Если вам нравится это предложение, отправьте контактную информацию в ответном сообщении.";
            default:
                return  "If you like this suggestion, please send contact info as reply message.";
        }
    }

}
