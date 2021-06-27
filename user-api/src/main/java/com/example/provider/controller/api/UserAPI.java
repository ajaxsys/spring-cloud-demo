package com.example.provider.controller.api;

import com.example.provider.controller.api.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

//【坑】注意，最好不带根的@RequestMapping.否则会报：Ambiguous mapping.
// https://blog.csdn.net/P_Top/article/details/106879539
//【案】非要用@RequestMaping也行，可以在fallback里面，设置为别的不一样的URL
@RequestMapping("/user")
public interface UserAPI {

    @GetMapping("/{id}")
    User findById(@PathVariable Integer id);

    @GetMapping("/fallback")
    String fallback();
}
