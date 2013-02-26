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
import org.hibernate.mapping.List;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author Tristan
 */
@Entity
// optional
@Table(name = "OWNERS")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Owner implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long ID;
	@Override
	public String toString() {
		return "Owner [ID=" + ID + "]";
	}
	private int version;
	private String Name;
	private Collection<Restaurant> restaurants = new ArrayList<Restaurant>();
	
	
	public Owner() {

	}

	// TODO not complete (bills)
	public Owner(Long ID, int version, String Name) {
		this.setID(ID);
		this.setVersion(version);
		this.setName(Name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Owner other = (Owner) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID")
	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}
	@Column(name ="NAME")
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
	@Column(name ="VERSION")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
}
