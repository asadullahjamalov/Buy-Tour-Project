package code.az.buytourproject.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.awt.image.BufferedImage;
import java.io.File;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@ToString
public class OfferQueueDTO {
    private Long agentId;
    private String uuid;
    private byte[] image;
}
