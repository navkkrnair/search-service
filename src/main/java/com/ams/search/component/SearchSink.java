package com.ams.search.component;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface SearchSink
{
	public static String SEARCHQ = "searchQ";

	@Input(SearchSink.SEARCHQ)
	public MessageChannel searchQ();

}
