package cn.aistore.ai.rest.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ModelResponse {
    private Integer id;
    private Integer userId;
    private Double frequencyPenalty;
    private Integer maintainContext;
    private Long maxResponses;
    private String model;
    private String name;
    private String outputFormat;
    private Double presencePenalty;
    private String role;
    private Double temperature;
    private Double topP;
    private Timestamp updateTime;
    private Long tenantId;
}