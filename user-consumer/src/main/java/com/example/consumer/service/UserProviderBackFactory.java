package com.example.consumer.service;

import java.util.Map;

import com.example.provider.controller.api.UserAPI;
import com.example.provider.controller.api.model.User;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

// 另一种方法，可以获取更详细的错误信息（cause）
@Component
public class UserProviderBackFactory implements FallbackFactory<UserAPI> {

	@Override
	public UserAPI create(Throwable cause) {
		return new UserAPI() {
			@Override
			public User findById(Integer id) {

				User u = new User();
				u.setId(-2);
				u.setName("降级了ByFactory");
				System.out.println("降级了ByFactory");
				return u;
			}

			@Override
			public String fallback() {
				System.out.println(cause);
				if(cause instanceof FeignException.InternalServerError) {

					return "远程服务器 500" + cause.getLocalizedMessage();
				}else {
					System.out.println("降级了～～");

					return "呵呵";
				}
			}

		};
	}

}
