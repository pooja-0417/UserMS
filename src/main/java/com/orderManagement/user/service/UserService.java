package com.orderManagement.user.service;

import java.util.List;
import com.orderManagement.user.dto.BuyerDTO;
import com.orderManagement.user.dto.CartDTO;
import com.orderManagement.user.dto.SellerDTO;
import com.orderManagement.user.exception.UserMsException;

public interface UserService {

	public Integer buyerRegistration(BuyerDTO buyerDTO) throws UserMsException;
	
	public Integer sellerRegistration(SellerDTO sellerDTO) throws UserMsException;
	
	public String buyerLogin(String email, String password) throws UserMsException;
	
	public String sellerLogin(String email, String password) throws UserMsException;
	
	public String deleteBuyer(Integer id) throws UserMsException;
	
	public String deleteSeller(Integer id) throws UserMsException;
	
	public String wishlistService(Integer prodId,Integer buyerId) throws UserMsException;
	
	public String cartService(Integer prodId, Integer buyerId, Integer quantity) throws UserMsException;
	
	public List<CartDTO> getCartProducts(Integer id) throws UserMsException;
	
	public String removeFromCart(Integer buyerId, Integer prodId) throws UserMsException;
	
	public String updateRewardPoint(Integer buyerId, Integer rewPoints) throws UserMsException;

}