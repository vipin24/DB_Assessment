package com.db.Assessment.Service;

import com.db.Assessment.Beans.CustomerDetails;

public interface NotifyService {

  void notifyTransfer(CustomerDetails customerDetails, String transfercomments);
}
