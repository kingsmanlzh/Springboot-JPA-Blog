package com.cos.blog.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cos.blog.model.User;

//JpaRepository는
//Jsp로 치면...DAO :데이터 엑세스 오브젝트라고 볼 수 있고...
//자동으로 bean으로 등록된다.
//@Repository애노테이션을 생략가능하다
//스프링 부트가 UserReposotory를 메모리로 자동으로 띄워준다
//아래의 의미는 JapRepository가 User테이블을 관리할것인데 PrimaryKey의 타입은 Integer이다...
public interface UserRepository extends JpaRepository<User,Integer>{

	//아래는 실제 구동은 =>SELECT * FROM user WHERE username=1? 
	Optional<User> findByUsername(String username); 
}


//JPA Naming 퀴리 전략
//SELECT * From user WHERE username=?1 And password?2;
//User findByUsernameAndPassword(String username, String password);

//or
/*
@Query(value="SELECT * From user WHERE username=?1 And password?2;", nativeQuery=true)
User login(String username, String password);
*/