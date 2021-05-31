package com.cos.blog.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

@Service//Bean 등록
public class PrincipalDetailService implements UserDetailsService {

	@Autowired 
	private UserRepository userRepository;
	
	//스프링이 로그인 요청을 가로챌때 username, password 2개(두 변수)를 가로채는데 password부분처리는 알아서함
	//해당 username이 DB에 있는지만 확인해주면 됨.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		/*User principal*/ User user= userRepository.findByUsername(username)
				.orElseThrow(()->{
					return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다:"+username);
				});
		//아래와 같이 실행하면 시큐리티의 세션에 유저 정보가 저장이 된다
		
		return new PrincipalDetail(user/*principal*/);
	}

}
