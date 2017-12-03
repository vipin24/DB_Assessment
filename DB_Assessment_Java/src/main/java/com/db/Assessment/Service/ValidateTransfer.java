package com.db.Assessment.Service;

import com.db.Assessment.Beans.CustomerDetails;
import com.db.Assessment.Beans.TransferAmount;
import com.db.Assessment.Exception.CustomerNotFoundExc;
import com.db.Assessment.Exception.InSufficentBalanceExc;

interface ValidateTransfer {

    void validate_account(final CustomerDetails accountFrom, final CustomerDetails accountTo, final TransferAmount transfer) throws CustomerNotFoundExc, InSufficentBalanceExc;

}
