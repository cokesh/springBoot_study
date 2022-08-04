package com.my.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.dto.Board;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;


@SpringBootTest
public class BoardRepositoryTest {

	@Autowired
	private BoardRepository repository;
	@Test
	public void testSelectByPage() throws FindException {
		int currentPage = 1;
		int cntPerPage = 3;
		int expectedSize = 3; 
		int []expectedBoardNoArr = {3, 7, 1};

		List<Board> list = repository.selectByPage(currentPage, cntPerPage);
		assertNotNull(list);
		assertEquals(expectedSize, list.size());
		for ( int i = 0; i < list.size(); i++ ) {
			assertEquals(expectedBoardNoArr[i], list.get(i).getBoardNo());
		}
	}

	@Test
	public void testSelectCount() throws FindException {
		int expectedCnt = 8;
		int cnt = repository.selectCount();
		assertEquals(expectedCnt, cnt);
	}

	@Test
	public void testSelectCount2() throws FindException {
		int expectedCnt = 3;
		int cnt = repository.selectCount("1번");
		assertEquals(expectedCnt, cnt);
	}

	@Test
	public void testSelectByWord() throws FindException {
		int expectedSize = 3;
		int currentPage = 1;
		int cntPerPage = 3;
		List list = repository.selectByWord("1번", 1, 3);
		assertEquals(expectedSize, list.size());
	}

	@Test
	public void testSelectByBoardNo() throws FindException {
		int boardNo = 1;
		String boardTitle = "1번글"; // sql 의 결과와 비교하여 테스트를 진행
		Board board = repository.selectByBoardNo(boardNo);
		assertNotNull(board);
		assertEquals(boardTitle, board.getBoardTitle());
	}

	@Test
	public void testUpdate() throws ModifyException, FindException {
		int boardNo = 1;
		Board board1 = repository.selectByBoardNo(boardNo);
		assertNotNull(board1);

		int expectedViewcount = board1.getBoardViewcount()+1; // 예상 조회수 : 조회수 1증가 전의 글 조회수 +1

		// 조회수 1증가를 검증하기위한 작업
		Board board = new Board();
		board.setBoardNo(boardNo);
		board.setBoardViewcount(-1); 
		repository.update(board); // 조회수 증가

		Board board2 = repository.selectByBoardNo(boardNo); // 조회수 1증가후의 글 조회수 
		assertEquals(expectedViewcount, board2.getBoardViewcount());

	}

	@Test
	public void testUpdateContent() throws FindException, ModifyException {
		int boardNo = 1;
		Board b1 = repository.selectByBoardNo(boardNo);
		assertNotNull(b1);
		String beforeContent = b1.getBoardContent();
		int beforeViewcount = b1.getBoardViewcount();
		String expectedContent = beforeContent +"a";
		Board b = new Board();
		b.setBoardNo(boardNo);
		b.setBoardContent(expectedContent);
		repository.update(b);

		Board b2 = repository.selectByBoardNo(boardNo);
		assertNotEquals(beforeContent, b2.getBoardContent()); // 이전내용과 수정된 내용이 다를것이다라고 예측
		assertEquals(expectedContent, b2.getBoardContent());
		assertEquals(beforeViewcount, b2.getBoardViewcount());
	}
	
//	@Test(expected = FindException.class)
	public void testDelete() throws RemoveException, FindException {
		int boardNo = 3;
		repository.delete(boardNo);
		repository.selectByBoardNo(boardNo); // FindException() 이 발생함
		
		assertThrows(FindException.class, () -> { //첫번째 인자로는 발생가능 익셉션선언, 두번째인자에는 람다표현식사용
			repository.selectByBoardNo(boardNo);
		});
	}
	
	@Test
	public void testInsertReply() throws AddException, FindException {
		int expectedBoardParentNo = 8;
		String expectedBoardTitle = "새글답";
		String expectedBoardContent = "새글답_내용";
		String expectedBoardId = "id1";
		
		Board b = new Board();
		b.setBoardParentNo(expectedBoardParentNo);
		b.setBoardTitle(expectedBoardTitle);
		b.setBoardContent(expectedBoardContent);
		b.setBoardId(expectedBoardId);
		
		repository.insert(b); // insert 하게되면 selectKey에 의해서 boardNo가 설정이 됨
		
		int boardNo = b.getBoardNo();
		assertNotEquals(0, boardNo);
		
		Board b1 = repository.selectByBoardNo(boardNo);
		assertNotNull(b1); // 잘검색이 되는지 확인
		assertEquals(expectedBoardTitle, b1.getBoardTitle());
		assertEquals(expectedBoardContent, b1.getBoardContent());
		assertEquals(expectedBoardId, b1.getBoardId());
		
	}

	
	@Test
	public void testInsert() throws AddException, FindException {
		
		String expectedBoardTitle = "새글";
		String expectedContent = "새글내용";
		String expectedId = "id1";
		
		Board b = new Board();
		b.setBoardTitle("새글");
		b.setBoardContent("새글내용");
		b.setBoardId("id1");
		
		repository.insert(b); // insert 하게되면 selectKey에 의해서 boardNo가 설정이 됨
		
		int boardNo = b.getBoardNo();
		assertNotEquals(0, boardNo);
		
		Board b1 = repository.selectByBoardNo(boardNo);
		assertNotNull(b1); // 잘검색이 되는지 확인
		assertEquals(expectedBoardTitle, b1.getBoardTitle());
		assertEquals(expectedContent, b1.getBoardContent());
		assertEquals(expectedId, b1.getBoardId());
		System.out.println(b1);
		
	}
}
