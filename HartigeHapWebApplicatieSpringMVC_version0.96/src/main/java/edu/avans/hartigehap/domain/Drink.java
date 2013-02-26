package edu.avans.hartigehap.domain;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Drink extends MenuItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private Size size;

	public enum Size {
		SMALL, MEDIUM, LARGE
	};

	public Drink() {

	}

	public Drink(String name, String imageFileName, int price,
			Collection<FoodCategory> foodCategories) {
		super(name, imageFileName, price, foodCategories);

	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

}
