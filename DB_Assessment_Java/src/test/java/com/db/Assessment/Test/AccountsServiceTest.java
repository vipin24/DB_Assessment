package com.db.Assessment.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.db.Assessment.Beans.CustomerDetails;
import com.db.Assessment.Beans.TransferAmount;
import com.db.Assessment.Exception.AlreadyExistExc;
import com.db.Assessment.Exception.CustomerNotFoundExc;
import com.db.Assessment.Exception.InSufficentBalanceExc;
import com.db.Assessment.Service.CustomerService;
import com.db.Assessment.Service.NotifyService;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private NotifyService notifyService;

    @Test
    public void addAccount() throws Exception {
        CustomerDetails customerDetails = new CustomerDetails("12345");
        customerDetails.setCustomerbalance((new BigDecimal(1222)));
        this.customerService.createAccount(customerDetails);

        assertThat(this.customerService.getAccount("12345")).isEqualTo(customerDetails);
    }

    @Test
    public void addAccountfailsOnsameId() throws Exception {
        String uniqueId = "Id-" + 5234;
        CustomerDetails customerDetails = new CustomerDetails(uniqueId);
        this.customerService.createAccount(customerDetails);

        try {
            this.customerService.createAccount(customerDetails);
            fail("Should have failed when adding same customerId");
        } catch (AlreadyExistExc ex) {
            assertThat(ex.getMessage()).isEqualTo( "Customer id " + uniqueId + " already exists!");
        }

    }

    @Test
    public void makeTransfer_should_fail_when_accountDoesNotExist() {
        final String customerFromId = UUID.randomUUID().toString();
        final String customerToId = UUID.randomUUID().toString();
        this.customerService.createAccount(new CustomerDetails(customerFromId));
        TransferAmount transferAmount = new TransferAmount(customerFromId, customerToId, new BigDecimal(131));
        try {
            this.customerService.transferFunds(transferAmount);
            fail("Should have failed because account does not exist");
        } catch (CustomerNotFoundExc customerNtFound) {
            assertThat(customerNtFound.getMessage()).isEqualTo("Customer " + customerToId + " not found.");
        }
        verifyZeroInteractions(notifyService);
    }

    @Test
    public void whenAccountNotSufficentFunds() {
        final String customerFromId = UUID.randomUUID().toString();
        final String customerToId = UUID.randomUUID().toString();
        this.customerService.createAccount(new CustomerDetails(customerFromId));
        this.customerService.createAccount(new CustomerDetails(customerToId));
        TransferAmount transferAmount = new TransferAmount(customerFromId, customerToId, new BigDecimal(131));
        try {
            this.customerService.transferFunds(transferAmount);
            fail("Should have failed because customer account does not have sufficent funds for the transfer");
        } catch (InSufficentBalanceExc inSuffEx) {
            assertThat(inSuffEx.getMessage()).isEqualTo("Not enough funds  in Customer account " + customerFromId + " balance=0");
        }
        verifyZeroInteractions(notifyService);
    }

    @Test
    public void transferFunds() {
        final String customerFromId = UUID.randomUUID().toString();
        final String customerToId = UUID.randomUUID().toString();
        final CustomerDetails customerFrom = new CustomerDetails(customerFromId, new BigDecimal("100.00"));
        final CustomerDetails customerTo = new CustomerDetails(customerToId, new BigDecimal("50.00"));

        this.customerService.createAccount(customerFrom);
        this.customerService.createAccount(customerTo);

        TransferAmount transferAmount = new TransferAmount(customerFromId, customerToId, new BigDecimal("20.00"));

        this.customerService.transferFunds(transferAmount);

        assertThat(this.customerService.getAccount(customerFromId).getCustomerbalance()).isEqualTo(new BigDecimal("80.00"));
        assertThat(this.customerService.getAccount(customerToId).getCustomerbalance()).isEqualTo(new BigDecimal("70.00"));

        verifyNotifications(customerFrom, customerTo, transferAmount);
    }

       private void verifyNotifications(final CustomerDetails customerFrom,
    		   final CustomerDetails customerTo, final TransferAmount transferAmount) {
    	   verify(notifyService, Mockito.times(1)).notifyTransfer(customerFrom, "Transfer to the Customer with ID :[" + customerTo.getCustomerId() + "]"+" is now complete for the amount of [" + transferAmount.getAmount() + "]. Thank You!!!");
    	   verify(notifyService, Mockito.times(1)).notifyTransfer(customerTo, "Customer with ID [ " + customerFrom.getCustomerId() +"]" + " has transferred funds [ " + transferAmount.getAmount() + "]"+ " into your account.");
       
    }


}
