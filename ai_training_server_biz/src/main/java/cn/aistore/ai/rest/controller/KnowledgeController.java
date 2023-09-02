package cn.aistore.ai.rest.controller;

import cn.aistore.ai.common.CommonRes;
import cn.aistore.ai.domain.Knowledge;
import cn.aistore.ai.rest.domain.KnowledgeReq;
import cn.aistore.ai.rest.domain.KnowledgeResponse;
import cn.aistore.ai.common.CharacterCounter;
import cn.aistore.ai.common.CommonUtil;
import cn.aistore.ai.common.SystemThreadLocal;
import cn.aistore.ai.repository.KnowledgeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/model/knowledge")
@Slf4j
public class KnowledgeController {
    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public KnowledgeResponse getKnowledgeById(@PathVariable Integer id) {
        Knowledge knowledge = knowledgeService.getKnowledgeById(id);
        if (knowledge == null || !knowledge.getTenantId().equals(SystemThreadLocal.getTenantId()) ||
                !knowledge.getUserId().equals(SystemThreadLocal.getUserId())) {
            return null;
        }
        KnowledgeResponse response = new KnowledgeResponse();
        BeanUtils.copyProperties(knowledge, response);
        return response;
    }

    @GetMapping("/all")
    public List<KnowledgeResponse> getAllKnowledge() {
        List<Knowledge> knowledgeList = knowledgeService.getAllKnowledge();
        List<KnowledgeResponse> responseList = new ArrayList<>();
        for (Knowledge knowledge : knowledgeList) {
            KnowledgeResponse response = new KnowledgeResponse();
            BeanUtils.copyProperties(knowledge, response);
            responseList.add(response);
        }
        return responseList;
    }

    @GetMapping("/page")
    public CommonRes<Page<KnowledgeResponse>> getKnowledgePage(@Param("pageNo") @NotNull(message = "分页序号不能为空") Integer pageNo,
                                                               @Param("pageSize") @NotNull(message = "分页大小不能为空") Integer pageSize,
                                                               @Param("modelId") @NotNull(message = "模型id不能为空") Integer modelId) {
        Integer userId = SystemThreadLocal.getUserId();
        Long tenantId = SystemThreadLocal.getTenantId();
        Page<Knowledge> knowledgeList = knowledgeService.getKnowledgePage(pageNo, pageSize, modelId, userId, tenantId);
        return CommonRes.success(CommonUtil.copyPage(knowledgeList, modelMapper, KnowledgeResponse.class));
    }

    @PostMapping("/create")
    public CommonRes<Integer> addKnowledge(@RequestBody KnowledgeReq knowledge) {
        Integer userId = SystemThreadLocal.getUserId();
        Long tenantId = SystemThreadLocal.getTenantId();
        String url = knowledge.getFileUrl();
        String fileName = url.substring(url.lastIndexOf("/")+1);
        Integer count = 0;
        try {
            count = CharacterCounter.countCharacters(url);
        }
        catch (IOException e) {
            log.info("read file error: {}", e.getMessage());
            throw new RuntimeException("文件读取失败");
        }
        Knowledge newKnowledge = Knowledge.builder().tenantId(tenantId).fileName(fileName).userId(userId)
                .fileStatus(knowledge.getFileStatus()).modelId(knowledge.getModelId()).fileUrl(knowledge.getFileUrl())
                .deleted(0).tenantId(tenantId)
                .wordCount(count).build();
        Knowledge savedKnowledge = knowledgeService.addKnowledge(newKnowledge);
        return CommonRes.success(savedKnowledge.getId());
    }

    @PutMapping("/{id}")
    public KnowledgeResponse updateKnowledge(@PathVariable Integer id, @RequestBody KnowledgeReq knowledgeReq) {
        Knowledge existingKnowledge = knowledgeService.getKnowledgeById(id);
        if (existingKnowledge == null || !existingKnowledge.getTenantId().equals(SystemThreadLocal.getTenantId()) ||
                !existingKnowledge.getUserId().equals(SystemThreadLocal.getUserId())) {
            return null;
        }
        Knowledge knowledge = this.modelMapper.map(knowledgeReq, Knowledge.class);
        existingKnowledge.setUserId(SystemThreadLocal.getUserId());
        existingKnowledge.setModelId(knowledge.getModelId());
        existingKnowledge.setFileUrl(knowledge.getFileUrl());
        existingKnowledge.setFileName(knowledge.getFileName());
        existingKnowledge.setFileStatus(knowledge.getFileStatus());
        existingKnowledge.setWordCount(knowledge.getWordCount());
        existingKnowledge.setTenantId(SystemThreadLocal.getTenantId());
        existingKnowledge.setUpdateTime(knowledge.getUpdateTime());
        Knowledge savedKnowledge = knowledgeService.updateKnowledge(id, existingKnowledge);
        KnowledgeResponse response = new KnowledgeResponse();
        BeanUtils.copyProperties(savedKnowledge, response);
        return response;
    }

    @DeleteMapping("/delete")
    public CommonRes<Boolean> deleteKnowledge(@Param("id") @NotNull(message = "id不能为空") Integer id) {
        Knowledge existingKnowledge = knowledgeService.getKnowledgeById(id);
        if (existingKnowledge == null || !existingKnowledge.getTenantId().equals(SystemThreadLocal.getTenantId()) ||
                !existingKnowledge.getUserId().equals(SystemThreadLocal.getUserId())) {
            return CommonRes.success(false);
        }
        Boolean res = knowledgeService.deleteKnowledge(id);
        return CommonRes.success(res);
    }
}