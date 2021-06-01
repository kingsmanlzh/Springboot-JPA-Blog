package com.cos.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

//스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌(IoC를 해준다는 의미)=Ioc컨테이너에 등록(=bean컨테이너에 등록)
//Ioc => Inversion of Control:"제어의 역전"
//=> 스프링이 new하여 등록해준다.(프로그래머가 new하는 것을 대신해서...)
//=>스프링이 new한다는것은 메모리에 프로그래머를 대신해서 이 클래스의 오브젝트를 메모리에 띄워준다는 의미이다.
@Service
public class UserService {

	@Autowired // 연결하고...DI로요....Depandency Injection로요..뭘연결? DB(?)로 연결...
	// 연결하면 아래 userRepository로 객체가 들어온다...
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	// 아래 "회원가입"서비스가 하나의 트랜젝션으로 묶이게되고
	// 성공하면 DB에서 commit이되고...
	// 실패하면 rollback이됨
	@Transactional
	public void 회원가입(User user) {

		String rawPassword = user.getPassword();// 1234 원문
		String encPassword = encoder.encode(rawPassword);// 해쉬화
		user.setPassword(encPassword);
		// 실제로 DB에 insert를 하고 아래에서 return이 되면 되요!
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}

	@Transactional
	public void 회원수정(User user) {
		// 수정시에는 영속성 컨텍스트 User 오브젝트를 영속화시키고 영속화된 User 오브젝트를 수정
		// select를 해서 User오브젝트를 DB로 부터 가져오는 이유는 영속화를 하기 위해서
		// 영속화된 오브젝트를 변경하면 자동으로 DB에서 update문을 날려주거든요!
		
		User persistance = userRepository.findById(user.getId()).orElseThrow(() -> {
			return new IllegalArgumentException("회원찾기 실패");
		});
		
		//Validation 체크(카카오 로그인 등의 사용자는 비밀 번호 못바꾸게하는 것!)
		if (persistance.getOauth() == null || persistance.getOauth().equals("")) {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistance.setPassword(encPassword);
			persistance.setEmail(user.getEmail());
			// 회원수정 종료시 =서비스종료=트랜젝션 종료=commit이 자동으로 됨
			// 영속화된 persistance객체의 변화가 감지되면 더티체킹이 되어
			// 변화된 것들을 자동으로 update을 날려줌
		}

	}

	public User 회원찾기(String username) {

		User user = userRepository.findByUsername(username).orElseGet(() -> {
			return new User();
		});
		return user;

	}

}
