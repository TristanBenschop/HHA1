package edu.avans.hartigehap.service;

import java.util.List;

import edu.avans.hartigehap.domain.*;

public interface OwnerService {
	public List<Owner> findAll();
	public Owner findById(String id);
	public List<Owner> findByName(String name);
	public Owner save(Owner owner);
	public void delete(Owner owner);


}
