package com.my.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.dto.Board;
import com.my.dto.PageBean;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.repository.BoardRepository;

@Service
public class BoardService {
	private static final int CNT_PER_PAGE = 3 ; // 페이지별 보여줄 목록 수 
	@Autowired
	private BoardRepository repository;

	/**
	 * 페이지별 게시글 목록과 페이지 그룹정보를 반환한다 
	 * @param currentPage 검색할 페이지
	 * @return 
	 * @throws FindException
	 */
	public PageBean<Board> boardList(int currentPage) throws FindException {

		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1; 
		List<Board> list = repository.findByPage(startRow,endRow);
		long totalCnt = repository.count(); // 총 행수를 얻어오는 메서드
		int cntPerPageGroup = 2;

		PageBean<Board> pb = new PageBean<>(list, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb;
	}

	/**
	 * 검색어를 이용한 게시글 검색 목록과 페이지 그룹정보를 반환한다
	 * @param word 검색어
	 * @param currentPage 검색할 페이지
	 * @return
	 * @throws FindException
	 */
	public PageBean<Board> searchBoard(String word, int currentPage) throws FindException{
		int totalCnt = repository.selectCount(word);
		int cntPerPageGroup = 2;
		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1;
		List<Board> list = repository.selectByWord(word, startRow, endRow);
		PageBean<Board> pb = new PageBean<>(list, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb;
	}

	/**
	 * 게시글 번호의 조회수를 1 증가한다
	 * 게시글 번호의 게시글을 반환한다
	 * @param boardNo 게시글 번호
	 * @return
	 * @throws FindException
	 */
	public Board viewBoard(long boardNo) throws FindException { // 게시물 보기 작업
		// 조회수를 1 증가한다
		// 인터페이스에서 제공하는 save메서드를 호출하는 것이기 때문에 
		// 우리 나름의 규약을 만들 수 없음 b.setBoardViewcount(-1)로 설정하는 등
		Optional<Board> optB = repository.findById(boardNo); // 글번호에 해당하는 글을 조회
		if(optB.isPresent()) {
			Board b = optB.get(); // optB값을 꺼내오고
			b.setBoardViewcount(b.getBoardViewcount() +1); // 꺼내온 viewcount에 +1을 더해서 다시 셋팅해 줌
			repository.save(b); // 해당자료가 존재하면 변경, 존재하지 않으면 변경하지 않음
		}else {
			throw new FindException("게시글이 없습니다");
		}		

		// 게시글 번호의 게시글을 조회
		//			Board b1 = repository.selectByBoardNo(boardNo); // 글번호에 해당하는 글을 조회
		Optional<Board> optB1 = repository.findById(boardNo); // 글번호에 해당하는 글을 조회
		// java.util의 Optional
		if(optB1.isPresent()) {
			Board b1 = optB1.get();
			return b1;
		}else { // Optional의 내용이 비었을 때
			throw new FindException("게시글이 없습니다");
		}			
	}

	/**
	 * 글쓰기
	 * @param repBoard
	 * @throws AddException
	 */
	public void writeBoard(Board board) throws AddException{
		//		board.setBoardParentNo(0); // 부모 글번호 무조건 0으로 설정해야 글쓰기가 가능함
		//		repository.insert(board);
		repository.save(board);
	}

	/**
	 * 답글쓰기
	 * @param board
	 * @throws AddException
	 */
	public void replyBoard(Board board) throws AddException{
		if(board.getBoardParentNo() == 0L) {
			throw new AddException("답글쓰기의 부모글번호가 없습니다");
		}
		repository.save(board);
	}

	/**
	 * 게시글 수정하기
	 * @param board
	 * @throws ModifyException
	 */
	public void modifyBoard(Board board) throws ModifyException {
		//		board.setBoardParentNo(0); // 부모 글 번호 0으로 설정해야 수정 가능
		//		repository.update(board);
		Optional<Board> optB = repository.findById(board.getBoardNo()); //boardNo가 PK이기 때문에 findById의 인자 BoardNo인 것
		if(!optB.isPresent()) {
			throw new ModifyException("글이 없습니다.");
		}else { // Content만 변경하고자 한다.
			Board b = optB.get(); // 기존 내용은 가져와서 그대로 사용하고
			b.setBoardTitle(board.getBoardTitle()); // 제목도 바꾸고자 할 때 
			b.setBoardContent(board.getBoardContent()); // content만 기존내용에서 새 내용으로 바꾸겠다.
			repository.save(b);
		}
		// 위의 if(!optB.isPresent())랑 같은 것
		//				optB.ifPresent((b) -> {
		//				}); // optB.ifPresent() : 인자가 컨수머 유형?!을 요구하고 람다식 표현법 쓰면 됨
	}

	/**
	 * 답글, 게시글 삭제하기
	 * @param board
	 * @throws RemoveException 
	 * @throws ModifyException
	 */
	@Transactional // 현재 removeBoard 메서드에서 한개 이상의 DML구문이 실행되게끔 코드가 구현되어져 있기 때문에 한 개의 트랜잭션에서 관리가 되어져야 하는 것들이다.
	public void removeBoard(Long boardNo) throws RemoveException {
		// 해당 글 번호 삭제₩
		Optional<Board> optB = repository.findById(boardNo); //boardNo가 PK이기 때문에 findById의 인자 BoardNo인 것
		if(!optB.isPresent()) {
			throw new RemoveException("글이 없습니다.");
		} else {
			repository.deleteReply(boardNo);
			repository.findById(boardNo); // throw처리해줘야 함
			repository.deleteById(boardNo);
			// 이 글번호에 해당하는 답글들을 삭제해야함
			// 답글의 답글도 삭제해야함
			// 일반 JPA구문으로 하기 어려움
			// JPQL 혹은 네이티브 쿼리 사용 해 주어야 함
		}
	}

}