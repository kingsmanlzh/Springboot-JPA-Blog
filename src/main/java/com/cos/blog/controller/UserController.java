package com.cos.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
public class UserController {

	@Value("${cos.key}")
	private String coskey;
	
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		
		return "user/joinForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		
		return "user/loginForm";
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm(){
		return "user/updateForm";
	}
	
	//아래 @ResponseBody를 붙이면 데이터를 리턴해주는 컨토롤러함수가 됨
	@GetMapping("/auth/kakao/callback")
	public /*@ResponseBody*/  String kakaoCallback(String code){
	//상기 @ResponseBody를 없애야지 뷰리졸브를 호출해서 파일을 찾아감	
		
		//post방식으로 key=value데이터를 요청(카카오쪽으로)
		//=>필요라이브러리: RestTemplate를 사용
        RestTemplate rt = new RestTemplate();		
        
        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        
        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "b91d3e70224705dacb62c2cd605da404");
        params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
        params.add("code", code);//code는 이함수의 파라미터에서 받는다.(동적으로...)
        
        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest=
        		new HttpEntity<>(params,headers);
        
        //Http 요청하기 - Post방식으로 - 그리고 response변수의 응답을 받음
        ResponseEntity<String> response = rt.exchange(
           "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,           
        	String.class
       ); 
        //Gson, Json Simple, ObjectMapper(여기서 사용할라이브러리임)
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken=null;
        try {
			oauthToken = objectMapper.readValue(response.getBody(),OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        System.out.println("카카오 엑세스 토큰:"+oauthToken.getAccess_token());
        
        ////////////////////////////////////////////////////////////////////////////////////////////
        
		//post방식으로 key=value데이터를 요청(카카오쪽으로)
		//=>필요라이브러리: RestTemplate를 사용
        RestTemplate rt2 = new RestTemplate();		
        
        //HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
              
        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest2=
        		new HttpEntity<>(headers2);
        
        //Http 요청하기 - Post방식으로 - 그리고 response변수의 응답을 받음
        ResponseEntity<String> response2 = rt2.exchange(
           "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoProfileRequest2,           
        	String.class
       ); 
        
     
        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile=null;
        try {
        	kakaoProfile = objectMapper2.readValue(response2.getBody(),KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        
        //User 오브젝트: username, password, email
        System.out.println("카카오 아이디(번호):"+kakaoProfile.getId());
        System.out.println("카카오 이메일:"+kakaoProfile.getKakao_account().getEmail());
        
        System.out.println("블로그 서버 유저 네임:"+kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
        System.out.println("블로그 서버 이메일:"+kakaoProfile.getKakao_account().getEmail());
        System.out.println("블로그서버 패스워드:"+coskey);
        
        User kakaoUser = User.builder()
        		.username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
        		.password(coskey)
        		.email(kakaoProfile.getKakao_account().getEmail())
        		.oauth("kakao")
        		.build();
        
        //가입자 인지 비가입자인지 체크해서 처리
       User originUser = userService.회원찾기(kakaoUser.getUsername());
       //System.out.println(originUser);
       if(originUser.getUsername() == null) {
    	   System.out.println("기존 회원이 아니기 때문에 자동 회원가입을 진행합니다.");
    	   userService.회원가입(kakaoUser);
       }
       System.out.println("자동 로그인을 진행합니다.");       
       
       //자동 로그인 처리
       Authentication authentication = authenticationManager.authenticate(
				 new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(),coskey));
	    SecurityContextHolder.getContext().setAuthentication(authentication);	    
               
		return "redirect:/";
	}
	
}
