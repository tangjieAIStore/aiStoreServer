package cn.aistore.ai.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class MyHomeRes {
    /** 使用的用户数量 */
    private Integer userCount;
    /** 互动次数 */
    private Integer interactionsCount;
    /** token数量 */
    private Integer tokenCount;
    /** 用户图片 */
    private String userAvatar;
    /** 我的应用列表 */
    private List<MyApplicationsDTO> myApplications;

    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class MyApplicationsDTO {
        private Integer id;
        private String applicationName;
        private String applicationDescription;
        private Integer rating;
        private String userAvatar;
        private String applicationImage;
    }
}
