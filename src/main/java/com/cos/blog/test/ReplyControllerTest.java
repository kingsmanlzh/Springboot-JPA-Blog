package com.cos.blog.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@RestController
public class ReplyControllerTest {

	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@GetMapping("/test/board/{id}")
	public Board getBoard(@PathVariable int id) {
		//[하기와 같이 리턴시 무한 참조가 일어나는 원리 설명]
		//jackson라이브러리 발동(오브젝트를 Json으로 리턴해줌)
		//=>모델이 들고 있는 게터(getter)를 호출해서 제이슨으로
		//바꾸어주는데...이는 즉, Board 클래스의 모든 필드의 getter가 호출됨
		//그 필드 중 replys는 Reply클래스 내부의 getter를 또 호출하게됨
		//=>그 게터중 board가 Replay의 클래스 필드변수이므로  Board클래스 
		//내부의 게터가 작동하면서 또 replays를 리턴 하게 된다..무한반복..됨
		//=>무한 참조 해결방법:
		//=>Board.java파일안에
		//@JsonIgnoreProperties({"board"})로 설정
		//private List<Reply> replys;		
		return boardRepository.findById(id).get();
	}
	
	@GetMapping("/test/reply")
	public List<Reply> getReply(){
			return replyRepository.findAll();
	}
}
