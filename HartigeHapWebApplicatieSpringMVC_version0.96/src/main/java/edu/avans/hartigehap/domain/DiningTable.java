package edu.avans.hartigehap.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author Erco
 */
@Entity
@Table(name = "DININGTABLES")
// optional
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class DiningTable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Restaurant restaurant;
	private Collection<Bill> bills = new ArrayList<Bill>();
	private Bill currentBill;

	public DiningTable() {
		// when the system resets, the c'tor is executed and a new Bill object
		// is created (which in
		// its turn creates a new order object. However, when the dining table
		// becomes managed, the
		// currentBill as was stored in the database is retrieved, and the new
		// Bill and new Order
		// object, which were not managed yet are discarded.
		currentBill = new Bill();
		currentBill.setDiningTable(this);
		bills.add(currentBill);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DININGTABLE_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne()
	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "diningTable")
	public Collection<Bill> getBills() {
		return bills;
	}

	public void setBills(Collection<Bill> bills) {
		this.bills = bills;
	}

	// example of an *unidirectional* one-to-one relationship, mapped on
	// database by diningTable side
	@OneToOne(cascade = javax.persistence.CascadeType.ALL)
	// optional, if you want more control on column names
	// @JoinColumn(name="BILL_BILL_ID", referencedColumnName="BILL_ID",
	// updatable=false)
	public Bill getCurrentBill() {
		return currentBill;
	}

	public void setCurrentBill(Bill currentBill) {
		this.currentBill = currentBill;
	}

	/* business logic */

	public void warmup() {
		Iterator<OrderItem> orderItemIterator = currentBill.getCurrentOrder()
				.getOrderItems().iterator();
		while (orderItemIterator.hasNext()) {
			orderItemIterator.next().getId();
			// note: menu items have been warmed up via the restaurant->menu
			// relation; therefore it
			// is not needed to warm these objects via this relation
		}
	}

	public void submitBill() throws StateException, EmptyBillException {
		currentBill.submit();
		currentBill = new Bill();
		currentBill.setDiningTable(this);
		bills.add(currentBill);
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
		if (!(object instanceof DiningTable)) {
			return false;
		}
		DiningTable other = (DiningTable) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.DiningTable[ id=" + id + " ]";
	}

}
