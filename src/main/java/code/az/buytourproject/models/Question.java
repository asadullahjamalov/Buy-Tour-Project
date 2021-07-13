package code.az.buytourproject.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
public class Question {

    @Id
    private Long id;
    private Long nextQuestionId;
    private String context;
    private Boolean isFirst;
    @ManyToOne
    @JoinColumn(name = "languageId")
    private Language language;
    @OneToMany(mappedBy = "question")
    private List<Operation> operations;
}
