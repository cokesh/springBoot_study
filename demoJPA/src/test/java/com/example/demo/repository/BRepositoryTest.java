package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.entity.B;
import com.example.demo.entity.M;


@SpringBootTest
class BRepositoryTest {
	
	@Autowired
	private BRepository brepo;
	
	@Autowired
	private MRepository mrepo;
	
	@Test
	void saveTest() {
		M m = new M();
		m.setId("hanmirae");
		m.setName("한미래");
		m.setRole("데빌");
		//mrepo.save(m);
		
		M m1 = new M();
		m1.setId("hanmirae2");
		m1.setName("한미래2");
		m1.setRole("데빌2");
		//mrepo.save(m1); cascadeType을 사용함으로써 영속성이 전이되어 관리를 함
		// 부모 엔티티를 저장할 때 자식 엔티티도 같이 저장할 수 있고 부모 엔티티를 삭제할 때 자식 엔티티도 삭제할 수 있다.
		
		for (int i = 1; i < 4; i++ ) {
			B b = new B();
			b.setTitle("둘리말고헐크"+ i);
			b.setM(m);
//			brepo.save(b);
		}
		mrepo.save(m);
		
		for (int i = 1; i < 4; i++ ) {
			B b = new B();
			b.setTitle("헐크말고토르" + i);
			b.setM(m1);
//			brepo.save(b);
		}
		mrepo.save(m1);
	}
	
	@Test
	void testManyToOneSelect() {
		B b = brepo.findById(1).get();
		System.out.println(b.getSeq() + "번 글 정보");
		System.out.println(b.getTitle());
		System.out.println(b.getM().getName());
		System.out.println(b.getM().getRole());
	}
	
	

}
