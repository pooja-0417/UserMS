package com.orderManagement.user.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="wishlist")
public class Wishlist {
	
	
	private Integer buyerId;
	private Integer prodId;
	

}
