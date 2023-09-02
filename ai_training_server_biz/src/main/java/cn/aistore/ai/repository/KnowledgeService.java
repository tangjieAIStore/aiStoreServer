package cn.aistore.ai.repository;

import cn.aistore.ai.common.CommonUtil;
import cn.aistore.ai.domain.Knowledge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class KnowledgeService {
    @Autowired
    private KnowledgeRepository knowledgeRepository;

    public Knowledge getKnowledgeById(Integer id) {
        return knowledgeRepository.findById(id).orElse(null);
    }

    public List<Knowledge> getAllKnowledge() {
        return knowledgeRepository.findAll();
    }

    public Page<Knowledge> getKnowledgePage(Integer page, Integer size, Integer modelId, Integer userId, Long tenantId) {
        return knowledgeRepository.findByModelIdEqualsAndDeletedEquals(modelId, 0, userId, tenantId,  Pageable.ofSize(size).withPage(CommonUtil.getRealPageNo(page)));
    }

    public Knowledge addKnowledge(Knowledge knowledge) {
        return knowledgeRepository.save(knowledge);
    }

    public Knowledge updateKnowledge(Integer id, Knowledge knowledge) {
        Knowledge existingKnowledge = knowledgeRepository.findById(id).orElse(null);
        if (existingKnowledge == null) {
            return null;
        }
        existingKnowledge.setUserId(knowledge.getUserId());
        existingKnowledge.setModelId(knowledge.getModelId());
        existingKnowledge.setFileUrl(knowledge.getFileUrl());
        existingKnowledge.setFileName(knowledge.getFileName());
        existingKnowledge.setFileStatus(knowledge.getFileStatus());
        existingKnowledge.setWordCount(knowledge.getWordCount());
        existingKnowledge.setUpdateTime(knowledge.getUpdateTime());
        return knowledgeRepository.save(existingKnowledge);
    }

    public Boolean deleteKnowledge(Integer id) {
        try {
            Optional<Knowledge> knowledgeOp = knowledgeRepository.findById(id);
            if (knowledgeOp.isPresent()) {
                Knowledge knowledge = knowledgeOp.get();
                knowledge.setDeleted(CommonUtil.getCurrentTime());
                knowledgeRepository.save(knowledge);
            }
            return true;
        } catch (Exception e) {
            log.info("deleteKnowledge error: {}", e.getMessage());
            return false;
        }
    }
}