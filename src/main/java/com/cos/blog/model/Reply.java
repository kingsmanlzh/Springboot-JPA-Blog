package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

//import com.cos.blog.dto.ReplySaveRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Reply {
	@Id	
	//프로젝트에 연결된 DB의 넘버링 전략을 따라간다...
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	//비어 놓아도 자동으로 들어감(DB에서 자동으로 입력한다.)
	private int id;//시퀸스, auto_increment
	
	@Column(nullable=false, length=200)
	private String content;
	
	@ManyToOne
	@JoinColumn(name="boardId")
	private Board board;
	
	@ManyToOne
	@JoinColumn(name="userId")	
	private User user;
	
	@CreationTimestamp
	private Timestamp createDate;
	
	/*public void update(User user, Board board, String content) {
		setUser(user);
		setBoard(board);
		setContent(content);
	}*/
	
}