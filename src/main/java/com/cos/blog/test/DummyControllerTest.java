package com.cos.blog.test;


import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

//아래 에노테이션의 레스트컨토롤러는 데이터를 리턴해주는 컨트롤러이다.
@RestController
public class DummyControllerTest<X> {
	
	
	@Autowired
	//의존성 주입(DI:Dependency Injection)
	//이 에노테이션을 쓰면 붙여주면
	//이 클래스가가 메모리에 로딩될때 자동으로
	//하기의 userRepository변수를 null에서 관련
	//메모리로 연결하여 셋팅해줌
	private UserRepository userRepository;
	
	//이름은 같지만 맵핑방법이 다름으로 아래 맵핑작동함
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id){
	 try {	
		userRepository.deleteById(id);
	 }catch(EmptyResultDataAccessException e) 
	 {
		 return "삭제에 실패하였습니다. 해당id는 DB에 없습니다.";
	 }
		
		return "삭제되었습니다. id:"+id;
	}
	
	
	
		
	@Transactional
	/*
	 * 아래 함수 종료시에 자동으로 Commit이 됨.
	더티 체킹: DB에서 가지고온 JPA내 영속성 컨텍스트안의 1차케쉬 안의 영속화된 데이터가
	@Transactional이 달린 자바 함수의 안에서 같은 Type의 오브젝트의 필드 변경시 이를
	이를 자동으로 감지하여 연결된 DB를 자동으로 업데이트 시켜주는 커밋(commit)을 행하게
	되어 DB의 해당 테이블의 해당 Row를 자동으로 업데이트 시켜 준다.
	*/
	
	/*
	   주소가 아래와 같으나 이것은 Put요청이고 아래는 "Get"요청이므로 구분이 된다...
	   이 업데이트 함수는 패스워드와 이메일만 수정하는 것으로 한다.(필드변수값: password, email)
	   json을 받으려면 @RequestBody라는 어노테이션이 필요함
	 */	 
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser ) {
		//브라우져에서 Put request를 이용하여 json형식으로 데이터를 스프링에게 보내면 
		//스프링이 Java Object로 변환해서 받아준다.
		//(스프링서버는 메세지컨버터의 젝스라이브러리를 이용하여
		//제이손 데이터를 자바를 객체로 변환해서 자바프로그램안으로 데이터를 자동으로 넣어 준다.)
		System.out.println("id:"+id);
		System.out.println("password:"+requestUser.getPassword());
		System.out.println("email:"+requestUser.getEmail());
		
		//아래식에서, optional객체인 user의 orElseThrow 메서드를 이용하여
		//null 에러처리를 람다식(무명함수식)을 이용하여 처리한것임
		User user = userRepository.findById(id).orElseThrow(
				()->{			
			                return new IllegalArgumentException("수정에 실패하였습니다!");
		               }
		);	   
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		/*
		//상기와 같이 java user오브젝트를 변경하여 그 변경된 user 오브젝트를 아래 함수에 넣으면...
		userRepository.save(user);
		//상기 save함수는 id를 전달하지 않으면 insert를 해주고...
		//id를 전달하면 해당id에 대한 데이터가 있으면 update를 해주고...
		//또한 전달된 id에 해당하는 데이터가 없으면 insert를 실행한다.
		*/	
		
		return user;		
	}
	
	
	
	
	//http://localhost:8000/blog/dummy/users
	//상기 주소값으로 웹브라우져에서 접근가능하도록 
	//아래 함수를 주소와 맵핑하는 것임
	//실제 물리주소와는 다르다; 즉 URI방식의 접근임)
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	//한페이지당 2건에 데이터를 리턴받아 볼 예정임
		@GetMapping("/dummy/user")
		public Page<User> pageList( 
				@PageableDefault(size=2,sort="id",direction=Sort.Direction.DESC) 
		                 Pageable pageable)
		{
			
			Page<User> pagingUsers =  userRepository.findAll(pageable);
			
			/*if(pagingUsers.isFirst()) {
			     //여기에 필요한 처리를 하면된다!
		    }*/
			
			List<User> users = pagingUsers.getContent();
			return pagingUsers;
		}	
	
	//{id}주소로 파라미터를 전달 받을 수 있음
	//http://localhost:8000/blog/dummy/user/3
	@GetMapping("dummy/user/{id}")
	public User detail(@PathVariable int id) 
	{
		User user = userRepository.findById(id).orElseThrow(
				
				//람다식으로 표현
				/*
				()->{
					return new IllegalArgumentException("해당유저가 없습니다 id:"+id);
				}	
				*/	
				
				//일반식으로 표현
				new Supplier<IllegalArgumentException>() {
					@Override
					public IllegalArgumentException get() {
						// TODO Auto-generated method stub
						return new IllegalArgumentException("해당유저는 없습니다 id:"+id);
					}					
				}
				
			);
		//유저객체는 자바 오브젝트이고 요청은 웹브라우져에서 했고
		////이 클래스 객체는 상기 맨위의 에노테이션 처럼
		// 레스트컨토롤러로 데이터를 리턴해주는 컨트롤러이다.
		//웹브라우저에게 자바객체인 유져를 바로 리턴해줘봐야
		//웹브라우저는 이해를 하지 못하므로 이 자바 객체인 user를
		//Json객체(중간매개data포맷)으로 변환하여 리턴해 주어야
		//웹브라우저가 이해할 수 있다. 
		//스프링부트는 메세지컨버터가 응답시에 자동 작동하여
		//만약에 자바 오브젝트를 리턴되는 상황이 감지되면 이 메세지컨버터가
		//잭슨(Jackson)이라는 라이브러리를 호출해서 user오브젝트를
		//json으로 변환하여 웹브라우져에게 던져주게 된다.
		return user;
	}
	
    //http://localhost:8000/blog/dummy/join
	//http의 body에 필드이름과 동일한 변수명인 username, password, email
	//로 요청하게되면...
	@PostMapping("/dummy/join")
	public String join(User user) 
	{//key=value(약속된 규칙)
		System.out.println("Id:"+user.getId());
		System.out.println("username: "+user.getUsername());	
		System.out.println("password: "+user.getPassword());	
		System.out.println("email: "+user.getEmail());	
		System.out.println("role:"+user.getRole());
		System.out.println("createDate:"+user.getCreateDate());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
}
