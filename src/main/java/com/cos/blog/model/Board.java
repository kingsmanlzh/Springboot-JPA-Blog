package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder//빌터패턴 사용

//ORM ->Java(다른언어포함)의 Object ->DB Table로 맵핑해주는 기술
@Entity//Mysql에 "board"테이블을 생성한다
public class Board {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	 private int id;

	@Column(nullable=false, length=100)
	private String title;
	@Lob//대용량 데이터 사용
    private String content;//섬머노트 라이브러리 사용 <html>태크가 섞여서 디자인됨
	
	private int count;// 조회수
	
	@ManyToOne(/*fetch=FetchType.EAGER*/) //보드(Board)가 many이고 유저(User)가 one이라는 것을 의미함
	//=> 즉, 한명(one)의 유저(User)는 여러개(many)의 보드(Board)를 쓸 수 있다는 의미	
	//=>여러개의 게시글은 한명의 유저에 의해서 쓰일 수 있다는 의미
	@JoinColumn(name="userId")
	private User user;//DB는 오브젝트를 저장할 수 없다. 그래서 FK를 사용하는데
	//객체 지향프로그래밍 언어같은 자바의 경우 오브젝트를 저장할 수 있다.
	//여기서 충돌이 나는데, 즉 자바는 오브젝트를 저장할 수 있지만 DB는 오브젝트를
	//저장할 수 없다. 그렇하기에 자바는 어쩔 수 없이 DB에 맞추어서
	//오브젝트를 저장하는것이 아니라 FK즉, 키값을 저장해야하지만...
	//하지만 JPA즉 ORM을 사용하면 오브젝트를 그대로 저장할 수 있게하기 위해서
	//JoinColumn애노테이션을 사용하여 연결한다.
	
	@OneToMany(mappedBy="board",fetch=FetchType.EAGER, cascade=CascadeType.REMOVE) //mappedby가 적혀있으면 연관관계의 주인이 아니다.
	//mappedBy: 양방향 관계 설정시 관계의 주체가 되는 쪽에서 정의합니다.
	//fetch: FetchType.EAGER, FetchType.LAZY로 전략을 변경 할 수 있습니다. 두 전략의 차이점은 EAGER인 경우 관계된 Entity의 정보를 미리 읽어오는 것이고 LAZY는 실제로 요청하는 순간 가져오는겁니다.
	//cascade: 현 Entity의 변경에 대해 관계를 맺은 Entity도 변경 전략을 결정합니다.
    //              속성값에는 CascadeType라는 enum에 정의 되어 있음
	//              (enum값: ALL, PERSIST, MERGE, REMOVE, REFRESH, DETACH)
	
	//즉, "나는 포린키(FK)가 아니예요"
	//즉, DB에 컬럼을 만들지 마세요...
	//상기의 "board"는 Replay클래스의 필드명(실제값을 저장하는 변수명)임에도 유의
	
	//아래와 같이 애노테이션을 설정하면...replys안에서 다시 board를 호출하게 될때 board는 게터호출을 무시하게
	//설정이되어 무한 반복을 끊게 된다.(즉 이경우는 json으로 시리얼라이제이션하지 않는다=제이슨으로 파싱하지 않는다)
	@JsonIgnoreProperties({"board"})//무한 참조 방지용 (왜냐하면 Reply객체 안에 또 Board객체를 포함하고 있음)
	@OrderBy("id desc")
	private List<Reply> replys;	
	
	@CreationTimestamp
	private Timestamp createData;
	
}
