package cn.aistore.ai.repository;

import cn.aistore.ai.rest.domain.ApplicationCategoriesResponse;
import cn.aistore.ai.domain.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    Page<Application> findByFirstLevelCategoryNameLikeAndSecondLevelCategoryNameLike(@NonNull String firstLevelCategoryName,
                                                                                 @NonNull String secondLevelCategoryName,
                                                                                 Pageable pageable);

    @Query(value="select * from application a where a.user_id=:userId and a.tenant_id=:tenantId and a.deleted=0", nativeQuery = true)
    Page<Application> findByAllDeleted(@Param("userId") Integer userId, @Param("tenantId") Long tennantId, Pageable pageable);

    Page<Application> findByUserIdEqualsAndTenantIdEqualsAndFirstLevelCategoryNameLikeAndSecondLevelCategoryNameLike(
            @NonNull Integer userId, @NonNull Long tenantId, @NonNull String firstLevelCategoryName,
            @NonNull String secondLevelCategoryName, Pageable pageable);

    @Query("select new cn.aistore.ai.rest.domain.ApplicationCategoriesResponse(a.firstLevelCategoryName, a.secondLevelCategoryName) "
            + " from Application a where a.deleted=0 group by a.firstLevelCategoryName, a.secondLevelCategoryName")
    Page<ApplicationCategoriesResponse> findApplicationCategories(Pageable pageable);
}