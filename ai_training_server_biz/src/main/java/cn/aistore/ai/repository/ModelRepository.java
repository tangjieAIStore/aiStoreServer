package cn.aistore.ai.repository;

import cn.aistore.ai.domain.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface ModelRepository extends JpaRepository<Model, Integer> {

    @Query("select k from Model k where k.userId = ?1 and k.tenantId = ?2 and k.deleted = ?3")
    Page<Model> findModelPage(@NonNull Integer userId, @NonNull Long tenantId, @NonNull Integer deleted, Pageable pageable);
}