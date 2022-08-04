package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter @Getter
public class B {
	@Id  //Primary Key (엔티티의 id를 부여)
	@GeneratedValue //자동으로 seq번호 부여
	private int seq;
	private String title;
	
	@ManyToOne
	@JoinColumn(name="m_id", nullable=false)
	private M m;
	
	public void setM(M m) {
		this.m = m;
		m.getBList().add(this);
	}
}
