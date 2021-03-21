package com.example.consumer.controller;


import com.example.consumer.service.ProviderFiegnClient;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
public class MyController {

    @Value("${server.port}")
    Integer port;

    // 注意这是Spring的抽象API,为了不绑定Eureka，我们最好使用接口，而非实现类（如 EurekaClient)
    @Autowired
    DiscoveryClient client;

    @Autowired
    LoadBalancerClient lb;

    @Autowired
    RestTemplate restTemplateWithLB;

    @Autowired
    ProviderFiegnClient fiegnClient;

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

    /**
     * 不自己拼接URL的使用负载均衡（Ribbon），只是指定服务名：provider即可
     * http://localhost:7780/client4
     * 注意需要给RestTemplate配置上 @LoadBalanced
     *
     *     @Bean
     *     @LoadBalanced
     *     RestTemplate singletonRestTemplate() {
     *         return new RestTemplate(); // 此处还可以加更多拦截器等
     *     }
     *
     * @return
     */
    @GetMapping("/client4")
    public ResponseEntity<String> simpleLoadbalancerCallByOneProvider() {

        // ServiceInstance provider = lb.choose("provider");

        // 只返回http body==结果
        // return restTemplateWithLB.getForObject("http://provider/hi", String.class);
        // 还返回http header
        ResponseEntity<String> forEntity = restTemplateWithLB.getForEntity("http://provider/hi", String.class);
        // <200,hi:7090,[Content-Type:"text/plain;charset=UTF-8", Content-Length:"7", Date:"Sun, 21 Mar 2021 03:21:06 GMT", Keep-Alive:"timeout=60", Connection:"keep-alive"]>
        System.out.println(forEntity);
        return forEntity;
    }

    ////////////////////////// RestTemplate 传参 //////////////////////////

    @GetMapping("/client5")
    public Map restTemplateWithParameters() {
        // 两种URL传参数方式
        String url = "http://provider/getMap?age={1}&name={2}";

        return restTemplateWithLB.getForObject(url, Map.class, 22, "xiaoming");
    }
    @GetMapping("/client6")
    public Map restTemplateWithParametersMap() {
        // 两种URL传参数方式
        String url = "http://provider/getMap?age={ageKey}&name={nameKey}";

        return restTemplateWithLB.getForObject(url, Map.class,
                Map.of(
                        "nameKey", "xiaofang",
                        "ageKey", 2));
    }


    ////////////////////////// 上面为止都是RestTemplate, 接下来换个客户端。 //////////////////////////


    @GetMapping("/client7")
    public String callByFeignClient() {
        return fiegnClient.hi();
    }
    @GetMapping("/client8")
    public Map callByFeignClientWithParameter() {
        return fiegnClient.getMap("hehe", 66);
    }



    ////////////////////////// 上面为止都是硬编码，比较简单，但是API硬编码维护困难。接下来使用Feign，基于接口编程。 //////////////////////////

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
