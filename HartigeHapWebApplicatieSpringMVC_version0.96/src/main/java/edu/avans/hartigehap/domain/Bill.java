package edu.avans.hartigehap.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author Erco
 */
@Entity
//optional
@Table(name = "BILLS")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Bill implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Collection<Order> orders = new ArrayList<Order>();
	private Order currentOrder;
	private Customer customer;
	private DiningTable diningTable;
	private BillStatus billStatus;
	private Date submittedTime;
	private Date paidTime;

	public enum BillStatus {
		CREATED, SUBMITTED, PAID
	};

	public Bill() {
		billStatus = BillStatus.CREATED;
		currentOrder = new Order();
		currentOrder.setBill(this);
		orders.add(currentOrder);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BILL_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// example of a bidirectional one-to-many
	@ManyToOne(cascade = javax.persistence.CascadeType.ALL)
	// optional, to have more control over column names:
	// @JoinColumn(name="CUSTOMER_CUSTOMER_ID",
	// referencedColumnName="CUSTOMER_ID", updatable=false)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "bill")
	public Collection<Order> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}

	@ManyToOne()
	public DiningTable getDiningTable() {
		return diningTable;
	}

	public void setDiningTable(DiningTable diningTable) {
		this.diningTable = diningTable;
	}

	// example of an *unidirectional* one-to-one relationship, mapped on
	// database by bill side
	@OneToOne(cascade = javax.persistence.CascadeType.ALL)
	// optional, if you want more control on column names
	// @JoinColumn(name="ORDER_ORDER_ID", referencedColumnName="ORDER_ID",
	// updatable=false)
	public Order getCurrentOrder() {
		return currentOrder;
	}

	public void setCurrentOrder(Order currentOrder) {
		this.currentOrder = currentOrder;
	}

	@Enumerated(EnumType.ORDINAL)
	// represented in database as integer
	public BillStatus getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(BillStatus billStatus) {
		this.billStatus = billStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getSubmittedTime() {
		return submittedTime;
	}

	public void setSubmittedTime(Date submittedTime) {
		this.submittedTime = submittedTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getPaidTime() {
		return paidTime;
	}

	public void setPaidTime(Date paidTime) {
		this.paidTime = paidTime;
	}

	/* business logic */

	@Transient
	public Collection<Order> getSubmittedOrders() {
		Collection<Order> submittedOrders = new ArrayList<Order>();
		Iterator<Order> orderIterator = orders.iterator();
		while (orderIterator.hasNext()) {
			Order tmp = orderIterator.next();
			if (tmp.isSubmittedOrSuccessiveState()) {
				submittedOrders.add(tmp);
			}
		}
		return submittedOrders;
	}

	// price of *all* orders, so submitted orders and current (not yet
	// submitted) order
	@Transient
	public int getPriceAllOrders() {
		int price = 0;
		Iterator<Order> orderIterator = orders.iterator();
		while (orderIterator.hasNext()) {
			price += orderIterator.next().getPrice();
		}
		return price;
	}

	// price of the *submitted or successive state* orders only
	@Transient
	public int getPriceSubmittedOrSuccessiveStateOrders() {
		int price = 0;
		Iterator<Order> orderIterator = orders.iterator();
		while (orderIterator.hasNext()) {
			Order tmp = orderIterator.next();
			if (tmp.isSubmittedOrSuccessiveState()) {
				price += tmp.getPrice();
			}
		}
		return price;
	}

	public void submitOrder() throws StateException {
		currentOrder.submit();
		currentOrder = new Order();
		currentOrder.setBill(this);
		orders.add(currentOrder);
	}

	/*
	 * as the table gets a new bill, there is no risk that a customer keeps
	 * ordering on the submitted or paid bill
	 */
	public void submit() throws StateException, EmptyBillException {
		boolean allEmpty = true;
		Iterator<Order> orderIterator = orders.iterator();
		while (orderIterator.hasNext()) {
			Order order = orderIterator.next();
			if (!order.isEmpty()) {
				allEmpty = false;
				break;
			}
		}
		if (allEmpty) {
			throw new EmptyBillException("not allowed to submit an empty bill");
		}

		if (!currentOrder.isEmpty() && currentOrder.getOrderStatus() == Order.OrderStatus.CREATED) {
			// the currentOrder is not empty, but not yet submitted
			throw new StateException("not allowed to submit an with currentOrder in created state");
		}

		// this can only happen by directly invoking HTTP requests, so not via
		// GUI
		// TODO better to use another exception, because now GUI show wrong
		// error message
		if (billStatus != BillStatus.CREATED) {
			throw new StateException(
			        "not allowed to submit an already submitted bill");
		}

		submittedTime = new Date();
		billStatus = BillStatus.SUBMITTED;
	}

	@Transient
	public boolean isSubmitted() {
		return (billStatus == BillStatus.SUBMITTED);
	}

	public void paid() throws StateException {

		// this can only happen by directly invoking HTTP requests, so not via
		// GUI
		if (billStatus != BillStatus.SUBMITTED) {
			throw new StateException(
			        "not allowed to pay an bill that is not in the submitted state");
		}

		paidTime = new Date();
		billStatus = BillStatus.PAID;
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
		if (!(object instanceof Bill)) {
			return false;
		}
		Bill other = (Bill) object;
		if ((this.id == null && other.id != null)
		        || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.Bill[ id=" + id + " ]";
	}

}
