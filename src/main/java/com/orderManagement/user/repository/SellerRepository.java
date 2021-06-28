package com.orderManagement.user.repository;

import org.springframework.data.repository.CrudRepository;

import com.orderManagement.user.entity.Seller;

public interface SellerRepository extends CrudRepository<Seller,Integer>{
	
	public Seller findByPhoneNo(Long phoneNo);
	
	public Seller findByEmail(String email);
	
	public Seller findBySellerId(Integer id);
}
