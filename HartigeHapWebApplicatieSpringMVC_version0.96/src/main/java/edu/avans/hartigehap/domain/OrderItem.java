package edu.avans.hartigehap.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author Erco
 */
@Entity
@Table(name = "ORDERITEMS")
// optional
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class OrderItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private MenuItem menuItem;
	private int quantity = 0;

	public OrderItem() {

	}

	public OrderItem(MenuItem menuItem, int quantity) {
		this.menuItem = menuItem;
		this.quantity = quantity;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORDERITEM_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// *unidirectional* one-to-one
	// no cascade!!
	@OneToOne()
	// optional, if you want to choose your own column names
	// @JoinColumn(name="MENUITEM_MENUITEM_ID",
	// referencedColumnName="MENUITEM_ID", updatable=false)
	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/* business logic */

	public void incrementQuantity() {
		this.quantity++;
	}

	public void decrementQuantity() {
		assert (quantity > 0);
		this.quantity--;
	}

	@Transient
	public int getPrice() {
		return menuItem.getPrice() * quantity;
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
		if (!(object instanceof OrderItem)) {
			return false;
		}
		OrderItem other = (OrderItem) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.OrderItem[ id=" + id + " ]";
	}

}
