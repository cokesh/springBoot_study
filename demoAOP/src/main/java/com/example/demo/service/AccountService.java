package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Account;
import com.example.demo.exception.ModifyException;
import com.example.demo.repository.AccountRepository;

@Service
public class AccountService {
	@Autowired //repo 를 자동주입받음
	private AccountRepository repository;
	
	/**
	 * 계좌를 만든다.
	 * @param a
	 */
	public void open(Account a) {
		repository.save(a);
	}
	
	/**
	 * 입금한다.
	 * @param no 계좌번호
	 * @param amount 입금액
	 */
	public void deposit(String no, int amount) throws ModifyException{
		Optional<Account> optA = repository.findById(no);
//		if(!optA.isPresent()) {
//			new IllegalArgumentException(no + "계좌가 없습니다.");
//		}  아래의 코드와 같은 코드임
		optA.orElseThrow(()->new ModifyException(no+"계좌가 없습니다")); // modify는 exception을 상속받은 예외이기 때문에 checkedException이다. 그래서 transaction이 자동 rollback이 되지않음. 그냥 commit 되버림
//		optA.orElseThrow(()->new IllegalArgumentException(no+"계좌가 없습니다")); // 계좌에 대한 행이 없다면 예외를 발생시켜
		Account a = optA.get();
		int aBalance = a.getAccountBalance(); //잔액을 확인하고 
		a.setAccountBalance(aBalance + amount ); // 더하여 업데이트 해준다.
		repository.save(a);
	}
	
	/**
	 * 출금
	 * @param no 계좌번호
	 * @param amount 출금액
	 * @throws ModifyException 
	 */
	public void withdraw(String no, int amount) throws ModifyException {
		Optional<Account> optA = repository.findById(no); // 자료를 찾아온다.
		Account a = optA.get();
		int aBalance = a.getAccountBalance(); // 해당계좌의 잔액을 확인한다.
		if(amount > aBalance ) { //출금할 잔액보다 계좌 잔액이 작다면
			throw new ModifyException("잔액이 부족합니다");
//			throw new IllegalArgumentException("잔액이 부족합니다");
		}
		a.setAccountBalance(aBalance - amount ); // 기존잔액에서 출금잔액을 뺀다.
		repository.save(a); // 내부에서 persist() 호출함 자료가 있으면 update/ 자료가 없으면 insert
		// 트랜잭션 작동시작 -> 정상처리된 경우 commit;
	}
	
	/**
	 * 계좌이체
	 * @param from 출금계좌번호
	 * @param to   입금계좌번호
	 * @param amount   이체금액
	 * @throws ModifyException
	 */
	@Transactional(rollbackFor = ModifyException.class)  // checkedexception이 발생이돼도 rollback을 해라!
	//(propagation = Propagation.REQUIRED) //기본적으로 명시를 안해줘도 required임  //계좌이체는 한 트랜잭션에서 처리되어야 함.자동커밋까지 해준다.
	public void transfer(String from, String to, int amount) throws ModifyException{
		withdraw(from, amount);
		deposit(to, amount);
	}
}