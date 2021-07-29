package code.az.buytourproject.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level= AccessLevel.PRIVATE)
@ToString
public class RequestQueueDTO {

    private String uuid;
    private String jsonAnswers;

}
