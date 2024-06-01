package com.gfg.ewallet69.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration {

    @Bean("passwordEncoder")
    public PasswordEncoder getPasswordEncoder(){
        //cover the prototype example.
        return new BCryptPasswordEncoder();
    }

}
