package code.az.buytourproject.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@ToString
public class AcceptQueueDTO {
    private Long agentId;
    private String uuid;
    private String info;
}
