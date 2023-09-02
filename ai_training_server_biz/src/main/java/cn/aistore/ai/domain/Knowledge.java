package cn.aistore.ai.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "knowledge")
@NoArgsConstructor
@AllArgsConstructor
public class Knowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    @Builder.Default
    private Integer userId=0;

    @Column(name = "model_id")
//    @NotNull(message="模型id不能为空")
    private Integer modelId;

    @Column(name = "file_url")
//    @NotNull(message="文件url不能为空")
    private String fileUrl;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_status")
    private String fileStatus;

    @Column(name = "word_count")
    private Integer wordCount;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "tenant_id")
    @Builder.Default
    private Long tenantId=0L;

    @Column(name = "deleted")
    private Integer deleted;
}