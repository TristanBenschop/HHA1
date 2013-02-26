package edu.avans.hartigehap.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.avans.hartigehap.domain.Restaurant;

public interface RestaurantService {
	public List<Restaurant> findAll();
	public Restaurant findById(String name);
	public Restaurant save(Restaurant restaurant);
	public void delete(String name);
	public Page<Restaurant> findAllByPage(Pageable pageable);
		
	public Restaurant fetchWarmedUp(String restaurantName);
	public Restaurant createHartigeHapWithInventory();
	public Restaurant createPittigePannekoekWithInventory();
	public Restaurant createHmmmBurgerWithInventory();
}