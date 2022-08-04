package com.example.demo.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// JPA 표준 어노테이션
@Entity	
@Table(name = "a_tbl") // 테이블 이름 지정
// lombok 어노테이션
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@ToString
public class A {
	@Id 
	@Column(name="a1_c", length=5)
	private String a1; // PK역할이 될 것 - not null로 제약조건 자동 생성
	
	@Column(name = "a2_c", precision = 5, scale = 2, nullable = true) 
	// 숫자 5자리 , 소숫점 이하 자리 수 2자리
	// nullable = false로 지정 시 Not NUll로 설정하는 것과 같은 효과
	private BigDecimal a2; // BigDecimal 타입으로 정의해야 오류가 안 남
	// a1,a2 테이블 생성
	
	// 엔티티가 테이블에 INSERT 되는 시점의 날짜 데이터를 자동 기록
	@CreationTimestamp   
	// 엔티티가 테이블에 UPDATE 되는 시점의 날짜데이터를 자동기록
//	@UpdateTimestamp // CreationTimestamp와 UpdateTimestamp 둘 중 하나만 사용해야 함
	private Date a3; // a3이 만들어지면서 자료형 자동 TimeStamp 됨
	
	//기본값 자동 설정 (hello 로)
	@ColumnDefault(value = "'hello'")
	@Column(name = "a4_c", length=10)
	private String a4;
	
	@Transient // 매핑에서 제외하고 싶을 때 사용
	private String a5;

}