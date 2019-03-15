package com.ams.search.component;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ams.search.controller.SearchQuery;
import com.ams.search.entity.Flight;
import com.ams.search.entity.Inventory;
import com.ams.search.repository.FlightRepository;

@Component
public class SearchComponent
{
    private FlightRepository    flightRepository;
    private static final Logger logger = LoggerFactory.getLogger(SearchComponent.class);

    @Autowired
    public SearchComponent(FlightRepository flightRepository)
    {
        this.flightRepository = flightRepository;
    }

    public List<Flight> search(SearchQuery query)
    {
        logger.info("In SearchComponents search method: SearchQuery send to FlightRepository datasource");
        List<Flight> flights      = flightRepository
                .findByOriginAndDestinationAndFlightDate(query.getOrigin(),
                                                         query.getDestination(),
                                                         query.getFlightDate());
        List<Flight> searchResult = new ArrayList<Flight>();
        searchResult.addAll(flights);
        flights.forEach(flight ->
        {
            flight.getFares();
            int inv = flight.getInventory()
                    .getCount();
            if (inv < 0)
            {
                searchResult.remove(flight);
            }
        });
        logger.info("Returning flight details from SearchComponent " + searchResult);
        return searchResult;
    }

    public void updateInventory(String flightNumber, String flightDate, int inventory)
    {
        logger.info("Updating inventory for flight " + flightNumber + " innventory " + inventory);
        Flight    flight = flightRepository.findByFlightNumberAndFlightDate(flightNumber,
                                                                            flightDate);
        Inventory inv    = flight.getInventory();
        inv.setCount(inventory);
        flightRepository.save(flight);
    }
}
