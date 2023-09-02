package cn.aistore.ai.rest.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ApplicationResponse {
    private Integer id;
    private Integer userId;
    private Integer modelId;
    private String applicationIcon;
    private String applicationName;
    private String applicationDescription;
    private String applicationType;
    private String firstLevelCategoryName;
    private String guiding;
    private String imgUrl;
    private Integer isIncluded;
    private String secondLevelCategoryName;
    private String trainingModel;
    private Timestamp updateTime;
    private Long tenantId;
}