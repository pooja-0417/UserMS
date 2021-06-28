package com.orderManagement.user.repository;

import org.springframework.data.repository.CrudRepository;

import com.orderManagement.user.entity.Wishlist;
import com.orderManagement.user.utility.CustomPK;


public interface WishlistRepository extends CrudRepository<Wishlist,CustomPK> {

}