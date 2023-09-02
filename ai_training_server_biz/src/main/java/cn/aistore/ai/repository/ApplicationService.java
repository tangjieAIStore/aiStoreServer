package cn.aistore.ai.repository;

import cn.aistore.ai.rest.domain.MyHomeRes;
import cn.aistore.ai.common.CommonUtil;
import cn.aistore.ai.common.JpaCondition;
import cn.aistore.ai.domain.Application;
import cn.aistore.ai.domain.TokenSumProjection;
import cn.aistore.ai.rest.domain.ApplicationCategoriesResponse;
import cn.aistore.ai.rest.domain.ApplicationQueryReq;
import cn.aistore.ai.rest.domain.TokenSumRes;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tangking
 */
@Service
@Slf4j
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private TokenCountRepository tokenCountRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    /**
     * 个人自己的
     * @param request
     * @return
     */
    public Page<Application> getAllApplicationPage(ApplicationQueryReq request) {
        String firstLevelCategory = request.getFirstLevelCategoryName();
        String secondLevelCategory = request.getSecondLevelCategoryName();
        firstLevelCategory = StringUtils.hasText(firstLevelCategory) ? CommonUtil.getLikeStr(firstLevelCategory) : CommonUtil.getLikeStr("");
        secondLevelCategory = StringUtils.hasText(secondLevelCategory) ? CommonUtil.getLikeStr(secondLevelCategory) : CommonUtil.getLikeStr("");
        if (request.getPageNo() == null) {
            request.setPageNo(0);
        }
        if (request.getPageSize() == null) {
            request.setPageSize(999999);
        }

//        return applicationRepository.findByFirstLevelCategoryNameLikeAndSecondLevelCategoryNameLike(
//                firstLevelCategory, secondLevelCategory, Pageable.ofSize(request.getPageSize()).withPage(request.getPageNo()));
        return applicationRepository.findByAllDeleted(request.getUserId(), request.getTenantId(), Pageable.ofSize(request.getPageSize()).withPage(request.getPageNo()));
    }

    /**
     * 给所有用户看的
     * @param request
     * @return
     */
    public Page<Application> getAllApplicationPageByUser(ApplicationQueryReq request) {
        String firstLevelCategory = request.getFirstLevelCategoryName();
        String secondLevelCategory = request.getSecondLevelCategoryName();
        if (request.getPageNo() == null) {
            request.setPageNo(0);
        }
        if (request.getPageSize() == null) {
            request.setPageSize(999999);
        }

        JpaCondition<Application> jpaCondition = JpaCondition.<Application>builder().build();
        if (StringUtils.hasText(firstLevelCategory)) {
            jpaCondition = jpaCondition.addEqual("firstLevelCategoryName");
        }
        else {
            request.setFirstLevelCategoryName(null);
        }

        if (StringUtils.hasText(secondLevelCategory)) {
            jpaCondition = jpaCondition.addEqual("secondLevelCategoryName");
        }
        else {
            request.setSecondLevelCategoryName(null);
        }
        jpaCondition.addEqual("deleted");
        jpaCondition.addEqual("isIncluded");
        Application application = this.modelMapper.map(request, Application.class);
        application.setDeleted(0);
        application.setIsIncluded(1);
        Page<Application> page = this.applicationRepository.findAll(jpaCondition.buildUser(application), Pageable.ofSize(request.getPageSize()).withPage(request.getPageNo()));
//        if (jpaCondition.isHaveCondition()) {
//            Application application = this.modelMapper.map(request, Application.class);
//            page = this.applicationRepository.findAll(jpaCondition.build(application), Pageable.ofSize(request.getPageSize()).withPage(request.getPageNo()));
//        }
//        else {
//            page = this.applicationRepository.findAll(Pageable.ofSize(request.getPageSize()).withPage(request.getPageNo()));
//        }

        return page;
//        return applicationRepository.findByUserIdEqualsAndTenantIdEqualsAndFirstLevelCategoryNameLikeAndSecondLevelCategoryNameLike(
//                request.getUserId(), request.getTenantId(),
//                firstLevelCategory, secondLevelCategory, Pageable.ofSize(request.getPageSize()).withPage(request.getPageNo()));
    }

    public Application getApplicationById(Integer id) {
        return applicationRepository.findById(id).orElse(null);
    }

    public Application addApplication(Application application) {
        return applicationRepository.save(application);
    }

    public Application updateApplication(Integer id, Application application) {
        Application existingApplication = applicationRepository.findById(id).orElse(null);
        if (existingApplication == null) {
            return null;
        }
        existingApplication.setUserId(application.getUserId());
        existingApplication.setModelId(application.getModelId());
        existingApplication.setApplicationIcon(application.getApplicationIcon());
        existingApplication.setApplicationName(application.getApplicationName());
        existingApplication.setApplicationDescription(application.getApplicationDescription());
        existingApplication.setApplicationType(application.getApplicationType());
        existingApplication.setFirstLevelCategoryName(application.getFirstLevelCategoryName());
        existingApplication.setGuiding(application.getGuiding());
        existingApplication.setImgUrl(application.getImgUrl());
        existingApplication.setIsIncluded(application.getIsIncluded());
        existingApplication.setSecondLevelCategoryName(application.getSecondLevelCategoryName());
        existingApplication.setTrainingModel(application.getTrainingModel());
        existingApplication.setUpdateTime(application.getUpdateTime());
        existingApplication.setTenantId(application.getTenantId());
        return applicationRepository.save(existingApplication);
    }

    public Boolean deleteApplication(Integer id) {
        try {
            Application application = applicationRepository.findById(id).orElse(null);
            if (application != null) {
                Integer deletedTime = CommonUtil.getCurrentTime();
                application.setDeleted(deletedTime);
                applicationRepository.save(application);
            }
            return true;
        } catch (Exception e) {
            log.warn("delete application failed, id: {}", id, e);
            return false;
        }
    }

    public Page<ApplicationCategoriesResponse> getApplicationCategories(Integer pageNo, Integer pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(CommonUtil.getRealPageNo(pageNo));
        return applicationRepository.findApplicationCategories(pageable);
    }

    public MyHomeRes getMyHome(Integer userId, Long tenantId) {
        String userImg = "";
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("user_id", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("tenant_id", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<Application> example = Example.of(Application.builder().userId(userId).tenantId(tenantId).deleted(0).build(), exampleMatcher);
        List<Application> list =  applicationRepository.findAll(example);
        List<Integer> appIds = list.stream().map(item -> item.getId()).collect(Collectors.toList());

        List<MyHomeRes.MyApplicationsDTO> myApplications = list.stream().map(item -> {
            return MyHomeRes.MyApplicationsDTO.builder().applicationImage(item.getImgUrl())
                    .applicationDescription(item.getApplicationDescription())
                    .applicationName(item.getApplicationName())
                    .id(item.getId())
                    .userAvatar(userImg)
                    .rating(5)
                    .build();
        }).collect(Collectors.toList());
        TokenSumProjection tokenSumProjection = this.tokenCountRepository.getTokenSumRes(appIds, userId, tenantId);
        TokenSumRes tokenSumRes = TokenSumRes.builder().sum(tokenSumProjection.getSum())
                .interactionsCount(tokenSumProjection.getInteractionsCount())
                .count(tokenSumProjection.getCount()).build();
        MyHomeRes myHomeRes = MyHomeRes.builder().userAvatar("").userCount(tokenSumRes.getCount())
                .tokenCount(tokenSumRes.getSum())
                .interactionsCount(tokenSumRes.getInteractionsCount())
                .myApplications(myApplications)
                .build();
        return myHomeRes;
    }

}