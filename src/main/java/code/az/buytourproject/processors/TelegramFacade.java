package code.az.buytourproject.processors;

import code.az.buytourproject.enums.OperationType;
import code.az.buytourproject.models.Operation;
import code.az.buytourproject.processors.handlers.MessageHandler;
import code.az.buytourproject.enums.CommandType;
import code.az.buytourproject.models.TelegramSession;
import code.az.buytourproject.repositories.OperationRepo;
import code.az.buytourproject.repositories.QuestionRepo;
import code.az.buytourproject.services.KeyboardServiceImpl;
import code.az.buytourproject.services.interfaces.KeyboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class TelegramFacade {

    MessageHandler messageHandler;

    OperationRepo operationRepo;

    QuestionRepo questionRepo;

    KeyboardService keyboardService;

    public TelegramFacade(MessageHandler messageHandler, OperationRepo operationRepo, QuestionRepo questionRepo, KeyboardService keyboardService) {
        this.messageHandler = messageHandler;
        this.operationRepo = operationRepo;
        this.questionRepo = questionRepo;
        this.keyboardService = keyboardService;
    }


    public BotApiMethod<?> handleUpdate(Update update, Map<Long, TelegramSession> telegramSessionMap) {
        log.info("New message from User:{}, chatId: {},  with text: {}",
                update.getMessage().getFrom().getFirstName(),
                update.getMessage().getChatId(), update.getMessage().getText());
        if (!telegramSessionMap.containsKey(update.getMessage().getChatId())) {
            System.out.println("new session was created");
            telegramSessionMap.put(update.getMessage().getChatId(), new TelegramSession());
        }


        String inputMessage = update.getMessage().getText();
        if (inputMessage.equals(CommandType.START.getValue())) {
            telegramSessionMap.get(update.getMessage().getChatId()).setQuestion(questionRepo.findFirstQuestion());
            telegramSessionMap.get(update.getMessage().getChatId()).setOperationList(operationRepo.getOperationsByQuestion(telegramSessionMap.get(update.getMessage().getChatId()).getQuestion()));
            System.out.println("first question " + telegramSessionMap.get(update.getMessage().getChatId()).getQuestion().getQuestion_az());
//            telegramSessionMap.get(update.getMessage().getChatId()).setOperation(operationRepo.getOperationsByQuestion(telegramSessionMap.get(update.getMessage().getChatId()).getQuestion()).get(0));
            return messageHandler.handleStartCommand(update, telegramSessionMap.get(update.getMessage().getChatId()));
        } else if (inputMessage.equals(CommandType.STOP.getValue())) {
            return messageHandler.handleStopCommand(update, telegramSessionMap.get(update.getMessage().getChatId()));
        } else {
            if (!telegramSessionMap.get(update.getMessage().getChatId()).isActive())
                return new SendMessage(update.getMessage().getChatId().toString(), "Zəhmət olmasa /start edin və dili seçin." + "\n" +
                        "Please /start and select a language." + "\n" + "Пожалуйста, /start и выберите язык.");


            if (telegramSessionMap.get(update.getMessage().getChatId()).getLocale() == null) {
                List<Operation> operationList = telegramSessionMap.get(update.getMessage().getChatId()).getOperationList();
                telegramSessionMap.get(update.getMessage().getChatId()).setQuestion(operationList.get(0).getNextQuestion());
//                    telegramSessionMap.get(update.getMessage().getChatId()).setOperation(operationRepo.getOperationsByQuestion(telegramSessionMap.get(update.getMessage().getChatId()).getQuestion()).get(0));
//                    telegramSessionMap.get(update.getMessage().getChatId()).setOperationList(operationRepo.getOperationsByQuestion(telegramSessionMap.get(update.getMessage().getChatId()).getQuestion()));
               return setLocale(update, telegramSessionMap.get(update.getMessage().getChatId()));
            }
//            if (telegramSessionMap.get(update.getMessage().getChatId()).getLocale()!=null){
//                return handleMessage(update, telegramSessionMap.get(update.getMessage().getChatId()));
//            }
            if (telegramSessionMap.get(update.getMessage().getChatId()).getLocale() != null && telegramSessionMap.get(update.getMessage().getChatId()).getQuestion() != null) {


                System.out.println("current question  " + telegramSessionMap.get(update.getMessage().getChatId()).getQuestion().getQuestion_az());

                List<Operation> operationList = operationRepo.getOperationsByQuestion(telegramSessionMap.get(update.getMessage().getChatId()).getQuestion());
                if (operationList.get(0).getType().equals(OperationType.BUTTON)) {
                    int counter = 0;
                    for (int i = 0; i < operationList.size(); i++) {
                        System.out.println(operationList.get(i).getText_az());
                        System.out.println(operationList.get(i).getText_en());
                        System.out.println(operationList.get(i).getText_ru());
                        System.out.println(update.getMessage().getText());
                        System.out.println(telegramSessionMap.get(update.getMessage().getChatId()).getLocale());
                        System.out.println(operationRepo.findFirstOperation().getText_az());

                        if (telegramSessionMap.get(update.getMessage().getChatId()).getLocale().equals(operationRepo.findFirstOperation().getText_az())
                        && update.getMessage().getText().equals(operationList.get(i).getText_az())){
                            System.out.println("in loop az");
                            counter++;
                            telegramSessionMap.get(update.getMessage().getChatId()).setQuestion(operationList.get(i).getNextQuestion());
                            break;
                        }if (telegramSessionMap.get(update.getMessage().getChatId()).getLocale().equals(operationRepo.findFirstOperation().getText_en())
                                && update.getMessage().getText().equals(operationList.get(i).getText_en())){
                            System.out.println("in loop en");
                            counter++;
                            telegramSessionMap.get(update.getMessage().getChatId()).setQuestion(operationList.get(i).getNextQuestion());
                            break;
                        }if (telegramSessionMap.get(update.getMessage().getChatId()).getLocale().equals(operationRepo.findFirstOperation().getText_ru())
                                && update.getMessage().getText().equals(operationList.get(i).getText_ru())){
                            System.out.println("in loop ru");
                            counter++;
                            telegramSessionMap.get(update.getMessage().getChatId()).setQuestion(operationList.get(i).getNextQuestion());
                            break;
                        }
                    }
                    if (counter == 0) {
                        if(telegramSessionMap.get(update.getMessage().getChatId()).getLocale().equals(operationRepo.findFirstOperation().getText_az())){
                            return new SendMessage(update.getMessage().getChatId(), "Cavabı doğru daxil edin.");
                        }else if(telegramSessionMap.get(update.getMessage().getChatId()).getLocale().equals(operationRepo.findFirstOperation().getText_en())){
                            return new SendMessage(update.getMessage().getChatId(), "Enter the correct answer.");
                        }else {
                            return new SendMessage(update.getMessage().getChatId(), "Введите правильный ответ.");
                        }
                    }else {
                        return handleMessage(update, telegramSessionMap.get(update.getMessage().getChatId()));
                    }
                } else if (operationList.get(0).getType().equals(OperationType.FREETEXT)) {
                    System.out.println("free in facade");
                    if (operationRepo.getOperationsByQuestion(telegramSessionMap.get(update.getMessage().getChatId()).getQuestion()).get(0).getNextQuestion()!=null){
                        telegramSessionMap.get(update.getMessage().getChatId()).setQuestion(operationList.get(0).getNextQuestion());
                    }

                }


                return handleMessage(update, telegramSessionMap.get(update.getMessage().getChatId()));
            }


            return new SendMessage(update.getMessage().getChatId(), "Try again , lang");
        }

    }


    public SendMessage setLocale(Update update, TelegramSession telegramSession) {
//        telegramSession.setQuestion(operationRepo.findFirstOperation().getNextQuestion());
//        telegramSession.setOperation(operationRepo.findFirstOperation());
        if (update.getMessage().getText().equals(operationRepo.findFirstOperation().getText_az())) {
            telegramSession.setChatId(update.getMessage().getFrom().getId());
            System.out.println("Az lang set");
            telegramSession.setLocale(update.getMessage().getText());
            telegramSession.getQuestion_answer_map().put(operationRepo.findFirstOperation().getQuestion().getKey(),
                    operationRepo.findFirstOperation().getText_az());
            return handleMessage(update, telegramSession);
        } else if (update.getMessage().getText().equals(operationRepo.findFirstOperation().getText_en())) {
            telegramSession.setChatId(update.getMessage().getFrom().getId());
            System.out.println("En lang set");
            telegramSession.setLocale(update.getMessage().getText());
            telegramSession.getQuestion_answer_map().put(operationRepo.findFirstOperation().getQuestion().getKey(),
                    operationRepo.findFirstOperation().getText_en());
            return handleMessage(update, telegramSession);
        } else if (update.getMessage().getText().equals(operationRepo.findFirstOperation().getText_ru())) {
            telegramSession.setChatId(update.getMessage().getFrom().getId());
            System.out.println("Ru lang set");
            telegramSession.setLocale(update.getMessage().getText());
            telegramSession.getQuestion_answer_map().put(operationRepo.findFirstOperation().getQuestion().getKey(),
                    operationRepo.findFirstOperation().getText_ru());
            return handleMessage(update, telegramSession);
        }
        return new SendMessage(update.getMessage().getChatId().toString(), "Zəhmət olmasa dil seçin." + "\n" +
                "Please, select a language." + "\n" + "Пожалуйста, выберите язык.");
    }

    public SendMessage handleMessage(Update update, TelegramSession telegramSession) {
        SendMessage sendMessage = new SendMessage();

        if (!telegramSession.isActive())
            return new SendMessage(update.getMessage().getChatId().toString(), "Zəhmət olmasa /start edin və dili seçin." + "\n" +
                    "Please /start and select a language." + "\n" + "Пожалуйста, /start и выберите язык.");

        if (operationRepo.getOperationsByQuestion(telegramSession.getQuestion()).get(0).getNextQuestion() == null) {
            if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_az())) {
                return sendMessage.setChatId(update.getMessage().getChatId().toString()).setText("Anket başa çatdı.");
            } else if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_en())) {
                return sendMessage.setChatId(update.getMessage().getChatId().toString()).setText("The survey is over.");
            } else if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_ru())) {
                return sendMessage.setChatId(update.getMessage().getChatId().toString()).setText("Опрос окончен.");
            }
        }


        if (telegramSession.getOperationList().get(0).getNextQuestion() != null) {

            System.out.println("AAAAAAAAAAAAAAAAAAA");


            if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_az())) {

                System.out.println(telegramSession.getQuestion().getQuestion_az());

                telegramSession.getQuestion_answer_map().put(telegramSession.getQuestion().getKey(), update.getMessage().getText());

//                operationRepo.getOperationsByQuestion(telegramSession.getQuestion());

                if (operationRepo.getOperationsByQuestion(telegramSession.getQuestion()).size()>1) {
                    sendMessage.setReplyMarkup(keyboardService.getKeyboardButtons(telegramSession.getQuestion(), telegramSession));
                }

                return sendMessage.setChatId(update.getMessage().getChatId().toString())
                        .setText(telegramSession.getQuestion().getQuestion_az());

            } else if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_en())) {
                telegramSession.getQuestion_answer_map().put(telegramSession.getQuestion().getKey(), update.getMessage().getText());

                if (operationRepo.getOperationsByQuestion(telegramSession.getQuestion()).size()>1) {
                    System.out.println("button");
                    sendMessage.setReplyMarkup(keyboardService.getKeyboardButtons(telegramSession.getQuestion(), telegramSession));
                }

                return sendMessage.setChatId(update.getMessage().getChatId().toString())
                        .setText(telegramSession.getQuestion().getQuestion_en());

            } else if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_ru())) {

                telegramSession.getQuestion_answer_map().put(telegramSession.getQuestion().getKey(), update.getMessage().getText());

                if (operationRepo.getOperationsByQuestion(telegramSession.getQuestion()).size()>1) {
                    System.out.println("button");
                    sendMessage.setReplyMarkup(keyboardService.getKeyboardButtons(telegramSession.getQuestion(), telegramSession));
                }

                return sendMessage.setChatId(update.getMessage().getChatId().toString())
                        .setText(telegramSession.getQuestion().getQuestion_ru());
            }
        }


        return sendMessage.setChatId(update.getMessage().getChatId().toString())
                .setText("Anket basha chatdi");


    }


}
