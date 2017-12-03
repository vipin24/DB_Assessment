package com.db.Assessment.Service;

import com.db.Assessment.Beans.CustomerDetails;
import com.db.Assessment.Beans.TransferAmount;
import com.db.Assessment.Exception.CustomerNotFoundExc;
import com.db.Assessment.Exception.InSufficentBalanceExc;
import com.db.Assessment.Exception.SameAccountsTransferExc;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransferValidatorImpl implements ValidateTransfer {

    /**
     * Here validation of accounts are done like funds,duplication etc..
     *
     * @param currAccountFrom  existing source account as found in the database
     * @param currAccountTo  existing destination account as found in the database
     * @param transfer  transfer as requested
     * @throws CustomerNotFoundExc
     * @throws InSufficentBalanceExc
     * @throws SameAccountsTransferExc
     */
    public void validate_account(final CustomerDetails currAccountFrom, final CustomerDetails currAccountTo, final TransferAmount transfer)
            throws CustomerNotFoundExc, InSufficentBalanceExc, SameAccountsTransferExc{

        if (currAccountFrom == null){
            throw new CustomerNotFoundExc("Customer " + transfer.getCustomerFromId() + " not found.");
        }

        if (currAccountTo == null) {
            throw new CustomerNotFoundExc("Customer " + transfer.getCustomerToId() + " not found.");
        }

        if (checkDuplicacy(transfer)){
            throw new SameAccountsTransferExc("Transfer to and from should be different ");
        }

        if (!insufficentFunds(currAccountFrom, transfer.getAmount())){
            throw new InSufficentBalanceExc("Not enough funds  in Customer account " + currAccountFrom.getCustomerId() + " balance="+currAccountFrom.getCustomerbalance());
        }
    }

    private boolean checkDuplicacy(final TransferAmount transfer) {
        return transfer.getCustomerFromId().equals(transfer.getCustomerToId());
    }


    private boolean insufficentFunds(final CustomerDetails customerDetails, final BigDecimal amount) {
        return customerDetails.getCustomerbalance().subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
    }

}
