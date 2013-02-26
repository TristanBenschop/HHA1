package edu.avans.hartigehap.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import edu.avans.hartigehap.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

	public List<Customer> findByFirstNameAndLastName(String firstName, String lastName);

	public List<Customer> findByRestaurants(
			Collection<Restaurant> restaurants, 
			Sort sort);

	public Page<Customer> findByRestaurants(
			Collection<Restaurant> restaurants, 
			Pageable pageable);
}
