package cn.aistore.ai.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TokenCountReq extends BaseReq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "model_id")
    @NotNull(message= "模型id不能为空")
    private Integer modelId;

    @Column(name = "application_id")
    @NotNull(message= "应用id不能为空")
    private Integer applicationId;

    @Column(name = "token_count")
    private Integer tokenCount;

    @Column(name = "update_time")
    private Timestamp updateTime;

    @Column(name = "deleted")
    private Integer deleted=0;

}