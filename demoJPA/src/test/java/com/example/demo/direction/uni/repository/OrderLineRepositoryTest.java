package com.example.demo.direction.uni.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.direction.uni.OrderLine;
import com.example.demo.direction.uni.Product;
@SpringBootTest
class OrderLineRepositoryTest {
	
	@Autowired
	private OrderLineRepository repository;
	
	@Test
	void testInsert() {
		Long orderNo = 3L;
		String prodNo = "C0002";
		int orderQuantity = 3;
		OrderLine line = new OrderLine();
		line.setOrderNo(orderNo);
		
		Product p = new Product();
		p.setProdNo(prodNo);
		
		line.setOrderQuantity(orderQuantity);
		
	}
	
	@Test
	void testFindById() {
		Long orderNo = 1L;
		Optional<OrderLine> optLine = repository.findById(orderNo);
		assertTrue(optLine.isPresent());
		OrderLine line = optLine.get();
		
		Product p = line.getOrderP();
		String expectedProdName = "C1";
		int expectedProdPrice = 1000;
		
		assertEquals(expectedProdName, p.getProdName());
		assertEquals(expectedProdPrice, p.getProdPrice());
	}

}
