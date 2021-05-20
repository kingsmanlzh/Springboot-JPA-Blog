package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller//파일을 리턴하는 것임(해당경로 이하에 있는 파일을 리턴해줌)
public class TempControllerTest {
	
	//http://localhost:8000/blog/temp/home
    @GetMapping("/temp/home")
	public String tempHome() {
    	
    	System.out.println("tempHome");
    	//(1)파일리턴 기본 경로: src/main/resources/static
    	//(2)리턴명: /home.html
    	//상기 (1)+(2)가 붙어서 풀경로가 된다.(해당경로가 된다!)
    	return "/home.html";// "/"안 빼먹도록 주의할것!
    }
    
    @GetMapping("/temp/jsp")
    public String tempJsp() 
    {
    	//prefix: /WEB-INF/views/
    	//suffix: .jsp
    	//상기와 아래를 조합하면...
    	///WEB-INF/views/test.jsp
    	
    	return "test";
    }
    
}
