package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

@NoArgsConstructor
@Setter @Getter
public class M {
	@Id
	private String id;
	private String name;
	private String role;
	
	@OneToMany(mappedBy="m", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<B> bList = new ArrayList<B>(); //m객체가 부모다 
	
}
