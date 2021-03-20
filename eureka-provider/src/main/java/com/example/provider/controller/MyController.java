package com.example.provider.controller;


import com.example.provider.service.HealthStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Value("${server.port}")
    Integer port;

    @Autowired
    HealthStatusService healthStatusService;

    @GetMapping("/hi")
    String hi() {
        return "hi:" + port;
    }

    /**
     * 如短信服务，到了提供商限额，想要主动报告Eureka自己停止服务。
     *
     * 《例如》
     * 执行：
     * http://localhost:7090/actuator/health
     * 看到服务本身的状态：UP/DOWN
     *
     * 执行
     * http://localhost:7002/
     * 看到服务是否在Eureka里面上下线
     *
     * @param status
     * @return
     */
    @GetMapping("/health")
    String manuallyChangeHealthStatus(
            @RequestParam("status") Boolean status
    ) {
        healthStatusService.setStatus(status);
        return healthStatusService.getStatus();
    }
}
