package com.orderManagement.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.orderManagement.user.dto.BuyerDTO;
import com.orderManagement.user.dto.CartDTO;
import com.orderManagement.user.dto.SellerDTO;
import com.orderManagement.user.entity.Buyer;
import com.orderManagement.user.entity.Cart;
import com.orderManagement.user.entity.Seller;
import com.orderManagement.user.entity.Wishlist;
import com.orderManagement.user.exception.UserMsException;
import com.orderManagement.user.repository.BuyerRepository;
import com.orderManagement.user.repository.CartRepository;
import com.orderManagement.user.repository.SellerRepository;
import com.orderManagement.user.repository.WishlistRepository;
import com.orderManagement.user.utility.CustomPK;
import com.orderManagement.user.validator.UserValidator;

@Service(value = "userService")
@Transactional
@PropertySource("classpath:messages.properties")
public class UserServiceImpl implements UserService{
	
	
	
	@Autowired
	private BuyerRepository buyerRepository;
	
	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private WishlistRepository wishlistRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Override
	public Integer buyerRegistration(BuyerDTO buyerDTO) throws UserMsException {
		
		UserValidator.validateBuyer(buyerDTO);
		Buyer buy = buyerRepository.findByPhoneNo(buyerDTO.getPhoneNo());
		if(buy != null)
			throw new UserMsException("Service.BUYER_ALREADY_PRESENT");
		
		
		buy = new Buyer();
		buy.setEmail(buyerDTO.getEmail());
		buy.setName(buyerDTO.getName());
		buy.setPhoneNo(buyerDTO.getPhoneNo());
		buy.setPassword(buyerDTO.getPassword());
		buy.setIsActive("Inactive");
		buy.setIsPrivileged('N');
		buy.setRewardPoints(0);
		buyerRepository.save(buy);
		return buy.getBuyerId();
	}

	@Override
	public Integer sellerRegistration(SellerDTO sellerDTO) throws UserMsException {
		UserValidator.validateSeller(sellerDTO);
		Seller seller = sellerRepository.findByPhoneNo(sellerDTO.getPhoneNo());
		if(seller != null)
			throw new UserMsException("Service.SELLER_ALREADY_PRESENT");
		
		seller = new Seller();
		seller.setEmail(sellerDTO.getEmail());
		seller.setName(sellerDTO.getName());
		seller.setPassword(sellerDTO.getPassword());
		seller.setIsActive("Inactive");
		seller.setPhoneNo(sellerDTO.getPhoneNo());
		sellerRepository.save(seller);
		return seller.getSellerId();
	}

	@Override
	public String buyerLogin(String email, String password) throws UserMsException {
		
		if(!UserValidator.validateEmail(email))
			throw new UserMsException("Service.INVALID_EMAIL");
		Buyer buyer = buyerRepository.findByEmail(email);
		if(buyer == null)
			throw new UserMsException("Service.INVALID_CREDENTIALS");
		if(!buyer.getPassword().equals(password))
			throw new UserMsException("Service.INVALID_CREDENTIALS");
		
		buyer.setIsActive("Active");
		buyerRepository.save(buyer);
		return "Login Success";
	}

	@Override
	public String sellerLogin(String email, String password) throws UserMsException {

		if(!UserValidator.validateEmail(email))
			throw new UserMsException("Service.INVALID_EMAIL");
		Seller seller = sellerRepository.findByEmail(email);
		if(seller == null)
			throw new UserMsException("Service.INVALID_CREDENTIALS");
		if(!seller.getPassword().equals(password))
			throw new UserMsException("Service.INVALID_CREDENTIALS");
		
		seller.setIsActive("Active");
		sellerRepository.save(seller);
		return "Login Success";
	}

	@Override
	public String deleteBuyer(Integer id) throws UserMsException{
		Optional<Buyer> buyerOpt = buyerRepository.findById(id);
		if(buyerOpt.isEmpty())
			throw new UserMsException("Service.NO_BUYER_FOUND");
		Buyer buyer = buyerRepository.findByBuyerId(id);
		buyerRepository.delete(buyer);
		return "Account successfully deleted";
	}

	@Override
	public String deleteSeller(Integer id) throws UserMsException {
		Optional<Buyer> buyer = buyerRepository.findById(id);
		if(buyer.isEmpty())
			throw new UserMsException("Service.NO_BUYER_FOUND");
		Seller seller = sellerRepository.findBySellerId(id);
		sellerRepository.delete(seller);
		return "Account successfully deleted";
	}

	@Override
	public String wishlistService(Integer prodId, Integer buyerId) throws UserMsException {
		Optional<Buyer> buyer = buyerRepository.findById(buyerId);
		if(buyer.isEmpty())
			throw new UserMsException("Service.NO_BUYER_FOUND");
		CustomPK cust = new CustomPK(prodId,buyerId);
		Wishlist w = new Wishlist();
		w.setCustomId(cust);
		wishlistRepository.save(w);
		return "Added Successfully to Wishlist";
	}
	
	@Override
	public String cartService(Integer prodId, Integer buyerId, Integer quantity) throws UserMsException {
		Optional<Buyer> buyer = buyerRepository.findById(buyerId);
		if(buyer.isEmpty())
			throw new UserMsException("Service.NO_BUYER_FOUND");
		CustomPK cust = new CustomPK(prodId,buyerId);
		Cart cart = new Cart();
		cart.setCustomPK(cust);
		cart.setQuantity(quantity);
		cartRepository.save(cart);
		return "Added Successfully to Cart";
	}

	@Override
	public List<CartDTO> getCartProducts(Integer id) throws UserMsException {
		
		List<Cart> list = cartRepository.findByCustomPKBuyerId(id);
		if(list.isEmpty())
			throw new UserMsException("Service.NO_ITEM_IN_CART");
		List<CartDTO> li = new ArrayList<>();
		
		for(Cart cart : list){
			CartDTO cartDTO = new CartDTO();
			cartDTO.setBuyerId(cart.getCustomPK().getBuyerId());
			cartDTO.setProdId(cart.getCustomPK().getProdId());
			cartDTO.setQuantity(cart.getQuantity());
			li.add(cartDTO);
		}
		return li;
	}

	@Override
	public String removeFromCart(Integer buyerId, Integer prodId) throws UserMsException {
		
		Cart cart = cartRepository.findByCustomPKBuyerIdAndCustomPKProdId(buyerId, prodId);
		if(cart==null)
			throw new UserMsException("Service.NO_ITEM_IN_CART");
		cartRepository.deleteByCustomPKBuyerIdAndCustomPKProdId(buyerId, prodId);
		return "The product was deleted successfully";
	}

	@Override
	public String updateRewardPoint(Integer buyerId, Integer rewPoints) throws UserMsException {
		
		Buyer buyer = buyerRepository.findByBuyerId(buyerId);
		if(buyer==null)
			throw new UserMsException("Service.NO_BUYER_FOUND");
		
		buyer.setRewardPoints(rewPoints);
		buyerRepository.save(buyer);
		return "Reward Points Updated for buyer Id : "+ buyer.getBuyerId();
	}


}

