package com.db.Assessment.Test;

import com.db.Assessment.Service.NotifyService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class AccountsServiceTestConfiguration {

    @Bean
    @Primary
    public NotifyService notificationService(){
        return Mockito.mock(NotifyService.class);
    }

}
