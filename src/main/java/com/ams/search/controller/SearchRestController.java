package com.ams.search.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ams.search.component.SearchComponent;
import com.ams.search.entity.Flight;

@RestController
@RefreshScope
@RequestMapping("/search")
class SearchRestController
{
    private static final Logger logger = LoggerFactory.getLogger(SearchRestController.class);

    private SearchComponent searchComponent;

    @Value("${originairports.shutdown}")
    private String originAirportShutdownList;

    @Autowired
    public SearchRestController(SearchComponent searchComponent)
    {
        this.searchComponent = searchComponent;
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    List<Flight> search(@RequestBody SearchQuery query)
    {
        if (Arrays.asList(originAirportShutdownList.split(","))
                .contains(query.getOrigin()))
        {
            logger.info("The origin airport is in shutdown state");
            return new ArrayList<Flight>();
        }
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String username  = ((UserDetails) principal).getUsername();

        logger.info("Logged in user :" + username + " searched flights on " + query);
        return searchComponent.search(query);
    }

}
