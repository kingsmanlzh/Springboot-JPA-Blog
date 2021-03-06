package com.cos.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.blog.config.auth.PrincipalDetailService;

//아래 3개의 에노테이션을 스프링 시큐리티 설정사용시
//셋트로 걸어준다는 점을 인식할것

//bean(빈)등록이란? 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@Configuration//아래 SecurityConfig 클래스 객체를 싱글톤으로 생성함

//컨트롤러에 가서 실행되기 전에 아래 에노태이션이 동작해서...
//아래의 주소로 들어오면 그냥통과..아니면 auth절차를 거쳐라라는
//필터링이 필요하므로....아래 에노태이션을 쓴다.
@EnableWebSecurity//시큐리티 필터 등록이된다.=>활성화된 스프링 시큐리티에 어떤 설정을 아래 해당클래스에서 하겠다.

//특정 주소 접근을 하면 권한 및 인증을 미리 체크하겠다는 의미
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
    @Autowired
	private PrincipalDetailService principalDetailService;      
    
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}

	@Bean//Ioc가 되요!(리턴값을 스프링이 관리하게된다)
	public BCryptPasswordEncoder encodePWD() {
	   return new BCryptPasswordEncoder();
	}
	
	//시큐리티가 대신 로그인해주는데 password를 가로채기를 하는데
	//해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
	//같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음.
	
   @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}	
	
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    	    .csrf().disable() //csrf 토큰 비활성화(테스트시는 걸어둔다)
    		.authorizeRequests()
    		     //인증이 안된 사용자들이 출입할 수 있는 경로 /auth/**는 허용
    		     //그냥 주소가 "/"이면 index.jsp도 허용
    		     //static이하에 있는 /js/**, /css/**, /image/** 도 허용
    			.antMatchers("/","/auth/**","/js/**","/css/**","/image/**","/dummy/**")
    			.permitAll()//상기 경로는 인증 없이 접근 가능
    			//상기외 모든 경로는 인증이 필요하게 설정
    	        .anyRequest()
    	        .authenticated()
    	    .and()//계속...
    	        .formLogin()
    	           .loginPage("/auth/loginForm")//로긴이 필요할 경우 (회원인증이 필요한 경우) 스프링 시큐리티의 디폴트 로긴 페이지 대신 이 페이지로 리다이렉션 시킴
	    	        //스프링 시큐리티가 해당 주소로 요청이 오는 로그인을 가로채서 대신 로그인을 해준다.
	    	        .loginProcessingUrl("/auth/loginProc")
	    	        //로그인이 정상적 성공시 갈 Uri설정
	    	        .defaultSuccessUrl("/")
    	    .and()
    	        //로그아웃시 설정할 것들(이지현 2021.06.02추가)
    	        .logout()
	    	        .logoutUrl("/logout")
	    	        .logoutSuccessUrl("/")
	    	        .invalidateHttpSession(true);
    }
}
