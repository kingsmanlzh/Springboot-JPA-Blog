package com.cos.blog.controller.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
     
@RestController//데이터만 리턴해 줄거기 때문에...
public class UserApiController {

	@Autowired//Dependency Injection(DI)를 받아서 사용하기 위해
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/auth/join")
	public ResponseDto<Integer> save(@RequestBody User user) {
		
		//로그인하는 방식: 스프링 시큐리티 이용	
		//System.out.println("UserApiController: save호출됨");	
		userService.회원가입(user);
		 return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);//자바 오브젝트를 JSON으로 리턴해줌 by Jackson라이브러리
	}	
	
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user){
		userService.회원수정(user);
		//여기서는 트랜젝션이 종료되기 때문에 DB값은 변경이 됐지만
		//하지만 세션값은 변경이 되지 않았기 때문에 우리가 직접 세션값을 변경해줄 것임		
		
		//세션등록
		 Authentication authentication = authenticationManager.authenticate(
				 new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
	    SecurityContextHolder.getContext().setAuthentication(authentication);	    
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);//자바 오브젝트를 JSON으로 리턴해줌 by Jackson라이브러리

	}
}
