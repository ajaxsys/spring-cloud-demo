package com.example.consumer.service;

import com.example.provider.controller.api.UserAPI;
import com.example.provider.controller.api.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

// 最简单测试方法：Provider关闭（模拟连不上）
// implements UserAPI: 为所有方法提供fallback
@Component
@RequestMapping("/user/fallback")
public class UserProviderSimpleFallback implements UserProviderFiegnClient { // 【坑】注意：不能是UserAPI
    @Override
    public User findById(Integer id) {
        User u = new User();
        u.setId(-1);
        u.setName("降级了");
        return u;
    }

    @Override
    public String fallback() {
        return "hehe fallback!";
    }
}
