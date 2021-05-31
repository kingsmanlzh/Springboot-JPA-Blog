package com.cos.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.cos.blog.model.User;
import lombok.Getter;
//스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails타입의 오브젝트를 스프링 시큐리티의
// 고유한 세션저장소에 저장을 해준다.
@SuppressWarnings("serial")
@Getter//getter()가 자동으로 만들어진것을 간주되어 다른곳에 쓸수 있음
public class PrincipalDetail implements UserDetails  {
	
	private User user;//컴포지트
   
     public PrincipalDetail(User user){
    	 this.user=user;
     }	 
	 
	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		
		return user.getUsername();
	}

	//계정이 만료되지 않았는지를 리턴해 준다(true:계정이 만료 안됨)
	@Override
	public boolean isAccountNonExpired() {
		
		return true;//계정만료안됨(계정이살아있다)
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;//계정이 잠겨있지않다(계정이 열려있다)
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;//비밀번호가 만료되지 않았음(비밀번호가 살아있음)
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;//계정활성화가 되어있다
	}
	
	//계정이 가지고 있는 권한목록을 리턴해줌(목록이기때문에 컬렉션 타입 리턴 값으로 설정됨에 유의)
	//아래 함수리턴타입설명: GrantedAuthority를 상속(extends)한 
	//어떤(?)데이터형(type)의 콜렉션이여야한다는 점에 유의
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		
		/*
		collectors.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {
				
				return "ROLE_"+user.getRole();//스프링에서는 규칙으로 ROLE_프리픽스를 반드시사용(ex:ROLE_USER,...)
			}
			
		});
		*/
		//상기식의 람다식표현(컴파일러가 add의 인수로 GrantedAuthority 클래스 오브젝트만 들어가
		//는 것을 알고 있고 GrantedAuthority 클래스는 오버라이드해야하는 단 하나의 메소드만 가지
		//므로 아래와 같은 람다식 표현으로 상기를 대체가능함(Java1.8이상버전부터 java에 무명함수(람다식)사용가능)
		collectors.add(()->{
			
			return "ROLE_"+user.getRole();//스프링에서는 규칙으로 ROLE_프리픽스를 반드시사용(ex:ROLE_USER,...)
			
		});	
		
		return collectors;
	}
	
	
}
