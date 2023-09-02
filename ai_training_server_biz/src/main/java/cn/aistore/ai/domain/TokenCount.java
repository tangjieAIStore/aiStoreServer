package cn.aistore.ai.domain;

import cn.aistore.ai.common.TimestampData;
import com.github.houbb.data.factory.api.annotation.DataFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@Entity
@Table(name = "token_count")
@AllArgsConstructor
@NoArgsConstructor
public class TokenCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    @Builder.Default
    private Integer userId = 0;

    @Column(name = "model_id")
//    @NotNull(message= "模型id不能为空")
    private Integer modelId;

    @Column(name = "application_id")
//    @NotNull(message= "应用id不能为空")
    private Integer applicationId;

    @Column(name = "token_count")
    private Integer tokenCount;

    @DataFactory(data = TimestampData.class)
    @Column(name = "update_time")
    private Timestamp updateTime;

    @Column(name = "tenant_id")
    @Builder.Default
    private Long tenantId = 0L;

    @Column(name = "deleted")
    private Integer deleted;

}