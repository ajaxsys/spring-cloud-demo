package com.example.consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
public class EurekaConsumer {
    public static void main(String[] args) {
        SpringApplication.run(EurekaConsumer.class);
    }

    @Bean
    @LoadBalanced // 开启负载均衡
    RestTemplate singletonRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // 开启拦截器
        restTemplate.getInterceptors().add((request,body,execution) -> {
            System.out.println("拦截URL" + request.getURI());
            ClientHttpResponse response = execution.execute(request, body);
            System.out.println("拦截结果" + response.getHeaders());
            return response;
        });
        return restTemplate;
    }
}
