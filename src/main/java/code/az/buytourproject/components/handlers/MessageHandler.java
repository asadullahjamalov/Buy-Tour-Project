package code.az.buytourproject.components.handlers;

import code.az.buytourproject.enums.OperationType;
import code.az.buytourproject.models.Operation;
import code.az.buytourproject.models.Question;
import code.az.buytourproject.models.TelegramSession;
import code.az.buytourproject.repositories.OperationRepo;
import code.az.buytourproject.repositories.QuestionRepo;
import code.az.buytourproject.services.KeyboardServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Component
public class MessageHandler {
    OperationRepo operationRepo;
    QuestionRepo questionRepo;
    KeyboardServiceImpl keyboardService;

    public MessageHandler(OperationRepo operationRepo, QuestionRepo questionRepo, KeyboardServiceImpl keyboardService) {
        this.operationRepo = operationRepo;
        this.questionRepo = questionRepo;
        this.keyboardService = keyboardService;
    }

    public SendMessage handleStartCommand(Update update, TelegramSession telegramSession) {
        Message message = update.getMessage();
        long chatId = message.getChatId();
        telegramSession.setActive(true);
        Question first_question = questionRepo.findFirstQuestion();
        String first_question_str = first_question.getQuestion_az() + "\n" + first_question.getQuestion_en() +
                "\n" + first_question.getQuestion_ru();
        SendMessage sendMessage = new SendMessage(chatId, first_question_str);
        if (telegramSession.getChatId() == update.getMessage().getFrom().getId()) {
            if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_az())) {
                sendMessage.setText("Siz artıq sessiyanı başlatmısız. Ləğv etmək üçün /stop əmrini yerinə yetirin");
            }
            if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_en())) {
                sendMessage.setText("You have already started the session. Execute the /stop command to cancel.");
            }
            if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_ru())) {
                sendMessage.setText("Вы уже начали сеанс. Для отмены выполните команду /stop .");
            }
            return sendMessage;
        }
        ReplyKeyboardMarkup languageButtons = keyboardService.getLocaleKeyboard();
        sendMessage.setReplyMarkup(languageButtons);
        return sendMessage;
    }

    public SendMessage handleStopCommand(Update update, TelegramSession telegramSession) {
        if (telegramSession.getChatId() == update.getMessage().getFrom().getId()) {
            telegramSession.setChatId(0);
            telegramSession.setActive(false);
            telegramSession.getQuestion_answer_map().clear();
            if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_az())) {
                return new SendMessage(update.getMessage().getChatId().toString(), "Sessiyanı dayandırdınız. Başlamaq üçün /start əmrini yerinə yetirin. ");
            } else if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_en())) {
                return new SendMessage(update.getMessage().getChatId().toString(), "You cancelled the session. Execute the /start command to get started. ");
            } else if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_ru())) {
                return new SendMessage(update.getMessage().getChatId().toString(), "Вы остановили сеанс. Выполните команду /start ,чтобы начать. ");
            }
        } else if (telegramSession.getChatId() == 0 && telegramSession.isActive() == false) {
            return new SendMessage(update.getMessage().getChatId().toString(), "/stop -dan əvvəl /start etməlisiniz." + "\n" +
                    "You must /start before /stop." + "\n" + "Вы должны /start до /stop .");

        } else if (telegramSession.getChatId() == 0 && telegramSession.isActive() == true) {
            return new SendMessage(update.getMessage().getChatId().toString(), "Zəhmət olmasa yenidən /start edin və dili seçin." + "\n" +
                    "Please /start again and select a language." + "\n" + "Пожалуйста, /start еще раз и выберите язык.");

        }
        return null;
    }

    public void setLocale(Update update, TelegramSession telegramSession) {
        telegramSession.setQuestion(operationRepo.findFirstOperation().getNextQuestion());
        telegramSession.setOperationList(operationRepo.findOperationByQuestion(telegramSession.getQuestion()));
        telegramSession.setChatId(update.getMessage().getFrom().getId());
        telegramSession.setOperation(operationRepo.findFirstOperation());
        if (update.getMessage().getText().equals(operationRepo.findFirstOperation().getText_az())) {
            telegramSession.setLocale(update.getMessage().getText());
            telegramSession.getQuestion_answer_map().put(operationRepo.findFirstOperation().getQuestion().getKey(),
                    operationRepo.findFirstOperation().getText_az());
        } else if (update.getMessage().getText().equals(operationRepo.findFirstOperation().getText_en())) {
            telegramSession.setLocale(update.getMessage().getText());
            telegramSession.getQuestion_answer_map().put(operationRepo.findFirstOperation().getQuestion().getKey(),
                    operationRepo.findFirstOperation().getText_en());
        } else if (update.getMessage().getText().equals(operationRepo.findFirstOperation().getText_ru())) {
            telegramSession.setLocale(update.getMessage().getText());
            telegramSession.getQuestion_answer_map().put(operationRepo.findFirstOperation().getQuestion().getKey(),
                    operationRepo.findFirstOperation().getText_ru());
        }
    }


    public SendMessage handleMessage(Update update, TelegramSession telegramSession) {
        SendMessage sendMessage = new SendMessage();

        if (!telegramSession.isActive())
            return new SendMessage(update.getMessage().getChatId().toString(), "Zəhmət olmasa /start edin və dili seçin." + "\n" +
                    "Please /start and select a language." + "\n" + "Пожалуйста, /start и выберите язык.");


        if (telegramSession.getChatId() == 0) {
            setLocale(update, telegramSession);
        }

        if (telegramSession.getOperation().getQuestion().getKey().equals("endMessage")) {
            if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_az())) {
                return sendMessage.setChatId(update.getMessage().getChatId().toString()).setText("Anket başa çatdı.");
            } else if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_en())) {
                return sendMessage.setChatId(update.getMessage().getChatId().toString()).setText("The survey is over.");
            } else if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_ru())) {
                return sendMessage.setChatId(update.getMessage().getChatId().toString()).setText("Опрос окончен.");
            }
        }


        if (telegramSession.getOperation().getNextQuestion() != null) {
            if (!telegramSession.getOperation().getQuestion().getKey().equals("endMessage")) {
                telegramSession.setQuestion(telegramSession.getOperation().getNextQuestion());
            }

            if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_az())) {

                telegramSession.getQuestion_answer_map().put(telegramSession.getOperation().getQuestion().getKey(), update.getMessage().getText());


                System.out.println(telegramSession.getOperation().getNextQuestion().getQuestion_az());

                telegramSession.setOperation(operationRepo.getOperationsByQuestion(telegramSession.getQuestion()).get(0));

                List<Operation> operations = operationRepo.getOperationsByQuestion(telegramSession.getQuestion());

//
                if (telegramSession.getOperation().getType().equals(OperationType.BUTTON)) {
//                    if (operationRepo.getOperationByText_az(update.getMessage().getText()) == null) {
//                        return new SendMessage(update.getMessage().getChatId().toString(), "Duzgun cavab yaz");
//                    }

                    System.out.println("button");
                    sendMessage.setReplyMarkup(keyboardService.getKeyboardButtons(telegramSession.getOperation().getQuestion(), telegramSession));
                } else if (telegramSession.getOperation().getType().equals(OperationType.FREETEXT)) {
                    if (update.getMessage().getText().equals("TourApp təklif etsin.")) {
                        System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbb");
                    }
                    System.out.println("freetext");
                }
                return sendMessage.setChatId(update.getMessage().getChatId().toString())
                        .setText(telegramSession.getQuestion().getQuestion_az());

            } else if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_en())) {
                telegramSession.getQuestion_answer_map().put(telegramSession.getOperation().getQuestion().getKey(), update.getMessage().getText());
                System.out.println(telegramSession.getQuestion_answer_map().toString());

//                telegramSession.setQuestion(telegramSession.getOperation().getNextQuestion());
                telegramSession.setOperation(operationRepo.getOperationsByQuestion(telegramSession.getQuestion()).get(0));

                if (telegramSession.getOperation().getType().equals(OperationType.BUTTON)) {
                    System.out.println("button");
                    sendMessage.setReplyMarkup(keyboardService.getKeyboardButtons(telegramSession.getOperation().getQuestion(), telegramSession));
                } else if (telegramSession.getOperation().getType().toString().equals("FREETEXT")) {
                    System.out.println("freetext");
                }
                return sendMessage.setChatId(update.getMessage().getChatId().toString())
                        .setText(telegramSession.getQuestion().getQuestion_en());

            } else if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_ru())) {

                telegramSession.getQuestion_answer_map().put(telegramSession.getOperation().getQuestion().getKey(), update.getMessage().getText());
                System.out.println(telegramSession.getQuestion_answer_map().toString());

//                telegramSession.setQuestion(telegramSession.getOperation().getNextQuestion());
                telegramSession.setOperation(operationRepo.getOperationsByQuestion(telegramSession.getQuestion()).get(0));

                if (telegramSession.getOperation().getType().equals(OperationType.BUTTON)) {
                    System.out.println("button");
                    sendMessage.setReplyMarkup(keyboardService.getKeyboardButtons(telegramSession.getOperation().getQuestion(), telegramSession));
                } else if (telegramSession.getOperation().getType().toString().equals("FREETEXT")) {
                    System.out.println("freetext");
                }
                return sendMessage.setChatId(update.getMessage().getChatId().toString())
                        .setText(telegramSession.getQuestion().getQuestion_ru());
            }
        }


        return sendMessage.setChatId(update.getMessage().getChatId().toString())
                .setText("Anket basha chatdi");


    }

}
