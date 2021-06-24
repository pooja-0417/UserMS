package com.orderManagement.user.repository;

import org.springframework.data.repository.CrudRepository;

import com.orderManagement.user.entity.Buyer;

public interface BuyerRepository extends CrudRepository<Buyer, Integer>{

}
