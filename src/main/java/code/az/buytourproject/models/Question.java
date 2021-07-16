package code.az.buytourproject.models;

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
@Table(name = "questions")
public class Question {

    @Id
    private Long id;
    private String question_az;
    private String question_en;
    private String question_ru;

    private Boolean isFirst;
    private String key;
}
