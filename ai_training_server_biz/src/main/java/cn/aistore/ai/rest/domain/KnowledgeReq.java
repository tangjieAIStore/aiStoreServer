package cn.aistore.ai.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeReq extends BaseReq {
    @NotNull(message = "模型id不能为空")
    private Integer modelId;
    @NotNull(message = "文件url不能为空")
    private String fileUrl;
//    @NotNull(message = "文件名不能为空")
//    private Integer wordCount;
    @NotNull(message = "文件名不能为空")
    private String fileStatus;
}