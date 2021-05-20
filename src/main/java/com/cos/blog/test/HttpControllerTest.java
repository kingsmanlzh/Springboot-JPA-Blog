package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpControllerTest {
	
	private static final String TAG="HttpControllerTest";
	
	//http://localhost:8000/blog/http/lombok
	@GetMapping("/http/lombok") 
	public String lombokTest() 
	{		
		Member m= Member.builder()
				.username("ssar")
				.password("1234")
				.email("ssar@nate.com")
				.build();//new Member("ssar", "1234", "email");
		System.out.println(TAG+"getter : "+m.getUsername());
		m.setUsername("cos");
		System.out.println(TAG+"After Setting : "+m.getUsername());			
		return "lombok test 완료";		
	}
	
   // http://localhost:8080/http/get
	@GetMapping("/http/get") //(select)
	public String getTest(Member m){
		
	
		
		return "get요청: "+m.getId()+", "+m.getUsername()+", "+m.getPassword()+", "+m.getEmail();
	} 
	
	@PostMapping("/http/post")
	public String postTest(@RequestBody Member m){
		return "post요청: "+m.getId()+", "+m.getUsername()+", "+m.getPassword()+", "+m.getEmail();

	}
	
	@PutMapping("/http/put")
	public String putTest(@RequestBody Member m){
		return "put요청: "+m.getId()+", "+m.getUsername()+", "+m.getPassword()+", "+m.getEmail();
   }
	@DeleteMapping("/http/delete")
	public String deleteTest(@RequestBody Member m){
		
		m.setId(0);
		m.setUsername("");
		m.setPassword("");
		m.setEmail("");
	   return "delete됨: "+m.getId()+", "+m.getUsername()+", "+m.getPassword()+", "+m.getEmail();
	   }
}

