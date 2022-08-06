package com.my.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.my.dto.Board;

public interface BoardRepository extends CrudRepository<Board, Long> {
	// 첫번째 인자 : Entity -> Board
	// 두번째 인자 : Entity의 @Id를 가진 변수의 자료형 -> Integer (int)
	
	// 페이지별 조회를 위한 메서드
	List<Board> findAll(org.springframework.data.domain.Pageable paging);
	@Query(value="SELECT\n"
			+ "		*\n"
			+ "		FROM (\n"
			+ "		SELECT rownum r, a.*\n"
			+ "		FROM (SELECT level,\n"
			+ "		board_no,\n"
			+ "		board_parent_no, board_title, board_content, board_id, board_dt,\n"
			+ "		board_viewcount\n"
			+ "		FROM board_jpa\n"
			+ "		START WITH board_parent_no = 0\n"
			+ "		CONNECT BY\n"
			+ "		PRIOR board_no = board_parent_no\n"
			+ "		ORDER SIBLINGS BY board_no DESC\n"
			+ "		) a\r\n"
			+ "		)\r\n"
			+ "		WHERE r BETWEEN ?1 AND ?2",
				nativeQuery=true)	
	List<Board> findByPage(int startRow, int endRow);
	
	//pageable -> 페이지 처리를 쉽게 할 수 있도록 도와줌
	// 삭제를 위한 네이티브 쿼리문 (JPA 문법으로는 글의 댓글, 대댓글 모두 삭제하기 어렵다)
	@Query(value = 	"DELETE FROM board_jpa\r\n"
			+ "		WHERE board_no IN ( SELECT board_no\r\n"
			+ "							FROM board_jpa\r\n"
			+ "							START WITH board_parent_no = ?1 \r\n"
			+ "							CONNECT BY PRIOR board_no = board_parent_no)"
			, nativeQuery = true) // 네이티브 쿼리문 설정하는 어노테이션
	void deleteReply(Long boardNo) ;
	
	@Query(value = "SELECT *\n"
			+ "		FROM (\n"
			+ "		SELECT rownum r, a.*\n"
			+ "		FROM (SELECT level,\n"
			+ "		board_no,\n"
			+ "		board_parent_no, board_title, board_content, board_id, board_dt,\n"
			+ "		board_viewcount\n"
			+ "		FROM board\n"
			+ "		WHERE board_title LIKE %?1% OR\n"
			+ "		board_id LIKE %?1% \n"
			+ "		START WITH board_parent_no = 0\n"
			+ "		CONNECT BY\n"
			+ "		PRIOR board_no = board_parent_no\n"
			+ "		ORDER SIBLINGS BY board_no DESC\n"
			+ "		) a\n"
			+ "		)\n"
			+ "		WHERE r BETWEEN ?2 AND ?3", nativeQuery = true)
	List<Board> selectByWord(String word, int startRow, int endRow);
	
	@Query(value="SELECT COUNT(*) FROM board_jpa WHERE board_title LIKE %?1% OR board_id LIKE %?1%", nativeQuery = true)
	int selectCount(String word);
	
	

}
