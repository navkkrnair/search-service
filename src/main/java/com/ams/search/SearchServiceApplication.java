package com.ams.search;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.ams.search.entity.Fares;
import com.ams.search.entity.Flight;
import com.ams.search.entity.Inventory;
import com.ams.search.repository.FlightRepository;

@SpringBootApplication
public class SearchServiceApplication extends WebSecurityConfigurerAdapter
        implements CommandLineRunner
{
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceApplication.class);

    @Autowired
    private FlightRepository flightRepository;

    public static void main(String[] args)
    {
        SpringApplication.run(SearchServiceApplication.class,
                              args);
    }

    @Override
    public void run(String... strings) throws Exception
    {
        flightRepository.deleteAll();
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight("BF100", "SEA", "SFO", "22-JAN-16", new Fares("100", "USD"), new Inventory(100)));
        flights.add(new Flight("BF101", "NYC", "SFO", "22-JAN-16", new Fares("101", "USD"), new Inventory(100)));
        flights.add(new Flight("BF105", "NYC", "SFO", "22-JAN-16", new Fares("105", "USD"), new Inventory(100)));
        flights.add(new Flight("BF106", "NYC", "SFO", "22-JAN-16", new Fares("106", "USD"), new Inventory(100)));
        flights.add(new Flight("BF102", "CHI", "SFO", "22-JAN-16", new Fares("102", "USD"), new Inventory(100)));
        flights.add(new Flight("BF103", "HOU", "SFO", "22-JAN-16", new Fares("103", "USD"), new Inventory(100)));
        flights.add(new Flight("BF104", "LAX", "SFO", "22-JAN-16", new Fares("104", "USD"), new Inventory(100)));

        flightRepository.saveAll(flights);

        logger.info("Looking to load flights...");
        for (Flight flight : flightRepository.findByOriginAndDestinationAndFlightDate("NYC",
                                                                                      "SFO",
                                                                                      "22-JAN-16"))
        {
            logger.info(flight.toString());
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests()
                .antMatchers("/actuator/bus-refresh")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .httpBasic()
                .disable()
                .csrf()
                .disable();
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

}
