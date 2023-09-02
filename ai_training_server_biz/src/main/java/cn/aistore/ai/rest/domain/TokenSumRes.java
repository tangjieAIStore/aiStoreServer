package cn.aistore.ai.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenSumRes {
    private Integer sum;
    private Integer count;
    private Integer interactionsCount;
}
