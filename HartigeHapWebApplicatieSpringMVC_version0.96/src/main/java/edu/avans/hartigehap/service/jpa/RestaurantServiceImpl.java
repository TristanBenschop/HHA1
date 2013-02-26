package edu.avans.hartigehap.service.jpa;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.repository.*;
import edu.avans.hartigehap.service.*;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;

@Service("restaurantService")
@Repository
@Transactional
public class RestaurantServiceImpl implements RestaurantService {
	final Logger logger = LoggerFactory.getLogger(RestaurantServiceImpl.class);
	public static final String hartigeHapRestaurantName = "HartigeHap";
	private static final String pittigePannekoekRestaurantName = "PittigePannekoek";
	private static final String hmmmBurgerRestaurantName = "HmmmBurger";
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private CustomerRepository customerRepository;
	
	@Transactional(readOnly=true)
	public List<Restaurant> findAll() {
		return Lists.newArrayList(restaurantRepository.findAll());
	}

	@Transactional(readOnly=true)
	public Restaurant findById(String restaurant) {
		return restaurantRepository.findOne(restaurant);
	}
	
	public Restaurant save(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}

	public void delete(String restaurant) {
		restaurantRepository.delete(restaurant);
	}

	@Transactional(readOnly=true)
	public Page<Restaurant> findAllByPage(Pageable pageable) {
		return restaurantRepository.findAll(pageable);
	}

	// to be able to follow associations outside the context of a transaction, prefetch the associated
	// entities by traversing the associations
	@Transactional(readOnly=true)
	public Restaurant fetchWarmedUp(String restaurantName) {
		Restaurant restaurant = restaurantRepository.findOne(restaurantName);
		restaurant.warmup();
		
		return restaurant;
	}

	

	public Restaurant createHartigeHapWithInventory() {
		Restaurant restaurant = new Restaurant(hartigeHapRestaurantName, "deHartigeHap.jpg");

		int NUMBER_OF_TABLES = 5;
		for(int i=0; i<NUMBER_OF_TABLES; i++) {
			DiningTable diningTable = new DiningTable();
			diningTable.setRestaurant(restaurant);
			restaurant.getDiningTables().add(diningTable);
		}


		FoodCategory foodCatLowFat = new FoodCategory("low fat");
		FoodCategory foodCatHighEnergy = new FoodCategory("high energy");
		FoodCategory foodCatVegy = new FoodCategory("vegatarian");
		FoodCategory foodCatItalian = new FoodCategory("italian");
		FoodCategory foodCatAsian = new FoodCategory("asian");
		FoodCategory foodCatAlcoholic = new FoodCategory("alcoholic");
		FoodCategory foodCatEnergizing = new FoodCategory("energizing");
		restaurant.getFoodCategories().add(foodCatLowFat); 
		restaurant.getFoodCategories().add(foodCatHighEnergy); 
		restaurant.getFoodCategories().add(foodCatVegy); 
		restaurant.getFoodCategories().add(foodCatItalian); 
		restaurant.getFoodCategories().add(foodCatAsian); 
		restaurant.getFoodCategories().add(foodCatAlcoholic); 
		restaurant.getFoodCategories().add(foodCatEnergizing); 

		Meal meal1 = new Meal("spaghetti", "spaghetti.jpg", 8, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatHighEnergy}));                
		Meal meal2 = new Meal("macaroni", "macaroni.jpg", 8, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatHighEnergy}));
		Meal meal3 = new Meal("canneloni", "canneloni.jpg", 9, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatHighEnergy}));
		Meal meal4 = new Meal("pizza", "pizza.jpg", 9, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatHighEnergy}));
		Meal meal5 = new Meal("carpaccio", "carpaccio.jpg", 7, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatLowFat}));
		Meal meal6 = new Meal("ravioli", "ravioli.jpg", 8, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatVegy, foodCatHighEnergy}));
		Drink d1 = new Drink("beer", "beer.jpg", 1, Arrays.asList(new FoodCategory[]{foodCatAlcoholic}));
		Drink d2 = new Drink("coffee", "coffee.jpg", 1, Arrays.asList(new FoodCategory[]{foodCatEnergizing}));
		
		foodCatLowFat.setMenuItems(Arrays.asList(new MenuItem[]{meal5}));
		foodCatHighEnergy.setMenuItems(Arrays.asList(new MenuItem[]{meal1, meal2, meal3, meal4, meal6}));
		foodCatVegy.setMenuItems(Arrays.asList(new MenuItem[]{meal6}));
		foodCatItalian.setMenuItems(Arrays.asList(new MenuItem[]{meal1, meal2, meal3, meal4, meal5, meal6}));
		foodCatAlcoholic.setMenuItems(Arrays.asList(new MenuItem[]{d1}));
		foodCatEnergizing.setMenuItems(Arrays.asList(new MenuItem[]{d2}));
		foodCatAsian.setMenuItems(Arrays.asList(new MenuItem[]{}));

		Menu menu = new Menu();
		restaurant.setMenu(menu);

		menu.getMeals().add(meal1);
		menu.getMeals().add(meal2);
		menu.getMeals().add(meal3);
		menu.getMeals().add(meal4);
		menu.getMeals().add(meal5);
		menu.getMeals().add(meal6);
		menu.getDrinks().add(d1);
		menu.getDrinks().add(d2);
		
		byte[] photo = new byte[]{127,-128,0};
		Customer c1 = new Customer("piet", "bakker", new DateTime(), 1, "description", photo, Arrays.asList(new Restaurant[]{restaurant}));
		Customer c2 = new Customer("piet", "bakker", new DateTime(), 1, "description", photo, Arrays.asList(new Restaurant[]{restaurant}));
		Customer c3 = new Customer("piet", "bakker", new DateTime(), 1, "description", photo, Arrays.asList(new Restaurant[]{restaurant}));

		restaurant.setCustomers(Arrays.asList(new Customer[]{c1, c2, c3}));
		
		// should save everything due to cascading
		save(restaurant);
		
		return restaurant;
	}

	public Restaurant createPittigePannekoekWithInventory() {
		Restaurant restaurant = new Restaurant(pittigePannekoekRestaurantName, "dePittigePannekoek.jpg");

		int NUMBER_OF_TABLES = 5;
		for(int i=0; i<NUMBER_OF_TABLES; i++) {	
			DiningTable diningTable = new DiningTable();
			diningTable.setRestaurant(restaurant);
			restaurant.getDiningTables().add(diningTable);
		}

		FoodCategory foodCatLowFat = new FoodCategory("low fat");
		FoodCategory foodCatHighEnergy = new FoodCategory("high energy");
		FoodCategory foodCatVegy = new FoodCategory("vegatarian");
		FoodCategory foodCatItalian = new FoodCategory("italian");
		FoodCategory foodCatAsian = new FoodCategory("asian");
		restaurant.getFoodCategories().add(foodCatLowFat); 
		restaurant.getFoodCategories().add(foodCatHighEnergy); 
		restaurant.getFoodCategories().add(foodCatVegy); 
		restaurant.getFoodCategories().add(foodCatItalian); 
		restaurant.getFoodCategories().add(foodCatAsian); 

		Meal meal1 = new Meal("spaghetti", "spaghetti.jpg", 8, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatHighEnergy}));                
		Meal meal2 = new Meal("macaroni", "macaroni.jpg", 8, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatHighEnergy}));
		Meal meal3 = new Meal("canneloni", "canneloni.jpg", 9, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatHighEnergy}));
		Meal meal4 = new Meal("pizza", "pizza.jpg", 9, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatHighEnergy}));
		Meal meal5 = new Meal("carpaccio", "carpaccio.jpg", 7, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatLowFat}));
		Meal meal6 = new Meal("ravioli", "ravioli.jpg", 8, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatVegy, foodCatHighEnergy}));
		
		foodCatLowFat.setMenuItems(Arrays.asList(new MenuItem[]{meal5}));
		foodCatHighEnergy.setMenuItems(Arrays.asList(new MenuItem[]{meal1, meal2, meal3, meal4, meal6}));
		foodCatVegy.setMenuItems(Arrays.asList(new MenuItem[]{meal6}));
		foodCatItalian.setMenuItems(Arrays.asList(new MenuItem[]{meal1, meal2, meal3, meal4, meal5, meal6}));
		// no asian food yet in restaurant foodCatAsian.setMenuItems(Arrays.asList());

		Menu menu = new Menu();
		restaurant.setMenu(menu);

		menu.getMeals().add(meal1);
		menu.getMeals().add(meal2);
		menu.getMeals().add(meal3);
		menu.getMeals().add(meal4);
		menu.getMeals().add(meal5);
		menu.getMeals().add(meal6);
		
		byte[] photo = new byte[]{127,-128,0};
		Customer c1 = new Customer("meike", "makkels", new DateTime(), 1, "description", photo, Arrays.asList(new Restaurant[]{restaurant}));
		Customer c2 = new Customer("meike", "makkels", new DateTime(), 1, "description", photo, Arrays.asList(new Restaurant[]{restaurant}));
		Customer c3 = new Customer("meike", "makkels", new DateTime(), 1, "description", photo, Arrays.asList(new Restaurant[]{restaurant}));

		restaurant.setCustomers(Arrays.asList(new Customer[]{c1, c2, c3}));

		// should save everything due to cascading
		save(restaurant);

		return restaurant;
	}

	public Restaurant createHmmmBurgerWithInventory() {
		Restaurant restaurant = new Restaurant(hmmmBurgerRestaurantName, "deHmmmBurger.jpg");

		int NUMBER_OF_TABLES = 5;
		for(int i=0; i<NUMBER_OF_TABLES; i++) {	
			DiningTable diningTable = new DiningTable();
			diningTable.setRestaurant(restaurant);
			restaurant.getDiningTables().add(diningTable);
		}

		FoodCategory foodCatLowFat = new FoodCategory("low fat");
		FoodCategory foodCatHighEnergy = new FoodCategory("high energy");
		FoodCategory foodCatVegy = new FoodCategory("vegatarian");
		FoodCategory foodCatItalian = new FoodCategory("italian");
		FoodCategory foodCatAsian = new FoodCategory("asian");
		restaurant.getFoodCategories().add(foodCatLowFat); 
		restaurant.getFoodCategories().add(foodCatHighEnergy); 
		restaurant.getFoodCategories().add(foodCatVegy); 
		restaurant.getFoodCategories().add(foodCatItalian); 
		restaurant.getFoodCategories().add(foodCatAsian); 

		Meal meal1 = new Meal("spaghetti", "spaghetti.jpg", 8, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatHighEnergy}));                
		Meal meal2 = new Meal("macaroni", "macaroni.jpg", 8, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatHighEnergy}));
		Meal meal3 = new Meal("canneloni", "canneloni.jpg", 9, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatHighEnergy}));
		Meal meal4 = new Meal("pizza", "pizza.jpg", 9, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatHighEnergy}));
		Meal meal5 = new Meal("carpaccio", "carpaccio.jpg", 7, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatLowFat}));
		Meal meal6 = new Meal("ravioli", "ravioli.jpg", 8, Arrays.asList(new FoodCategory[]{foodCatItalian, foodCatVegy, foodCatHighEnergy}));
		
		foodCatLowFat.setMenuItems(Arrays.asList(new MenuItem[]{meal5}));
		foodCatHighEnergy.setMenuItems(Arrays.asList(new MenuItem[]{meal1, meal2, meal3, meal4, meal6}));
		foodCatVegy.setMenuItems(Arrays.asList(new MenuItem[]{meal6}));
		foodCatItalian.setMenuItems(Arrays.asList(new MenuItem[]{meal1, meal2, meal3, meal4, meal5, meal6}));
		// no asian food yet in restaurant foodCatAsian.setMenuItems(Arrays.asList());

		Menu menu = new Menu();
		restaurant.setMenu(menu);

		menu.getMeals().add(meal1);
		menu.getMeals().add(meal2);
		menu.getMeals().add(meal3);
		menu.getMeals().add(meal4);
		menu.getMeals().add(meal5);
		menu.getMeals().add(meal6);
		
		// should save everything due to cascading
		save(restaurant);

		return restaurant;
	}
	
}
