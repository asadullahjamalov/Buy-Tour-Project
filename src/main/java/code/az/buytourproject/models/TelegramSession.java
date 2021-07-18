package code.az.buytourproject.models;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TelegramSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int chatId;
    String locale;
    Map<String, String> question_answer_map = new HashMap<>();
    List<Operation> operationList;
    Question question;
    boolean isActive;
}

