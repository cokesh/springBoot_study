package com.my.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@EqualsAndHashCode(of= {"boardNo"})//boardNo가 같으면 같은 객체
@ToString //권장X
//@Data //권장X

@Entity
@Table(name= "board_jpa")
@SequenceGenerator(name = "boardjpa_seq_generator",
					sequenceName= "board_jpa_seq",
					initialValue = 1,
					allocationSize = 1
					)//오라클DB에 시퀀스 생성

@DynamicInsert
//
//동적 SQL구문을 생성하는 @
//insert대상 객체에서 멤버변수가 null인가 아닌가로 판단하여 ? 생성
//board_parent_no는 (integer)참조형 데이터 타입이므로 null값이 안될 수 X  => dynamicinsert에서 체크되지 않음
@DynamicUpdate
//dynamicupdate에서는 기존 내용과 다른 것으로 판단 <==>dynamicInsert의 기준은 null
public class Board {
	@Transient//실제 테이블에는 존재X
	private int level; //글레벨 : 1-원글, 2-답글, 3-답답글, 4-답답답글
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "boardjpa_seq_generator")//만들어진 값을 사용하겠다 선언
	@Column(name = "board_no")
	private Long boardNo; //게시글번호
	
	@Column(name="board_parent_no")
	@ColumnDefault(value = "-1")
	private Long boardParentNo;//기본형은 null을 가질 수 없기 때문에 dynamicinsert효과 X
	
	@Column(name="board_title")
	private String boardTitle;
	
	@Column(name="board_content")
	private String boardContent;  
	
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul") 
	@Column(name="board_dt")
	@ColumnDefault(value = "SYSDATE")
	private Date boardDt;
	
	@Column(name="board_id")
	private String boardId; //? private Customer boardC; //게시글 작성자의 정보를 알고싶다면 Has-a관계로 선언할 것
	
	@Column(name="board_viewcount")
	private Integer boardViewcount;
}