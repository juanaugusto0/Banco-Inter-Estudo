package study.co.inter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDto {
    private Long sendingClientId;
    private Long receivingClientId;
    private double transferValue;
}
