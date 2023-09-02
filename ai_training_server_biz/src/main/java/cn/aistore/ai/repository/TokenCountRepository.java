package cn.aistore.ai.repository;

import cn.aistore.ai.domain.TokenCount;
import cn.aistore.ai.domain.TokenSumProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;

public interface TokenCountRepository extends JpaRepository<TokenCount, Integer> {
    @Query("select t from TokenCount t where t.applicationId in ?1")
    List<TokenCount> findByApplicationIdIn(@NonNull Collection<Integer> applicationIds);

    @Query(value = "select COALESCE(sum(t.token_count), 0) as sum, count(t.user_id) as count, count(t.id) as interactionsCount " +
            "from token_count t where (t.application_id in ?1) or (t.user_id=?2 and t.tenant_id=?3 and t.application_id=0 and t.model_id=0)",
            nativeQuery = true)
    TokenSumProjection getTokenSumRes(@NonNull Collection<Integer> applicationIds, @NonNull Integer userId, @NonNull Long tenantId);

}