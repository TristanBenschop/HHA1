package edu.avans.hartigehap.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author Erco
 */
@Entity
// @Inheritance optional
@Table(name = "MENUITEMS")
// optional
@SecondaryTable(name = "MENUITEM_IMAGES", pkJoinColumns = @PrimaryKeyJoinColumn(name = "MENUITEM_ID"))
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public abstract class MenuItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private Collection<FoodCategory> foodCategories = new ArrayList<FoodCategory>();
	private byte[] image; // just to show that this is possible as well (but not
							// used)
	private String imageFileName; // alternative to image in database!
	private int price;

	public MenuItem() {

	}

	public MenuItem(String name, String imageFileName, int price,
			Collection<FoodCategory> foodCategories) {
		this.name = name;
		this.imageFileName = imageFileName;
		this.price = price;
		this.foodCategories = foodCategories;

	}

	// use a natural key as primary key instead of a surrogate key (note:
	// no auto generation of primary key needed)
	@Id
	@Column(name = "MENUITEM_ID")
	// optional
	public String getId() {
		return name;
	}

	public void setId(String name) {
		this.name = name;
	}

	@ManyToMany(cascade = javax.persistence.CascadeType.ALL)
	// the JoinTable is optional, as it specifies the same as the default would
	// do
	// @JoinTable(name = "MENUITEMS_FOODCATEGORIES",
	// joinColumns = @JoinColumn(
	// name = "MENUITEM_ID",
	// referencedColumnName = "MENUITEM_ID"),
	// inverseJoinColumns = @JoinColumn(
	// name = "FOODCATEGORY_ID",
	// referencedColumnName = "FOODCATEGORY_ID"))
	public Collection<FoodCategory> getFoodCategories() {
		return foodCategories;
	}

	public void setFoodCategories(Collection<FoodCategory> foodCategories) {
		this.foodCategories = foodCategories;
	}

	@Column(name = "IMAGE", table = "MENUITEM_IMAGES")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	// optional, if you want to control the column name
	@Column(name = "IMAGEFILENAME")
	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	// JPA is case sensitive; the corresponding column name will be in small
	// caps: "price"
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	/* business logic */

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof MenuItem)) {
			return false;
		}
		MenuItem other = (MenuItem) object;
		if ((this.name == null && other.name != null)
				|| (this.name != null && !this.name.equals(other.name))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.MenuItem[ name=" + name + " ]";
	}

}
