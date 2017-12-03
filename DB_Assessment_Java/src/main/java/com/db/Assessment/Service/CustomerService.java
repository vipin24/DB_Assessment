package com.db.Assessment.Service;

import com.db.Assessment.Beans.CustomerDetails;
import com.db.Assessment.Beans.CustomerDetailsUpdate;
import com.db.Assessment.Beans.TransferAmount;
import com.db.Assessment.Exception.CustomerNotFoundExc;
import com.db.Assessment.Exception.InSufficentBalanceExc;
import com.db.Assessment.Exception.SameAccountsTransferExc;
import com.db.Assessment.Repository.CustomerDetailsRepository;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class CustomerService {

    @Getter
    private final CustomerDetailsRepository customerRepository;

    @Getter
    private final NotifyService notificationService;

    @Autowired
    private ValidateTransfer transferValidator;

    @Autowired
    public CustomerService(CustomerDetailsRepository customerDetailRepository, NotifyService notificationService) {
        this.customerRepository = customerDetailRepository;
        this.notificationService = notificationService;
    }

    public boolean  createAccount(CustomerDetails customerDetail) {
        return this.customerRepository.createAccount(customerDetail);
    }

    public CustomerDetails getAccount(String customerId) {
        return this.customerRepository.getAccount(customerId);
    }

    /**
     * For transfer funds between two accounts 
     * @param transfer
     * @throws CustomerNotFoundExc when customer details not found in database.
     * @throws InSufficentBalanceExc when insufficient funds are for transfer.
     * @throws SameAccountsTransferExc when transfer between same account .
     */
    public void transferFunds(TransferAmount transfer) throws CustomerNotFoundExc, InSufficentBalanceExc, SameAccountsTransferExc {

        final CustomerDetails accountFrom = customerRepository.getAccount(transfer.getCustomerFromId());
        final CustomerDetails accountTo = customerRepository.getAccount(transfer.getCustomerToId());
        final BigDecimal amount = transfer.getAmount();

        transferValidator.validate_account(accountFrom, accountTo, transfer);

   
        boolean successful = customerRepository.updateAccountsBatch(Arrays.asList(
                new CustomerDetailsUpdate(accountFrom.getCustomerId(), amount.negate()),
                new CustomerDetailsUpdate(accountTo.getCustomerId(), amount)
                ));

        if (successful){
            notificationService.notifyTransfer(accountFrom, "Transfer to the Customer with ID :[" + accountTo.getCustomerId() + "]"+" is now complete for the amount of [" + transfer.getAmount() + "]. Thank You!!!");
            notificationService.notifyTransfer(accountTo, "Customer with ID [ " + accountFrom.getCustomerId() +"]" + " has transferred funds [ " + transfer.getAmount() + "]"+ " into your account.");
        }
    }

}
