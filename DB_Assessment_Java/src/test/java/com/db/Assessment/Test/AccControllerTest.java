package com.db.Assessment.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.db.Assessment.Beans.CustomerDetails;
import com.db.Assessment.Service.CustomerService;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class AccControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void prepareMockMvc() {
    this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

    // Clear accounts if any
    customerService.getCustomerRepository().clearAccounts();
  }

  @Test
  public void createAccount() throws Exception {
    createCustomerWithContent("{\"customerId\":\"123456\",\"customerbalance\":1250}").andExpect(status().isCreated());

    CustomerDetails customerDetails = customerService.getAccount("123456");
    assertThat(customerDetails.getCustomerId()).isEqualTo("123456");
    assertThat(customerDetails.getCustomerbalance()).isEqualByComparingTo("1250");
  }

  @Test
  public void createSameAccount() throws Exception {
    createCustomerWithContent("{\"customerId\":\"123456\",\"customerbalance\":1250}").andExpect(status().isCreated());

    createCustomerWithContent("{\"customerId\":\"123456\",\"customerbalance\":1250}").andExpect(status().isBadRequest());
  }

  private ResultActions createCustomerWithContent(final String content) throws Exception {
    return this.mockMvc.perform(post("/customers/").contentType(MediaType.APPLICATION_JSON)
            .content(content));
  }



 
}
