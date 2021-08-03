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
@Table(name = "questions")
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String question_az;
    String question_en;
    String question_ru;
    Boolean isFirst;
    String key;
    String regex;
}
