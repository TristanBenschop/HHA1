package edu.avans.hartigehap.service.jpa;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.repository.*;
import edu.avans.hartigehap.service.*;
import com.google.common.collect.Lists;



@Service("ownerService")
@Repository
@Transactional
public class OwnerServiceImpl implements OwnerService {
	@Autowired private OwnerRepository ownerRepository;
	
	@Override
	public List<Owner> findAll() {
		return Lists.newArrayList(ownerRepository.findAll());
	}

	@Override
	public Owner findById(String id) {
		// TODO Auto-generated method stub
		return ownerRepository.findOne(id);
	}

	@Override
	public List<Owner> findByName(String name) {
		return ownerRepository.findByName(name);
	}

	@Override
	public Owner save(Owner owner) {
		return ownerRepository.save(owner);
	}

	@Override
	public void delete(Owner owner) {
		ownerRepository.delete(owner);
	}
	
	
}

