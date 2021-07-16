package code.az.buytourproject.models;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TelegramSession {
    int chatId;
    UUID sessionId = UUID.randomUUID();
    String locale;
    Map<String, String> question_answer_map = new HashMap<>();
    Operation operation;
    List<Operation> operationList;
    Question question;
    boolean isActive;
}

