package cn.aistore.ai.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    @GetMapping("/hello")
    public String home() {
        return "Welcome to home!";
    }


}
