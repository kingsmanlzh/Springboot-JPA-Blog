package com.cos.blog.model;

import java.sql.Timestamp; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import org.hibernate.annotations.CreationTimestamp;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder//빌터패턴 사용

//ORM ->Java(다른언어포함)의 Object ->DB Table로 맵핑해주는 기술
@Entity//User클래스가 Mysql에 테이블이 생성된다.
//@DynamicInsert//insert시에 null인 필드를 제외시켜준다.
public class User {  
  
	@Id	
	//프로젝트에 연결된 DB의 넘버링 전략을 따라간다...
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	//비어 놓아도 자동으로 들어감(DB에서 자동으로 입력한다.)
	private int id;//시퀸스, auto_increment
	
	
	@Column(nullable=false, length=100, unique=true) 
	private String username;//아이디
	
	@Column(nullable=false, length=100)//해쉬(비밀번호암호화를 위해 크기를 크게 준다)
	private String password;
	
	@Column(nullable=false, length=50)
	private String email;
	
	
	
	//private String role;//Enum을 쓰는 것이 좋다.
	//@ColumnDefault("user");
	@Enumerated(EnumType.STRING)
	private RoleType role;
	
	private String oauth;//kakao, google ...
	
	@CreationTimestamp//시간이 자동으로 입력됨(현재시간자동입력)
	//비어놓아도 자동으로 들어감
	private Timestamp createDate;
	
}
