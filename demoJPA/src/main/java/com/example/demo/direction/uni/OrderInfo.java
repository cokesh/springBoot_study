package com.example.demo.direction.uni;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
//@Table(name="order_info_jpa")
@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class OrderInfo {
	@Id
	@Column(name="order_info_no")
	private Long orderNo;
	private Date orderDt;
	private String orderId;
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "order_line_no")
	private List<OrderLine> lines; // 자료구조인 List 자료형을 맵핑할때는 ~ToMany로끝나야한다.
}
