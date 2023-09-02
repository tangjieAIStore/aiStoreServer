package cn.aistore.ai.rest.controller;

import cn.aistore.ai.common.CommonRes;
import cn.aistore.ai.common.SystemThreadLocal;
import cn.aistore.ai.domain.TokenCount;
import cn.aistore.ai.repository.TokenCountService;
import cn.aistore.ai.rest.domain.TokenCountReq;
import cn.aistore.ai.rest.domain.TokenCountResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/token")
public class TokenCountController {
    @Autowired
    private TokenCountService tokenCountService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/create")
    public CommonRes<Integer> save(@RequestBody @Validated TokenCountReq tokenCountReq) {
        tokenCountReq.setTenantId(SystemThreadLocal.getTenantId());
        tokenCountReq.setUserId(SystemThreadLocal.getUserId());
        TokenCount tokenCount = this.modelMapper.map(tokenCountReq, TokenCount.class);
        TokenCount savedTokenCount = tokenCountService.save(tokenCount);
        return CommonRes.success(savedTokenCount.getId());
    }

    @DeleteMapping("/delete")
    public void deleteById(@Param("id") Integer id) {
        tokenCountService.deleteById(id);
    }

    @GetMapping("/{id}")
    public TokenCountResponse findById(@PathVariable Integer id) {
        TokenCount tokenCount = tokenCountService.findById(id);
        TokenCountResponse response = new TokenCountResponse();
        BeanUtils.copyProperties(tokenCount, response);
        return response;
    }

    @GetMapping
    public List<TokenCountResponse> findAll() {
        List<TokenCount> tokenCounts = tokenCountService.findAll();
        List<TokenCountResponse> responses = new ArrayList<>();
        for (TokenCount tokenCount : tokenCounts) {
            TokenCountResponse response = new TokenCountResponse();
            BeanUtils.copyProperties(tokenCount, response);
            responses.add(response);
        }
        return responses;
    }
}