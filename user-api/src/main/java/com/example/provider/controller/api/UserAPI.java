package com.example.provider.controller.api;

import com.example.provider.controller.api.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

//【坑】注意，不能带根的@RequestMapping.https://blog.csdn.net/P_Top/article/details/106879539
// @RequestMapping("/user")
public interface UserAPI {

    @GetMapping("/User/{id}")
    User findById(@PathVariable Integer id);

    @GetMapping("/User/fallback")
    String fallback();
}
