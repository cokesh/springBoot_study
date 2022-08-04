package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.A;

public interface ARepository extends CrudRepository<A, String> {
	/**
	 쿼리메소드 findBy queryBy countBy
	 */
	List<A> findByA4(String a4);
	
	List<A> findByA4Like(String word);
	
}
