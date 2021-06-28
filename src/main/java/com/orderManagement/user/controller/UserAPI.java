package com.orderManagement.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.orderManagement.user.dto.BuyerDTO;
import com.orderManagement.user.dto.CartDTO;
import com.orderManagement.user.dto.ProductDTO;
import com.orderManagement.user.dto.SellerDTO;
import com.orderManagement.user.exception.UserMsException;
import com.orderManagement.user.service.UserService;


@RestController
@CrossOrigin
@RequestMapping(value="/userMS")
public class UserAPI {

	@Autowired
	private UserService userServiceNew;
	
 	@Autowired
 	RestTemplate template;
	
	@Autowired
	Environment environment;
	
	@PostMapping(value = "/buyer/register")
	public ResponseEntity<String> registerBuyer(@RequestBody BuyerDTO buyerDto){
		try {
			String s ="Buyer registered successfully with buyer Id : " + userServiceNew.buyerRegistration(buyerDto);
			return new ResponseEntity<>(s,HttpStatus.OK);
		}
		catch(UserMsException e){
			String s = environment.getProperty(e.getMessage());
			return new ResponseEntity<>(s,HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@PostMapping(value = "/seller/register")
	public ResponseEntity<String> registerSeller(@RequestBody SellerDTO sellerDto){
		try {
		String s ="Seller registered successfully with seller Id : "+ userServiceNew.sellerRegistration(sellerDto);
		return new ResponseEntity<>(s,HttpStatus.OK);
		}
		catch(UserMsException e){
			return new ResponseEntity<>(environment.getProperty(e.getMessage()),HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@PostMapping(value = "/buyer/login/{email}/{password}")
	public ResponseEntity<String> loginBuyer(@PathVariable String email, @PathVariable String password){
		try {
			String msg = userServiceNew.buyerLogin(email, password);
			return new ResponseEntity<>(msg,HttpStatus.OK);
		}
		catch(UserMsException e){
			return new ResponseEntity<String>(environment.getProperty(e.getMessage()),HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value = "/seller/login/{email}/{password}")
	public ResponseEntity<String> loginSeller(@PathVariable String email, @PathVariable String password){
		try {
			String msg = userServiceNew.sellerLogin(email, password);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		}
		catch(UserMsException e){
			return new ResponseEntity<String>(environment.getProperty(e.getMessage()),HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping(value = "/buyer/deactivate/{id}")
	public ResponseEntity<String> deleteBuyerAccount(@PathVariable Integer id) throws UserMsException{
		String msg;
		try {
			msg = userServiceNew.deleteBuyer(id);
			return new ResponseEntity<>(msg,HttpStatus.OK);
		} catch (UserMsException e) {
			return new ResponseEntity<String>(environment.getProperty(e.getMessage()),HttpStatus.NOT_FOUND);
		}
		
	}
	
	@DeleteMapping(value = "/seller/deactivate/{id}")
	public ResponseEntity<String> deleteSellerAccount(@PathVariable Integer id) throws UserMsException{
		String msg;
		try {
			msg = userServiceNew.deleteSeller(id);
			return new ResponseEntity<>(msg,HttpStatus.OK);
		} catch (UserMsException e) {
			return new ResponseEntity<String>(environment.getProperty(e.getMessage()),HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value = "/buyer/wishlist/add/{buyerId}/{prodId}")
	public ResponseEntity<String> addProductToWishlist(@PathVariable Integer buyerId, @PathVariable Integer prodId) throws UserMsException{
		try {
		ProductDTO product = template.getForObject("http://PRODUCTSERVICE"+"/productMS/getById/"+prodId, ProductDTO.class);
		String msg = userServiceNew.wishlistService(product.getProdId(), buyerId);
		return new ResponseEntity<>(msg,HttpStatus.ACCEPTED);
	}
		catch(Exception e){
			System.out.println(e);
			String newMsg = e.getMessage();
			if(e instanceof UserMsException) {
				newMsg = environment.getProperty(e.getMessage());
			}
			if(e.getMessage().equals("404 null"))
				newMsg = "There are no PRODUCTS for the given product ID";
			return new ResponseEntity<>(newMsg,HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value = "/buyer/cart/add/{buyerId}/{prodId}/{quantity}")
	public ResponseEntity<String> addProductToCart(@PathVariable Integer buyerId, @PathVariable Integer prodId, @PathVariable Integer quantity) throws UserMsException{
		try {
		ProductDTO product = template.getForObject("http://PRODUCTSERVICE"+"/productMS/getById/"+prodId, ProductDTO.class);
		System.out.println(product);
		System.out.println(product instanceof ProductDTO);
		String msg = userServiceNew.cartService(product.getProdId(), buyerId, quantity);
		return new ResponseEntity<>(msg,HttpStatus.ACCEPTED);
	}
		catch(Exception e){
			String newMsg = e.getMessage();
			if(e instanceof UserMsException) {
				newMsg = environment.getProperty(e.getMessage());
			}
			if(e.getMessage().equals("404 null"))
				newMsg = "There are no PRODUCTS for the given product ID";
			
			return new ResponseEntity<>(newMsg,HttpStatus.NOT_FOUND);
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping(value = "/buyer/cart/get/{buyerId}")
	public ResponseEntity<List<CartDTO>> getProductListFromCart(@PathVariable Integer buyerId) throws UserMsException{
		
		try {
			List<CartDTO> list = userServiceNew.getCartProducts(buyerId);
			return new ResponseEntity<>(list,HttpStatus.ACCEPTED);
		}
		catch(UserMsException e){
			String msg = environment.getProperty(e.getMessage());
			System.out.println(environment.getProperty(e.getMessage()));
			return new ResponseEntity(msg,HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value = "/buyer/cart/remove/{buyerId}/{prodId}")
	public ResponseEntity<String> removeFromCart(@PathVariable Integer buyerId,@PathVariable Integer prodId) throws UserMsException{
		try {
			String msg = userServiceNew.removeFromCart(buyerId, prodId);
			return new ResponseEntity<>(msg,HttpStatus.OK);
		}
		catch(UserMsException e){
			String msg = environment.getProperty(e.getMessage());
			return new ResponseEntity<>(msg,HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value = "/updateRewardPoints/{buyerId}/{rewPoints}")
	public ResponseEntity<String> updateRewardPoints(@PathVariable Integer buyerId, @PathVariable Integer rewPoints){
		try {
			String msg = userServiceNew.updateRewardPoint(buyerId, rewPoints);
			return new ResponseEntity<>(msg,HttpStatus.OK);
		}
		catch(Exception e){
			return new ResponseEntity<>(environment.getProperty(e.getMessage()),HttpStatus.NOT_FOUND);
		}
	}

}
