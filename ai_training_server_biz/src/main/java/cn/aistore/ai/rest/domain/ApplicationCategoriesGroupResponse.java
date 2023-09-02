package cn.aistore.ai.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationCategoriesGroupResponse {
    private String firstLevelCategoryName;
    private List<String> secondLevelCategoryNames;
}