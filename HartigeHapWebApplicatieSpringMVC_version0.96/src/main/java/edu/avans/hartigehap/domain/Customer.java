package edu.avans.hartigehap.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author Erco
 */
@Entity
// optional
@Table(name = "CUSTOMERS")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	private Collection<Restaurant> restaurants = new ArrayList<Restaurant>();
	private Long customerCardId;
	private int version;
	private Collection<Bill> bills = new ArrayList<Bill>();
	private int partySize;
	private String firstName;
	private String lastName;
	private DateTime birthDate;
	private String description;
	private byte[] photo;

	public Customer() {

	}

	// TODO not complete (bills)
	public Customer(String firstName, String lastName, DateTime birthDate,
			int partySize, String description, byte[] photo,
			Collection<Restaurant> restaurants) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.partySize = partySize;
		this.description = description;
		this.photo = photo;
		this.restaurants = restaurants;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CUSTOMER_ID")
	public Long getId() {
		return customerCardId;
	}

	public void setId(Long customerCardId) {
		this.customerCardId = customerCardId;
	}

	@ManyToMany(cascade = javax.persistence.CascadeType.ALL)
	public Collection<Restaurant> getRestaurants() {
		return restaurants;
	}

	public void setRestaurants(Collection<Restaurant> restaurants) {
		this.restaurants = restaurants;
	}

	@Version
	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	// bidirectional one-to-many; mapping on the database happens at the many
	// side
	@OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "customer")
	public Collection<Bill> getBills() {
		return bills;
	}

	public void setBills(Collection<Bill> bills) {
		this.bills = bills;
	}

	public int getPartySize() {
		return partySize;
	}

	public void setPartySize(int partySize) {
		this.partySize = partySize;
	}

	@NotEmpty(message = "{validation.firstname.NotEmpty.message}")
	@Size(min = 3, max = 60, message = "{validation.firstname.Size.message}")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotEmpty(message = "{validation.lastname.NotEmpty.message}")
	@Size(min = 1, max = 40, message = "{validation.lastname.Size.message}")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	// works with hibernate 3.x
	// @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	// to allow using Joda's DateTime with hibernate 4.x use:
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	// needed to allow changing a date in the GUI
	@DateTimeFormat(iso = ISO.DATE)
	public DateTime getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(DateTime birthDate) {
		this.birthDate = birthDate;
	}

	// example of a "derived property". This property can be be easily derived
	// from the
	// property "birthDate", so no need to persist it.
	@Transient
	public String getBirthDateString() {
		String birthDateString = "";
		if (birthDate != null)
			birthDateString = org.joda.time.format.DateTimeFormat.forPattern(
					"yyyy-MM-dd").print(birthDate);
		return birthDateString;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic(fetch = FetchType.LAZY)
	@Lob
	@Column(name = "PHOTO")
	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	// business logic

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (customerCardId != null ? customerCardId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Customer)) {
			return false;
		}
		Customer other = (Customer) object;
		if ((this.customerCardId == null && other.customerCardId != null)
				|| (this.customerCardId != null && !this.customerCardId
						.equals(other.customerCardId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.Party[" + " id=" + customerCardId + ", firstName="
				+ firstName + ", lastName=" + lastName + ", birthDate="
				+ birthDate + ", partySize=" + partySize + ", description="
				+ description + ", photo=" + photo + ", version=" + version
				+ ", restaurants=" + restaurants + " ]";
	}

}
