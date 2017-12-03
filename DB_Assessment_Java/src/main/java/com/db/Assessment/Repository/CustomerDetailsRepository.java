package com.db.Assessment.Repository;

import com.db.Assessment.Beans.CustomerDetails;
import com.db.Assessment.Beans.CustomerDetailsUpdate;
import com.db.Assessment.Exception.AlreadyExistExc;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerDetailsRepository {

  boolean createAccount(CustomerDetails customerDetails) throws AlreadyExistExc;

  CustomerDetails getAccount(String customerId);

  void clearAccounts();

  boolean updateAccountsBatch(List<CustomerDetailsUpdate> customerDetailUpdates);

}
