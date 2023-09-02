package cn.aistore.ai.rest.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationReq extends BaseReq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @NotNull(message = "id不能为空")
    @Column(name = "id")
    private Integer id;

    @Column(name = "model_id")
    @NotNull(message = "模型id不能为空")
    private Integer modelId;

    @Column(name = "application_icon")
    @NotNull(message = "应用图标不能为空")
    private String applicationIcon;

    @Column(name = "application_name")
    @NotNull(message = "应用名称不能为空")
    private String applicationName;

    @Column(name = "application_description")
    @NotNull(message = "应用描述不能为空")
    private String applicationDescription;

    @Column(name = "application_type")
    @NotNull(message = "应用类型不能为空")
    private String applicationType;

    @Column(name = "first_level_category_name")
    @NotNull(message = "一级分类名称不能为空")
    private String firstLevelCategoryName;

    @Column(name = "guiding")
    @Builder.Default
    private String guiding="";

    @Column(name = "img_url")
    @Builder.Default
    private String imgUrl="";

    @Column(name = "is_included")
    @Range(min = 0, max = 1, message = "是否收录只能为(1收录；0不收录；)")
    private Integer isIncluded;

    @Column(name = "second_level_category_name")
    @NotNull(message = "二级分类名称不能为空")
    private String secondLevelCategoryName;

    @Column(name = "training_model")
    @NotNull(message = "训练模型不能为空")
    private String trainingModel;

    @Column(name = "update_time")
    private Timestamp updateTime;

    @Column(name = "deleted")
    @Builder.Default
    private Integer deleted = 0;
}
