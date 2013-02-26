package edu.avans.hartigehap.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author Erco
 */
@Entity
//optional
@Table(name = "RESTAURANTS") 
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Restaurant implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id; // restaurant name
	private Collection<DiningTable> diningTables = new ArrayList<DiningTable>();
	private Collection<FoodCategory> foodCategories = new ArrayList<FoodCategory>();
	private Collection<Customer> customers = new ArrayList<Customer>();
	private Menu menu;
	private String imageFileName;

	public Restaurant() {
	}

	public Restaurant(String name, String imageFileName) {
		this.id = name;
		this.imageFileName = imageFileName;
	}

	// this is an example of using a natural key as primary key instead of a
	// surrogate key (note: no auto generation of primary key needed)
	@Id
	@Column(name = "RESTAURANT_ID")
	public String getId() {
		return id;
	}

	public void setId(String name) {
		this.id = name;
	}

	@OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "restaurant")
	public Collection<DiningTable> getDiningTables() {
		return diningTables;
	}

	public void setDiningTables(Collection<DiningTable> diningTables) {
		this.diningTables = diningTables;
	}

	// This is an example of a *unidirectional* one-to-many relationship. In JPA
	// 1.0 this resulted
	// in an additional join table (aka intersection table or koppeltabel). In
	// JPA 1.0 the only
	// workaround to get a the "normal" foreign key mapping, was to make the
	// relation bidirectional.
	// As of JPA 2.0, *unidirectional* one-to-many relationships are mapped to
	// the "normal" foreign key
	@OneToMany(cascade = javax.persistence.CascadeType.ALL)
	@JoinColumn(name = "ERCO_FOODCATEGORY_RESTAURANT_ID", referencedColumnName = "RESTAURANT_ID")
	public Collection<FoodCategory> getFoodCategories() {
		return foodCategories;
	}

	public void setFoodCategories(Collection<FoodCategory> foodCategories) {
		this.foodCategories = foodCategories;
	}

	// *unidirectional* one-to-one
	@OneToOne(cascade = javax.persistence.CascadeType.ALL)
	// optional, if you want to choose your own column names
	// @JoinColumn(name="MENU_MENU_ID", referencedColumnName="MENU_ID",
	// updatable=false)
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@ManyToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "restaurants")
	public Collection<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Collection<Customer> customers) {
		this.customers = customers;
	}


	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	// business methods

	public void warmup() {
		Iterator<DiningTable> diningTableIterator = diningTables.iterator();
		while(diningTableIterator.hasNext()) {
			diningTableIterator.next().getId();
		}		
		Iterator<FoodCategory> foodCategoryIterator = foodCategories.iterator();
		while(foodCategoryIterator.hasNext()) {
			foodCategoryIterator.next().getId();
		}

		Iterator<MenuItem> miIterator = menu.getMeals().iterator();
		while(miIterator.hasNext()) {
			MenuItem mi = miIterator.next();
			mi.getId();
			Iterator<FoodCategory> fcIterator = mi.getFoodCategories().iterator();
			while(fcIterator.hasNext()) {
				fcIterator.next().getId();                
			}
		}
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Restaurant)) {
			return false;
		}
		Restaurant other = (Restaurant) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.Restaurant[ name=" + id + " ]";
	}

}
