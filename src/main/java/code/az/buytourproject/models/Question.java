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
@Table(name = "questions")
public class Question implements Serializable {
    @Id
    Long id;
    String question_az;
    String question_en;
    String question_ru;
    Boolean isFirst;
    String key;
    String regex;
}
