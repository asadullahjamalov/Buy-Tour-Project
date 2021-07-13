package code.az.buytourproject.models;

import code.az.buytourproject.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operations")
public class Operation {
    @Id
    private Long id;
    private OperationType type;
    private String text;
    @ManyToOne
    @JoinColumn(name = "languageId")
    private Language language;
    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;
    @ManyToOne
    @JoinColumn(name = "nextQuestionId")
    private Question nextQuestion;


}