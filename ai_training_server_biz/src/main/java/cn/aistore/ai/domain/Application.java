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
@Table(name = "application")
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    @Builder.Default
    private Integer userId = 0;

    @Column(name = "model_id")
    private Integer modelId;

    @Column(name = "application_icon")
    private String applicationIcon;

    @Column(name = "application_name")
    private String applicationName;

    @Column(name = "application_description")
    private String applicationDescription;

    @Column(name = "application_type")
    private String applicationType;

    @Column(name = "first_level_category_name")
    private String firstLevelCategoryName;

    @Column(name = "guiding")
    private String guiding;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "is_included")
    private Integer isIncluded;

    @Column(name = "second_level_category_name")
    private String secondLevelCategoryName;

    @Column(name = "training_model")
    private String trainingModel;

    @DataFactory(data = TimestampData.class)
    @Column(name = "update_time")
    private Timestamp updateTime;

    @Column(name = "tenant_id")
    @Builder.Default
    private Long tenantId=0L;

    @Column(name = "deleted")
    private Integer deleted;
}
