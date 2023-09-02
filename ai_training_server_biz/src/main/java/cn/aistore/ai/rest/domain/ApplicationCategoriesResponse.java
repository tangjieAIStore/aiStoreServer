package cn.aistore.ai.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationCategoriesResponse {
    private String firstLevelCategoryName;
    private String secondLevelCategoryName;
}