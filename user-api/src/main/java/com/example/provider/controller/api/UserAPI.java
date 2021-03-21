package com.example.provider.controller.api;

import com.example.provider.controller.api.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public interface UserAPI {

    @GetMapping("/{id}")
    User findById(@PathVariable Integer id);
}
