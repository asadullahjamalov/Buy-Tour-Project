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
    @Enumerated(EnumType.STRING)
    private OperationType type;
    private String text_az;
    private String text_en;
    private String text_ru;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_Id")
    private Question question;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "next_question_id")
    private Question nextQuestion;


}