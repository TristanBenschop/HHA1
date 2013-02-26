package edu.avans.hartigehap.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import edu.avans.hartigehap.domain.*;

public interface DiningTableService {

	public List<DiningTable> findAll();
	public DiningTable findById(Long id);
	public DiningTable save(DiningTable diningTable);
	public void delete(Long id);
	public Page<DiningTable> findAllByPage(Pageable pageable);
		
	public DiningTable fetchWarmedUp(Long diningTableId);
	public void addOrderItem(DiningTable diningTable, String menuItemName);
	public void deleteOrderItem(DiningTable diningTable, String menuItemName);
	public void submitOrder(DiningTable diningTable) throws StateException;
	public void submitBill(DiningTable diningTable) throws StateException, EmptyBillException;
}