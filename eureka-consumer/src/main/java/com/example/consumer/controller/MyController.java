package com.example.consumer.controller;


import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class MyController {

    @Value("${server.port}")
    Integer port;

    // 注意这是Spring的抽象API,为了不绑定Eureka，我们最好使用接口，而非实现类（如 EurekaClient)
    @Autowired
    DiscoveryClient client;

    @Autowired
    LoadBalancerClient lb;

    @GetMapping("/hi")
    String hi() {
        return "hi consumer:" + port;
    }

    /**
     * 手动看看eureka服务器有啥
     * @return
     */
    @GetMapping("/client1")
    public Object seeWhatIsServicesAndInstances() {
        List<String> services = client.getServices();
        for (String service : services) {
            System.out.println("service = " + service);
        }
        return client.getInstances("provider");
    }

    /**
     * 手动看看eureka服务器有啥
     * @return
     */
    @GetMapping("/client2")
    public String manuallyCallByOneProvider() {

        List<ServiceInstance> providers = client.getInstances("provider");

        ServiceInstance provider = providers.stream().findFirst().get();
        // 想要深入分析provider，找不到status，只能转为具体的Eureka类型
        return manuallyCallHiByEurekaProvider((EurekaServiceInstance) provider);
    }

    /**
     * 使用负载均衡来取得其中一个（Ribbon）
     * @return
     */
    @GetMapping("/client3")
    public String loadbalancerCallByOneProvider() {

        // List<ServiceInstance> providers = client.getInstances("provider");
        ServiceInstance provider = lb.choose("provider");
        return manuallyCallHiByEurekaProvider((EurekaServiceInstance) provider);

    }

    private String manuallyCallHiByEurekaProvider(EurekaServiceInstance provider) {
        InstanceInfo instanceInfo = provider.getInstanceInfo();
        if (instanceInfo.getStatus() == InstanceInfo.InstanceStatus.UP) {
            String url = "http://" + instanceInfo.getHostName() + ":" + instanceInfo.getPort() + "/hi";

            System.out.println("url:" + url);

            RestTemplate restTemplate = new RestTemplate();

            String respStr = restTemplate.getForObject(url, String.class);

            System.out.println("respStr: " + respStr);
            return respStr;
        }
        return "No provider found, server may down";
    }
}
