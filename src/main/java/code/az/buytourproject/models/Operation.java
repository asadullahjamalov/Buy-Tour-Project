package code.az.buytourproject.models;

import code.az.buytourproject.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operations")
public class Operation implements Serializable {
    @Id
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