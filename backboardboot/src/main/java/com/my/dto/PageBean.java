package com.my.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@ToString
public class PageBean<T> { // 목록을 보여줄 자료형을 선언함(어떤 목록인가에 관한 제너릭을) 
	private List<T> list;
	private int currentPage; //현재페이지
	private int totalPage; // 총페이지
	private int startPage; // 시작페이지
	private int endPage; //끝페이지
	private int cntPerPageGroup = 2; // 하단부에 보여줄 페이지 갯수를 말한다. (자료가 많지 않은 관계로 2page설정)
	
	/**
	 * 
	 * @param list 페이지의 목록
	 * @param totalCnt 총 건수
	 * @param currentPage 검색할페이지
	 * @param cntPerPageGroup //페이지그룹별 보여줄 페이지수
	 * @param cntPerPage //한페이지당 보여줄 목록수
	 */
	public PageBean(List<T>list, long totalCnt, int currentPage, int cntPerPageGroup, int cntPerPage) {
		this.list = list;
		this.currentPage = currentPage;
		this.cntPerPageGroup = cntPerPageGroup;
		
		this.totalPage =  (int)Math.ceil((double)totalCnt/cntPerPage);
		this.endPage =
				   (int)(Math.ceil((double)currentPage/cntPerPageGroup)*cntPerPageGroup);
		this.startPage = this.endPage - cntPerPageGroup + 1;
		if(this.totalPage <this.endPage) {
			this.endPage = this.totalPage;
		}
	}
}


