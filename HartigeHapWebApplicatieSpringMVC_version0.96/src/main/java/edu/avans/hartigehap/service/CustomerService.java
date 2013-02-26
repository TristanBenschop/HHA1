package edu.avans.hartigehap.service;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.avans.hartigehap.domain.*;


public interface CustomerService {
	public List<Customer> findAll();
	public Customer findById(Long id);	
	public Customer findByFirstNameAndLastName(String firstName, String lastName);
	public List<Customer> findCustomersForRestaurant(Restaurant restaurant);
	public Page<Customer> findAllByPage(Pageable pageable);	
	public Page<Customer> findCustomersForRestaurantByPage(Restaurant restaurant, Pageable pageable);
	public Customer save(Customer customer);
	public void delete(Long id);
}
