package com.cos.blog.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//스프링이 com.cos.blog패키지 이하를 스캔해서 모든 파일을 메모리에 new하는 것은 아니구요,
//특정 어노테이션(@)이 붙어 있는 패키지내 클래스 파일들을 스프링 자기가 메모리를 할당하는 (즉 new하는) 
//제어의 역전 (IoC:Inversion of Control)을 시행하여 스프링 컨테이너에서 관리해 줍니다.
@RestController
public class BlogControllerTest {
   
	//http://localhost:8080/test/hello
	@GetMapping("/test/hello")
	public String hello() 
	{
		return "<h1>hello spring boot</h1>";
	}
}
