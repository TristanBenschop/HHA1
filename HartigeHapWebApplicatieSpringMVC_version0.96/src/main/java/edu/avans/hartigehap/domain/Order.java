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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

//import javax.persistence.CascadeType.*;

/**
 * 
 * @author Erco
 */
@Entity
@NamedQuery(name = "Order.findSubmittedOrders", query = "SELECT o FROM Order o "
		+ "WHERE o.orderStatus = edu.avans.hartigehap.domain.Order$OrderStatus.SUBMITTED "
		+ "AND o.bill.diningTable.restaurant = :restaurant "
		+ "ORDER BY o.submittedTime")
@Table(name = "ORDERS")
// to prevent collision with MySql reserved keyword
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Collection<OrderItem> orderItems = new ArrayList<OrderItem>();
	private Bill bill;
	private Date submittedTime;
	private Date plannedTime;
	private Date preparedTime;
	private Date servedTime;
	private OrderStatus orderStatus;

	public enum OrderStatus {
		CREATED, SUBMITTED, PLANNED, PREPARED, SERVED
	};

	public Order() {
		orderStatus = OrderStatus.CREATED;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORDER_ID")
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
	@JoinColumn(name = "ERCO_ORDERITEM_ORDER_ID", referencedColumnName = "ORDER_ID")
	public Collection<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Collection<OrderItem> newValue) {
		this.orderItems = newValue;
	}

	@ManyToOne()
	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getSubmittedTime() {
		return submittedTime;
	}

	public void setSubmittedTime(Date submittedTime) {
		this.submittedTime = submittedTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getPlannedTime() {
		return plannedTime;
	}

	public void setPlannedTime(Date plannedTime) {
		this.plannedTime = plannedTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getPreparedTime() {
		return preparedTime;
	}

	public void setPreparedTime(Date preparedTime) {
		this.preparedTime = preparedTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getServedTime() {
		return servedTime;
	}

	public void setServedTime(Date servedTime) {
		this.servedTime = servedTime;
	}

	@Enumerated(EnumType.ORDINAL)
	// represented in database as integer
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	/* business logic */

	@Transient
	public boolean isSubmittedOrSuccessiveState() {
		return (orderStatus != OrderStatus.CREATED);
	}

	// isEmpty() is reserved by JPA, therefore we use a strange name
	@Transient
	public boolean isEmpty() {
		return orderItems.isEmpty();
	}

	public void addOrderItem(MenuItem menuItem) {
		Iterator<OrderItem> orderItemIterator = orderItems.iterator();
		boolean found = false;
		while (orderItemIterator.hasNext()) {
			OrderItem orderItem = orderItemIterator.next();
			if (orderItem.getMenuItem().equals(menuItem)) {
				orderItem.incrementQuantity();
				found = true;
				break;
			}
		}
		if (!found) {
			OrderItem orderItem = new OrderItem(menuItem, 1);
			orderItems.add(orderItem);
		}
	}

	public void deleteOrderItem(MenuItem menuItem) {
		Iterator<OrderItem> orderItemIterator = orderItems.iterator();
		boolean found = false;
		while (orderItemIterator.hasNext()) {
			OrderItem orderItem = orderItemIterator.next();
			if (orderItem.getMenuItem().equals(menuItem)) {
				found = true;
				if (orderItem.getQuantity() > 1) {
					orderItem.decrementQuantity();
				} else { // orderItem.getQuantity() == 1
					orderItemIterator.remove();
				}
				break;
			}
		}
		if (!found) {
			// do nothing
		}
	}

	public void submit() throws StateException {
		if (isEmpty()) {
			throw new StateException("not allowed to submit an empty order");
		}

		// this can only happen by directly invoking HTTP requests, so not via
		// GUI
		if (orderStatus != OrderStatus.CREATED) {
			throw new StateException(
					"not allowed to submit an already submitted order");
		}
		submittedTime = new Date();
		orderStatus = OrderStatus.SUBMITTED;
	}

	public void plan() throws StateException {

		// this can only happen by directly invoking HTTP requests, so not via
		// GUI
		if (orderStatus != OrderStatus.SUBMITTED) {
			throw new StateException(
					"not allowed to plan an order that is not in the submitted state");
		}

		plannedTime = new Date();
		orderStatus = OrderStatus.PLANNED;
	}

	public void prepared() throws StateException {

		// this can only happen by directly invoking HTTP requests, so not via
		// GUI
		if (orderStatus != OrderStatus.PLANNED) {
			throw new StateException(
					"not allowed to change order state to prepared, if it is not in the planned state");
		}

		preparedTime = new Date();
		orderStatus = OrderStatus.PREPARED;
	}

	public void served() throws StateException {

		// this can only happen by directly invoking HTTP requests, so not via
		// GUI
		if (orderStatus != OrderStatus.PREPARED) {
			throw new StateException(
					"not allowed to change order state to served, if it is not in the prepared state");
		}

		servedTime = new Date();
		orderStatus = OrderStatus.SERVED;
	}

	@Transient
	public int getPrice() {
		int price = 0;
		Iterator<OrderItem> orderItemIterator = orderItems.iterator();
		while (orderItemIterator.hasNext()) {
			price += orderItemIterator.next().getPrice();
		}
		return price;
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
		if (!(object instanceof Order)) {
			return false;
		}
		Order other = (Order) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.Order[ id=" + id + " ]";
	}

}
