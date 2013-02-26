package edu.avans.hartigehap.domain;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Meal extends MenuItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private String recipe;

	public Meal() {

	}

	public Meal(String name, String imageFileName, int price,
			Collection<FoodCategory> foodCategories) {
		super(name, imageFileName, price, foodCategories);

	}

	public String getRecipe() {
		return recipe;
	}

	public void setRecipe(String recipe) {
		this.recipe = recipe;
	}
}
