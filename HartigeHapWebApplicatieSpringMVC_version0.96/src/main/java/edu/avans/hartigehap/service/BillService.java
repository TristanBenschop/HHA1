package edu.avans.hartigehap.service;

import java.util.List;

import edu.avans.hartigehap.domain.*;

public interface BillService {
	public Bill findBillById(Long billId);
	public void billHasBeenPaid(Bill bill) throws StateException;
	public List<Bill> findSubmittedBillsForRestaurant(Restaurant restaurant);
}
