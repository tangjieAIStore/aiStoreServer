package cn.aistore.ai.repository;

import cn.aistore.ai.common.CommonUtil;
import cn.aistore.ai.domain.Application;
import cn.aistore.ai.domain.TokenCount;
import cn.aistore.ai.domain.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenCountService {
    @Autowired
    private TokenCountRepository tokenCountRepository;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ApplicationService applicationService;

    public TokenCount save(TokenCount tokenCount) {
        Model model = modelService.getById(tokenCount.getModelId());
        if (tokenCount.getModelId()!=0 && model == null) {
            throw new RuntimeException("modelId不存在");
        }
        Application application = applicationService.getApplicationById(tokenCount.getApplicationId());
        if (tokenCount.getApplicationId()!=0 && application == null) {
            throw new RuntimeException("applicationId不存在");
        }
        return tokenCountRepository.save(tokenCount);
    }

    public Boolean deleteById(Integer id) {
        Optional<TokenCount> optional = tokenCountRepository.findById(id);
        if (optional.isPresent()) {
            TokenCount tokenCount = optional.get();
            tokenCount.setDeleted(CommonUtil.getCurrentTime());
            tokenCountRepository.save(tokenCount);
            return true;
        }
        return false;
    }

    public TokenCount findById(Integer id) {
        return tokenCountRepository.findById(id).orElse(null);
    }

    public List<TokenCount> findAll() {
        return tokenCountRepository.findAll();
    }

}

