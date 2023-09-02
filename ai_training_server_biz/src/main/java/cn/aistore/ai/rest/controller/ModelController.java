package cn.aistore.ai.rest.controller;

import cn.aistore.ai.common.CommonRes;
import cn.aistore.ai.common.CommonUtil;
import cn.aistore.ai.common.SystemThreadLocal;
import cn.aistore.ai.domain.Model;
import cn.aistore.ai.repository.ModelService;
import cn.aistore.ai.rest.domain.ModelReq;
import cn.aistore.ai.rest.domain.ModelResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/model")
public class ModelController {
    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ModelResponse getById(@PathVariable Integer id) {
        Model model = modelService.getById(id);
        if (model == null) {
            return null;
        }
        ModelResponse response = new ModelResponse();
        BeanUtils.copyProperties(model, response);
        return response;
    }

    @GetMapping("/list")
    public List<ModelResponse> list() {
        List<Model> models = modelService.list();
        List<ModelResponse> responses = new ArrayList<>();
        for (Model model : models) {
            ModelResponse response = new ModelResponse();
            BeanUtils.copyProperties(model, response);
            responses.add(response);
        }
        return responses;
    }

    @GetMapping("/page")
    public CommonRes<Page<Model>> listPage(@RequestParam("pageNo") @NotNull(message = "页码不能为空") Integer pageNo,
                                           @RequestParam("pageSize") @NotNull(message = "页数据大小不能为空") Integer pageSize) {
        Long tenantId = SystemThreadLocal.getTenantId();
        Integer userId = SystemThreadLocal.getUserId();
        Page<Model> page = modelService.listPage(CommonUtil.getRealPageNo(pageNo), pageSize, userId, tenantId);
        return CommonRes.success(page);
    }

    @PostMapping("/create")
    public CommonRes<Integer> add(@RequestBody @Valid ModelReq modelReq) {
        modelReq.setUserId(SystemThreadLocal.getUserId());
        modelReq.setTenantId(SystemThreadLocal.getTenantId());
        Model model = this.modelMapper.map(modelReq, Model.class);
        model = modelService.add(model);
        if (model == null || model.getId() == null || model.getId()<1) {
            return CommonRes.fail(model.getId());
        }
        return CommonRes.success(model.getId());
    }

    @PutMapping("/{id}")
    public ModelResponse update(@PathVariable Integer id, @RequestBody ModelReq modelReq) {
        Model oldModel = modelService.getById(id);
        if (oldModel == null) {
            return null;
        }
        Model model = this.modelMapper.map(modelReq, Model.class);
        oldModel.setUserId(model.getUserId());
        oldModel.setFrequencyPenalty(model.getFrequencyPenalty());
        oldModel.setMaintainContext(model.getMaintainContext());
        oldModel.setMaxResponses(model.getMaxResponses());
        oldModel.setModel(model.getModel());
        oldModel.setName(model.getName());
        oldModel.setOutputFormat(model.getOutputFormat());
        oldModel.setPresencePenalty(model.getPresencePenalty());
        oldModel.setRole(model.getRole());
        oldModel.setTemperature(model.getTemperature());
        oldModel.setTopP(model.getTopP());
        oldModel.setTenantId(model.getTenantId());
        oldModel.setStream(model.getStream());
        modelService.update(id, oldModel);
        ModelResponse response = new ModelResponse();
        BeanUtils.copyProperties(oldModel, response);
        return response;
    }

    @DeleteMapping("/delete")
    public CommonRes<Boolean> delete(@Param("id") @Validated @NotNull(message = "模型ID不能为空") Integer id) {
        Model model = modelService.getById(id);
        if (model == null || !model.getUserId().equals(SystemThreadLocal.getUserId()) || !model.getTenantId().equals(SystemThreadLocal.getTenantId())) {
            return CommonRes.fail(false);
        }
        Boolean res = modelService.delete(id);
        return res ? CommonRes.success(res) : CommonRes.fail(res);
    }
}