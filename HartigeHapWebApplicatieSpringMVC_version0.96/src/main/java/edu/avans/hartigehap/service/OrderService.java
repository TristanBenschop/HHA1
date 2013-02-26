package edu.avans.hartigehap.service;

import java.util.List;

import edu.avans.hartigehap.domain.*;

public interface OrderService {
	public Order findById(Long orderId);
	public List<Order> findSubmittedOrdersForRestaurant(Restaurant restaurant);	
	public List<Order> findPlannedOrdersForRestaurant(Restaurant restaurant);	
	public List<Order> findPreparedOrdersForRestaurant(Restaurant restaurant);
	public void planOrder(Order order) throws StateException;
	public void orderPrepared(Order order) throws StateException;
	public void orderServed(Order order) throws StateException;
	
}
