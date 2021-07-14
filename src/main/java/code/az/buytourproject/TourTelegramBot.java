package code.az.buytourproject;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class TourTelegramBot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

//    @Autowired
//    QuestionRepo questionRepo;
//
//    @Autowired
//    OperationRepo operationRepo;
//
//    Map<Long, TourSurveyData> survey_map = new HashMap<>();
//
//    TourSurveyData tourSurveyData;



    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }



    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Message message = update.getMessage();
        long chatId = message.getChatId();
        String chatIdStr = String.valueOf(chatId);
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId: {},  with text: {}",
                    message.getFrom().getFirstName(), message.getChatId(), message.getText());
//            if (message.getText().equals("/start")) {
//                SendMessage sendMessage = startMessaging(update);
//                return sendMessage;
//            }
        }

        return null;

    }

//    public SendMessage startMessaging(Update update) {
//        Message message = update.getMessage();
//        long chatId = message.getChatId();
//        String chatIdStr = String.valueOf(chatId);
//        String first_question = questionRepo.findFirstQuestion().getContext_az() + "\n" +
//                questionRepo.findFirstQuestion().getContext_en() + "\n" +
//                questionRepo.findFirstQuestion().getContext_ru();
//        SendMessage sendMessage = new SendMessage(chatIdStr, first_question);
//
//        InlineKeyboardMarkup languageButtons = getLanguageButtons();
//        sendMessage.setReplyMarkup(languageButtons);
//        return sendMessage;
//    }
//
//    public SendMessage selectLanguage(Update update, Long chatId, String chatIdStr) {
//
//        CallbackQuery callbackQuery = update.getCallbackQuery();
//
//        Operation firstOperation = operationRepo.findOperationByQuestion(questionRepo.findFirstQuestion());
//        Question secondQuestion = firstOperation.getNextQuestion();
//
//        if (callbackQuery.getData().equals("buttonAz")) {
//            tourSurveyData.setLanguage(firstOperation.getText_az());
//          return   new SendMessage(chatIdStr, secondQuestion.getContext_az());
//
//        } else if (callbackQuery.getData().equals("buttonEn")) {
//            tourSurveyData.setLanguage(firstOperation.getText_en());
//        } else if (callbackQuery.getData().equals("buttonRu")) {
//            tourSurveyData.setLanguage(firstOperation.getText_ru());
//        }
//
//        return new SendMessage(chatIdStr, secondQuestion.getContext_az());
//    }
//
//    private InlineKeyboardMarkup getLanguageButtons() {
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        Operation firstOperation = operationRepo.findOperationByQuestion(questionRepo.findFirstQuestion());
//        InlineKeyboardButton buttonLanguageAz = new InlineKeyboardButton().setText(firstOperation.getText_az());
//        InlineKeyboardButton buttonLanguageEn = new InlineKeyboardButton().setText(firstOperation.getText_en());
//        InlineKeyboardButton buttonLanguageRu = new InlineKeyboardButton().setText(firstOperation.getText_ru());
//
//        //Every button must have callBackData, or else not work !
//        buttonLanguageAz.setCallbackData("buttonAz");
//        buttonLanguageEn.setCallbackData("buttonEn");
//        buttonLanguageRu.setCallbackData("buttonRu");
//
//        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
//        keyboardButtonsRow1.add(buttonLanguageAz);
//        keyboardButtonsRow1.add(buttonLanguageEn);
//        keyboardButtonsRow1.add(buttonLanguageRu);
//
//        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
//        rowList.add(keyboardButtonsRow1);
//
//        inlineKeyboardMarkup.setKeyboard(rowList);
//
//        return inlineKeyboardMarkup;
//    }

}