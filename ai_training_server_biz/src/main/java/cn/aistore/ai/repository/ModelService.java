package cn.aistore.ai.repository;

import cn.aistore.ai.common.CommonUtil;
import cn.aistore.ai.domain.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ModelService {
    @Autowired
    private ModelRepository modelRepository;

    public Model getById(Integer id) {
        return modelRepository.findById(id).orElse(null);
    }

    public List<Model> list() {
        return modelRepository.findAll();
    }

    public Page<Model> listPage(Integer page, Integer size, Integer userId, Long tenantId) {
        Pageable pageable = Pageable.ofSize(size).withPage(CommonUtil.getRealPageNo(page));
        Page<Model> modelPage = modelRepository.findModelPage(userId, tenantId, 0, pageable);
        return modelPage;
    }

    public Model add(Model model) {
        return modelRepository.save(model);
    }

    public Model update(Integer id, Model model) {
        Model oldModel = modelRepository.findById(id).orElse(null);
        if (oldModel == null) {
            return null;
        }
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
        return modelRepository.save(oldModel);
    }

    public Boolean delete(Integer id) {
        try {
            Optional<Model> optionalModel = modelRepository.findById(id);
            if (optionalModel.isPresent()) {
                Model model = optionalModel.get();
                model.setDeleted(CommonUtil.getCurrentTime());
                modelRepository.save(model);
            }
            return true;
        } catch (Exception e) {
            log.info("删除模型失败: {}", e.getMessage());
        }
        return false;
    }
}