package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.A;


@SpringBootTest
class ARepositoryTest {
	@Autowired
	private ARepository repository;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
//	@Transactional
	void testSave() { //save() 실행 시 이미 자료가 있으면 update가 될 것이고, 자료가 없으면 생성될 것
		A a = new A();
		a.setA1("1"); 
		a.setA2(new BigDecimal(1.0));
		a.setA4("a4_1");
		repository.save(a); // JPA의 find() 메서드 호출 - Hibernate : select a0_.a1_c as a1_0_0_, a0_.a2_c as a2_0_0_, a0_.a3 as a3_0_0_, a0_.a4_c as a4_0_0_ from a_tbl a0_ where a0_.a1_c=?
							// 1차 캐시에 엔터티 객체가 있거나 없는 경우 (1차는 없음) - 없는 경우 INSERT 구문 실행하는 persist() 호출
							// JPA의 persist() 호출 - Hibernate : insert into a_tbl (a2_c, a3, a4_c, a1_c) values (?, ?, ?, ?)
		
		A aa = new A();
		aa.setA1("1"); // 위와 다른 객체지만 @Id 붙어있는 멤버변수값 위와 같음
		aa.setA2(new BigDecimal(2));
		aa.setA4("a4_2");
		repository.save(aa); // JPA의 find() 메서드 호출 Hibernate : select a0_.a1_c as a1_0_0_, a0_.a2_c as a2_0_0_, a0_.a3 as a3_0_0_, a0_.a4_c as a4_0_0_ from a_tbl a0_ where a0_.a1_c=?
							 // 1차 캐시에 엔터티 객체가 있거나 없는 경우 (2차는 있음) - Update 구문 실행하는 set() 호출 
							 // 엔터티 객체 set() 메서드 호출 - Hibernate : update a_tbl set a2_c=?, a3=?, a4_c=? where a1_c=?
		A aaa = new A();
		aaa.setA1("1"); // 위와 다른 객체지만 @Id 붙어있는 멤버변수값 위와 같음
		aaa.setA2(new BigDecimal(2));
		aaa.setA4("a4_3");
		repository.save(aaa);
	}
	
	@Test
	void testFindById() {
		Optional<A> optA = repository.findById("1"); // spring 의 pathVariable의 Optional과 동일
		assertTrue(optA.isPresent());
		String expectedA4 = "a4_2";
		A a = optA.get();
		assertEquals(expectedA4, a.getA4());
	}
	@Test
	void testDeleteById() {
		repository.deleteById("1");
	}
	
	@Test
	void testFindAll() {
		Iterable<A> list = repository.findAll();
		logger.error(list.toString());
	}
	
	@Test
	void testFindByA4() {
		List<A> list = repository.findByA4("a4_3");
		logger.error(list.toString());
	}
	
	@Test
	void testCount() {
		long cnt = repository.count();
		logger.error("총행수는 : " + cnt);
	}
	
	@Test
	void testFindByA4Like() {
		List<A> list = repository.findByA4Like("%a%");
		logger.error(list.toString());
	}
	
	
}