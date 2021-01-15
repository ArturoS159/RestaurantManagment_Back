package com.przemarcz.restaurant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/restaurants/public", "/restaurants/*/public", "/restaurants/*/time/public", "/restaurants/*/meals/public", "/restaurants/{restaurantId}/meals/category/public", "/restaurants/*/opinions/public");
    }
}
