package cn.aistore.ai.rest.controller;

import cn.aistore.ai.rest.domain.*;
import cn.aistore.ai.common.CommonRes;
import cn.aistore.ai.common.CommonUtil;
import cn.aistore.ai.common.IgnoreBaseReq;
import cn.aistore.ai.common.SystemThreadLocal;
import cn.aistore.ai.domain.Application;
import cn.aistore.ai.domain.Model;
import cn.aistore.ai.repository.ApplicationService;
import cn.aistore.ai.repository.ModelService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/get")
    public CommonRes<ApplicationResponse> getApplicationById(@Param("id") @NotNull(message = "id不能为空") Integer id) {
        Application existingApplication = applicationService.getApplicationById(id);

        if (existingApplication == null) {
            return CommonRes.fail(null);
        }
        Integer isInclude = existingApplication.getIsIncluded();
        if (isInclude==0 && (!existingApplication.getTenantId().equals(SystemThreadLocal.getTenantId()) ||
                !existingApplication.getUserId().equals(SystemThreadLocal.getUserId()))) {
            return CommonRes.fail(null);
        }

        ApplicationResponse applicationResponse = new ApplicationResponse();
        BeanUtils.copyProperties(existingApplication, applicationResponse);
        return CommonRes.success(applicationResponse);
    }

    @GetMapping("/model/get")
    public CommonRes<ModelResponse> getModelByAppId(@Param("id") @NotNull(message = "id不能为空") Integer id) {
        Application application = applicationService.getApplicationById(id);
        if (application == null/* || !application.getTenantId().equals(SystemThreadLocal.getTenantId()) ||
                !application.getUserId().equals(SystemThreadLocal.getUserId())*/) {
            return CommonRes.success(null);
        }
        Model model = this.modelService.getById(application.getModelId());
        if (model == null) {
            return CommonRes.success(null);
        }
        ModelResponse modelResponse = modelMapper.map(model, ModelResponse.class);
        return CommonRes.success(modelResponse);
    }

    @GetMapping("/all")
    public List<ApplicationResponse> getAllApplications() {
        List<Application> applications = applicationService.getAllApplications();
        List<ApplicationResponse> applicationResponses = new ArrayList<>();
        for (Application application : applications) {
            ApplicationResponse applicationResponse = new ApplicationResponse();
            BeanUtils.copyProperties(application, applicationResponse);
            applicationResponses.add(applicationResponse);
        }
        return applicationResponses;
    }

    @GetMapping("/page")
    @IgnoreBaseReq(true)
    public CommonRes<Page<Application>> getAllApplications(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                           @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                           @RequestParam(value = "firstLevelCategory", required = false) String firstLevelCategory,
                                                           @RequestParam(value = "secondLevelCategory", required = false) String secondLevelCategory) {
        ApplicationQueryReq applicationQueryReq = ApplicationQueryReq.builder().pageNo(CommonUtil.getRealPageNo(pageNo)).pageSize(pageSize)
                .firstLevelCategoryName(firstLevelCategory).secondLevelCategoryName(secondLevelCategory).build();
        applicationQueryReq.setUserId(SystemThreadLocal.getUserId());
        applicationQueryReq.setTenantId(SystemThreadLocal.getTenantId());
        Page<Application> applications = applicationService.getAllApplicationPage(applicationQueryReq);
        return CommonRes.success(applications);
    }

    @GetMapping("/user/page")
    public CommonRes<Page<Application>> getAllApplicationsByUser(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                           @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                           @RequestParam(value = "firstLevelCategoryName", required = false) String firstLevelCategory,
                                                           @RequestParam(value = "secondLevelCategoryName", required = false) String secondLevelCategory) {
        ApplicationQueryReq applicationQueryReq = ApplicationQueryReq.builder().pageNo(CommonUtil.getRealPageNo(pageNo)).pageSize(pageSize)
                .firstLevelCategoryName(firstLevelCategory).secondLevelCategoryName(secondLevelCategory).build();
//        applicationQueryReq.setUserId(SystemThreadLocal.getUserId());
//        applicationQueryReq.setTenantId(SystemThreadLocal.getTenantId());
        Page<Application> applications = applicationService.getAllApplicationPageByUser(applicationQueryReq);
        return CommonRes.success(applications);
    }

    @GetMapping("/categories/page")
    public CommonRes<List<ApplicationCategoriesGroupResponse>> getAllApplicationCategories(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                                                           @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<ApplicationCategoriesResponse> applicationCategories = applicationService.getApplicationCategories(CommonUtil.getRealPageNo(pageNo), pageSize);
        Map<String, List<String>> map = applicationCategories.stream().collect(Collectors
                .groupingBy(ApplicationCategoriesResponse::getFirstLevelCategoryName,
                Collectors.mapping(ApplicationCategoriesResponse::getSecondLevelCategoryName, Collectors.toList())));
        List<ApplicationCategoriesGroupResponse> list = new ArrayList<>();
        map.forEach((k, v) -> {
            list.add(ApplicationCategoriesGroupResponse.builder().firstLevelCategoryName(k).secondLevelCategoryNames(v).build());
        });
        return CommonRes.success(list);
    }

    @PostMapping("/create")
    public CommonRes<Integer> addApplication(@RequestBody @Valid ApplicationReq applicationReq) {
        Application application =  modelMapper.map(applicationReq, Application.class);
        application.setTenantId(SystemThreadLocal.getTenantId());
        application.setUserId(SystemThreadLocal.getUserId());
        Application savedApplication = applicationService.addApplication(application);
        return CommonRes.success(savedApplication.getId());
    }

    @PutMapping("/{id}")
    public CommonRes<String> updateApplication(@PathVariable @Valid @NotNull(message = "id不能为空") Integer id, @RequestBody @NotNull(message = "应用不能为空") ApplicationReq applicationReq) {
        Application existingApplication = applicationService.getApplicationById(id);
        Integer isInclude = existingApplication.getIsIncluded();
        if (existingApplication == null || !existingApplication.getTenantId().equals(SystemThreadLocal.getTenantId()) ||
                !existingApplication.getUserId().equals(SystemThreadLocal.getUserId())) {
            return CommonRes.fail("不存在该应用");
        }
        existingApplication.setUserId(SystemThreadLocal.getUserId());
        existingApplication.setModelId(applicationReq.getModelId());
        existingApplication.setApplicationIcon(applicationReq.getApplicationIcon());
        existingApplication.setApplicationName(applicationReq.getApplicationName());
        existingApplication.setApplicationDescription(applicationReq.getApplicationDescription());
        existingApplication.setApplicationType(applicationReq.getApplicationType());
        existingApplication.setFirstLevelCategoryName(applicationReq.getFirstLevelCategoryName());
        existingApplication.setGuiding(applicationReq.getGuiding());
        existingApplication.setImgUrl(applicationReq.getImgUrl());
        existingApplication.setIsIncluded(applicationReq.getIsIncluded());
        existingApplication.setSecondLevelCategoryName(applicationReq.getSecondLevelCategoryName());
        existingApplication.setTrainingModel(applicationReq.getTrainingModel());
        existingApplication.setUpdateTime(applicationReq.getUpdateTime());
        existingApplication.setTenantId(SystemThreadLocal.getTenantId());
        Application savedApplication = applicationService.updateApplication(id, existingApplication);
        if (savedApplication == null) {
            return CommonRes.fail();
        }
        return CommonRes.success("更新成功");
    }

    @DeleteMapping("/delete")
    public CommonRes<Boolean> deleteApplication(@Param("id") @NotNull(message = "应用ID不能为空") Integer id) {
        Application existingApplication = applicationService.getApplicationById(id);
        if (existingApplication == null || !existingApplication.getTenantId().equals(SystemThreadLocal.getTenantId()) ||
                !existingApplication.getUserId().equals(SystemThreadLocal.getUserId())) {
            return CommonRes.fail(false);
        }
        Boolean res = applicationService.deleteApplication(id);
        return CommonRes.success(res);
    }

    @GetMapping("/my/page")
    public CommonRes<MyHomeRes> getMyHome() {
        Integer userId = SystemThreadLocal.getUserId();
        Long tenantId = SystemThreadLocal.getTenantId();
        MyHomeRes myHomeRes = applicationService.getMyHome(userId, tenantId);
        return CommonRes.success(myHomeRes);
    }
}