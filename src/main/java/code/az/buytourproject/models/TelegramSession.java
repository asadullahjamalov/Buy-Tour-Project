package code.az.buytourproject.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level= AccessLevel.PRIVATE)
@Entity
@Table(name = "telegram_sessions")
public class TelegramSession implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    long chatId;
    String uuid = UUID.randomUUID().toString();
    String locale;

    @ElementCollection
    Map<String, String> question_answer_map = new HashMap<>();

    @Transient
    List<Operation> operationList;

    @OneToOne
    @JoinColumn(name = "question_id")
    Question question;

    boolean isActive;
    LocalDateTime createdDate = LocalDateTime.now();
}
