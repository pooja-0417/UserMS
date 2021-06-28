package com.orderManagement.user.repository;

import org.springframework.data.repository.CrudRepository;

import com.orderManagement.user.entity.Buyer;

public interface BuyerRepository extends CrudRepository<Buyer, Integer>{
	
	public Buyer findByPhoneNo(Long phoneNo);
	
	public Buyer findByEmail(String email);
	
	public Buyer findByBuyerId(Integer id);


}
