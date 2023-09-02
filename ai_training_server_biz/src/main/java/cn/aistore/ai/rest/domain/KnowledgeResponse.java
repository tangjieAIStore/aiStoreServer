package cn.aistore.ai.rest.domain;

import lombok.Data;

import java.util.Date;

@Data
public class KnowledgeResponse {
    private Integer id;
    private Integer userId;
    private Integer modelId;
    private String fileUrl;
    private String fileName;
    private String fileStatus;
    private Long wordCount;
    private Date updateTime;
}