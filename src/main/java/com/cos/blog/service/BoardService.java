package com.cos.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import lombok.RequiredArgsConstructor; 

@Service
//아래 에노테이션과 변수에 final을 붙여서
//한꺼번에 DI 하는 방법; 각각 객체변수에서@Autowired제거 가능
@RequiredArgsConstructor 
public class BoardService {
	
	//@Autowired//DI
	private final BoardRepository boardRepository;
	
	//@Autowired
	private final ReplyRepository replyRepository;
	
	@Transactional
	public void 글쓰기(Board board, User user) {		
	 
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);	
	}
	
	@Transactional(readOnly=true)
	public Page<Board> 글목록(Pageable pageable){		
		return boardRepository.findAll(pageable);
	}
	
	@Transactional(readOnly=true)
	public Board 글상세보기(int id){
		return boardRepository.findById(id)
				.orElseThrow(()->{
			 return new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없습니다!");
		});
	}
     
	@Transactional
	public void 글삭제하기(int id) {
		System.out.println(id);
		boardRepository.deleteById(id);	
	}

	@Transactional
	public void 글수정하기(int id, Board requestboard) {
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					 return new IllegalArgumentException("글 찾기 실패: 아이디를 찾을 수 없습니다!");
				});//영속화 완료
		board.setTitle(requestboard.getTitle());
		board.setContent(requestboard.getContent());
		//해당함수로 종료시(Service가 종료될때) 트랜젝션이 종료됩니다.
		//이때 더티채킹이 일어나고 DB쪽으로 자동 업데이트가 flush됨
	}

	@Transactional 
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
		
	//상기 작성된 댓글구성을 저장 
	int result= replyRepository.mSave(
			replySaveRequestDto.getUserid(),
			replySaveRequestDto.getBoardId(),
			replySaveRequestDto.getContent()
			); 
		System.out.println("Board Service:"+result);
		/*
		//댓글구성
		User user =userRepository.findById(replySaveRequestDto.getUserid())
				.orElseThrow(()->{
					 return new IllegalArgumentException("댓글 쓰기 실패: 사용자 Id를 찾을 수 없습니다!");
				});//영속화 완료
		
		Board board=boardRepository.findById(replySaveRequestDto.getBoardId())
				.orElseThrow(()->{
					 return new IllegalArgumentException("댓글 쓰기 실패: 게시글 Id를 찾을 수 없습니다!");
				});//영속화 완료
		
		//댓글 구성
		Reply reply = Reply.builder() 
				.user(user) 
				.board(board)
		        .content(replySaveRequestDto.getContent()) 
		        .build();
		//상기 작성된 댓글구성을 저장 
		replyRepository.save(reply); 
		*/
		
		/*
		 * Reply reply = new Reply(); reply.update(user, board,
		 * replySaveRequestDto.getContent());
		 */	
		
	}

	@Transactional
	public void 댓글삭제(int replyId) {
		//System.out.println(replyId);
		replyRepository.deleteById(replyId);		
	}
	
	
	

}
