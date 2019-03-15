package com.ams.search.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(SearchSink.class)
public class Receiver
{
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    @Autowired
    private SearchComponent     searchComponent;

    @StreamListener(SearchSink.SEARCHQ)
    public void accept(BookingConfirmationMessage message)
    {
        logger.info("Received booking event :" + message);
        searchComponent.updateInventory(message.getFlightNumber(),
                                        message.getFlightDate(),
                                        message.getInventory());
        // call repository and update the fare for the given flight
    }
}