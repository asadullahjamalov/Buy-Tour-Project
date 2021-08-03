package code.az.buytourproject.models;

import code.az.buytourproject.enums.OperationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Entity
@Table(name = "operations")
public class Operation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    OperationType type;
    String text_az;
    String text_en;
    String text_ru;
    @ManyToOne
    @JoinColumn(name = "question_Id")
    Question question;
    @ManyToOne
    @JoinColumn(name = "next_question_id")
    Question nextQuestion;


}