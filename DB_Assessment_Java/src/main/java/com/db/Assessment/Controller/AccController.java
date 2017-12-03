package com.db.Assessment.Controller;
/*
 * Class:AccController . Here first request will come and accordingly action will be taken place.
 */

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.db.Assessment.Beans.CustomerDetails;
import com.db.Assessment.Beans.TransferAmount;
import com.db.Assessment.Exception.AlreadyExistExc;
import com.db.Assessment.Exception.CustomerNotFoundExc;
import com.db.Assessment.Exception.InSufficentBalanceExc;
import com.db.Assessment.Exception.SameAccountsTransferExc;
import com.db.Assessment.Service.CustomerService;



@RestController
@RequestMapping("/customers/")
@Slf4j
public class AccController {
	
	 private final CustomerService customerService;
	 @Autowired
	    public AccController(CustomerService customerService) {
	        this.customerService = customerService;
	    }
	
	// Creating Account 
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAccount(@RequestBody @Valid CustomerDetails customerDetails) {
        log.info("Creation of Customer Account: {}", customerDetails);

        try {
           boolean checkAcct= this.customerService.createAccount(customerDetails);
           log.info((checkAcct==true)?("Account Created!!!"):("Account Not Cretaed"));
        } catch (AlreadyExistExc alreadyExist) {
            return new ResponseEntity<>(alreadyExist.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
	// Fetching account details 
	  @GetMapping(path = "/accountNo/{customerId}")
	    public CustomerDetails getAccount(@PathVariable String customerId) {
	        log.info("Retrieving account for id {}", customerId);
	        CustomerDetails cust = this.customerService.getAccount(customerId);
	        log.info((cust==null)?("Customer Details are not Present !!!"):("Giving you Customer Details:"));
	        return this.customerService.getAccount(customerId);
	    }
	  // Transferring Funds from one account to another 
	    @PutMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<Object> makeTransfer(@RequestBody @Valid TransferAmount transferAmount) {
	        log.info("Making transfer {}", transferAmount);
	        try {
	            this.customerService.transferFunds(transferAmount);
	        } catch (CustomerNotFoundExc customerNtExc) {
	            return new ResponseEntity<>(customerNtExc.getMessage(), HttpStatus.NOT_FOUND);
	        } catch (InSufficentBalanceExc insufficentBal) {
	            return new ResponseEntity<>(insufficentBal.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	        } catch (SameAccountsTransferExc sameAccounts) {
	            return new ResponseEntity<>(sameAccounts.getMessage(), HttpStatus.BAD_REQUEST);
	        }

	        return new ResponseEntity<>(HttpStatus.OK);
	    }
	

}
