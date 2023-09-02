package cn.aistore.ai.repository;

import cn.aistore.ai.domain.Knowledge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface KnowledgeRepository extends JpaRepository<Knowledge, Integer> {

    Page<Knowledge> findByModelIdEquals(@NonNull Integer modelId, Pageable pageable);

    @Query("select k from Knowledge k where k.modelId = ?1 and k.deleted = ?2 and k.userId = ?3 and k.tenantId = ?4")
    Page<Knowledge> findByModelIdEqualsAndDeletedEquals(@NonNull Integer modelId, @NonNull Integer deleted,
                                                        @NonNull Integer userId, @NonNull Long tenantId, Pageable pageable);


}