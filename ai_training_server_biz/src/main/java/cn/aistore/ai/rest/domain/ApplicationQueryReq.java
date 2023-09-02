package cn.aistore.ai.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class ApplicationQueryReq extends PageReq{
    @NotNull(message = "一级分类不能为空")
    private String firstLevelCategoryName;
    @NotNull(message = "二级分类不能为空")
    private String secondLevelCategoryName;
}
