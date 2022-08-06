package com.my.exception;

public class FindException extends Exception {
    public FindException() {

        System.out.println("상품이 없습니다.");
    }
    public FindException(String message) {
        super(message);
    }
}
