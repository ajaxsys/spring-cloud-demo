package com.example.provider.controller;

import  com.example.provider.controller.api.UserAPI;

import com.example.provider.controller.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController implements UserAPI {

    @Value("${server.port}")
    Integer port;

    @Override
    public User findById(Integer id) {
        User user = new User();
        user.setId(id);
        user.setName("I am " + this.port);
        return user;
    }
}
