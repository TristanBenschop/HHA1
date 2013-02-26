package edu.avans.hartigehap.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author Erco
 */
@Entity
@Table(name = "MENUS")
// optional
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Collection<MenuItem> meals = new ArrayList<MenuItem>();
	private Collection<MenuItem> drinks = new ArrayList<MenuItem>();

	// surrogate key as primary key
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MENU_ID")
	// optional
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@JoinColumn(name = "ERCO_MEAL_MENU_ID", referencedColumnName = "MENU_ID")
	public Collection<MenuItem> getMeals() {
		return meals;
	}

	public void setMeals(Collection<MenuItem> meals) {
		this.meals = meals;
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
	@JoinColumn(name = "ERCO_DRINK_MENU_ID", referencedColumnName = "MENU_ID")
	public Collection<MenuItem> getDrinks() {
		return drinks;
	}

	public void setDrinks(Collection<MenuItem> drinks) {
		this.drinks = drinks;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Menu)) {
			return false;
		}
		Menu other = (Menu) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.Menu[ id=" + id + " ]";
	}

}
