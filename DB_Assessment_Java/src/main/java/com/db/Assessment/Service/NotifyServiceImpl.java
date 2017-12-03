package com.db.Assessment.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.db.Assessment.Beans.CustomerDetails;

@Slf4j
@Service
public class NotifyServiceImpl implements NotifyService {

  @Override
  public void notifyTransfer(CustomerDetails customerDetails, String transferComments) {
  // Assuming that my Some other colleague going to implement it.
    log
      .info("Sending notification to owner of {}: {}", customerDetails.getCustomerId(), transferComments);
  }

}
