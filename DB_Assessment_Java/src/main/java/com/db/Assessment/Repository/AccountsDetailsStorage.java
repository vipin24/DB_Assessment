package com.db.Assessment.Repository;

import com.db.Assessment.Beans.CustomerDetails;
import com.db.Assessment.Beans.CustomerDetailsUpdate;
import com.db.Assessment.Exception.AlreadyExistExc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class AccountsDetailsStorage implements CustomerDetailsRepository {

    private final Map<String, CustomerDetails> accountStorageMap = new HashMap<String, CustomerDetails>();

    @Override
    public boolean  createAccount(CustomerDetails customerDetails) throws AlreadyExistExc {
        CustomerDetails previousAccount = accountStorageMap.putIfAbsent(customerDetails.getCustomerId(), customerDetails);
        if (previousAccount != null) {
        
            throw new AlreadyExistExc(
                    "Customer id " + customerDetails.getCustomerId() + " already exists!");
            
        }
		return previousAccount==null?true:false;
    }

    @Override
    public CustomerDetails getAccount(String customerId) {
        return accountStorageMap.get(customerId);
    }

    @Override
    public void clearAccounts() {
        accountStorageMap.clear();
    }

    @Override
    public boolean updateAccountsBatch(List<CustomerDetailsUpdate> customerDetailUpdates) {
    	customerDetailUpdates.stream().forEach(this::updateAccount);

        return true;
    }

    private void updateAccount(final CustomerDetailsUpdate customerDetailUpd) {
        final String customerId = customerDetailUpd.getCustomerId();
        accountStorageMap.computeIfPresent(customerId, (key, account) -> {
            account.setCustomerbalance(account.getCustomerbalance().add(customerDetailUpd.getAmount()));
            return account;
        });
    }

}
