package com.my.dto;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
@Component

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@EqualsAndHashCode(of = {"boardNo"})
@ToString
//@Data
public class Board {
	/*
	 * BOARD_NO 				NOT NULL NUMBER
	 * BOARD_PARENT_NO			NUMBER
	 * BOARD_TITLE				VARCHAR2(30)
	 * BOARD_CONTENT			VARCHAR2(100)
	 * BOARD_DT					DATE
	 * BOARD_ID					VARCHAR2(5)
	 * BOARD_VIEWCOUNT			NUMBER(4)
	 */
	private int level; //글레벨 : 1-원글, 2-답글, 3-답답글, 4-답답답글
	private int boardNo; //게시글번호 
	private int boardParentNo;
	
	private String boardTitle;
	private String boardContent;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date boardDt;
	
	@NonNull
	private String boardId; //? private Customer boardC;
	private int boardViewcount;
}