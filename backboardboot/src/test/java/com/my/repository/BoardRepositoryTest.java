package com.my.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.my.dto.Board;

@SpringBootTest
class BoardRepositoryTest {
	@Autowired
	private BoardRepository repository;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	void testFindByIdValid() {
		Long boardNo = 1L;
		//게시글번호의 게시글을 조회한다
		Optional<Board> optB1 = repository.findById(boardNo);
		assert(optB1.isPresent());
	}
	
	@Test
	void tetFindByIdInvalid() {
		Long boardNo = 1L;
		Optional <Board> optB1 =repository.findById(boardNo);
		assertFalse(optB1.isPresent());
	}

	@Test 
	//	@Transactional
	void testWrite() {
		Board b = new Board();
		b.setBoardTitle("test title2");
		b.setBoardContent("test board content2");
		b.setBoardId("id2");
		b.setBoardParentNo(0L);
		repository.save(b); // transcational 어노테이션으로 인해 실제 디비에는 반영이 되지 않는다. -> 롤백이 되기 때문에

	}
	@Test
	void testReply() {
		Board b = new Board();

		b.setBoardParentNo(1L);
		b.setBoardTitle("1_re_title_");
		b.setBoardContent("1_re_content");
		b.setBoardId("id2");
		repository.save(b);
	}

	@Test
	void testModify() {
		Optional<Board>optB = repository.findById(4L);//b변수에 DB의 4번글정보 담겨있음
		optB.ifPresent((b)->{//보드타입의 객체 b 찾아오기
			b.setBoardContent("글4내용수정");//기존내용 놔두고 바뀐내용의 멤버변수만 set
			repository.save(b);//자료가 없으면 insert, 있으면 update하는 SQL구문 생성
		});
	}

	@Test
	void testUpdateViewCount() {
		Optional<Board>optB = repository.findById(4L);
		optB.ifPresent((b)->{//보드타입의 객체 b 찾아오기
			
			int oldViewCount = b.getBoardViewcount();//기존내용 놔두고 바뀐내용의 멤버변수만 set
			int newViewCount = oldViewCount + 1;
			b.setBoardViewcount(newViewCount);
			repository.save(b);//자료가 없으면 insert, 있으면 update하는 SQL구문 생성
			
			int expectedNewViewCount = newViewCount;
			assertEquals(expectedNewViewCount, repository.findById(4L).get().getBoardViewcount());
		});
	}

	@Test
	void testDelete() {
		Long boardNo = 5L;
		repository.deleteReply(boardNo);
		repository.deleteById(boardNo);
	}
	
	@Test
	void testFindAllPage() {
		int currentPage = 1;
		Pageable pageable = PageRequest.of(currentPage-1, 3);//, Direction.ASC
		List<Board> list = repository.findAll(pageable);
		list.forEach((b)-> {
			logger.error(b.toString());
		});
	}
	
	@Test
	void testFindByPage() {
		int currentPage = 1;
		int cntPerPage = 3;
		int endRow = currentPage * cntPerPage;
		int startRow = endRow - cntPerPage +1;
		List<Board> list = repository.findByPage(startRow, endRow);
		list.forEach((b)->{
			logger.error(b.toString());
		});
	}
	
	@Test
	void testSearchBoard() {
		String word = "test";
		int currentPage = 1;
		int cntPerPage = 3;
		int endRow = currentPage * cntPerPage;
		int startRow = endRow - cntPerPage +1;
		
		List<Board> list = repository.selectByWord(word, startRow, endRow);
		list.forEach((b)->{
			logger.error(b.toString());
		});
	}
	
	@Test
	void testSelectCount() {
		String word = "test";
		int cnt = repository.selectCount(word);
		int expectedCnt = 2;
		assertEquals(expectedCnt, cnt);
	}
}
