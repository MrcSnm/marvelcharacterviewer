package com.hipreme.mobbuy.marvel.character.lists;
import com.hipreme.mobbuy.marvel.character.summaries.EventSummary;

import org.json.JSONObject;

public class EventList extends MarvelList<EventSummary>
{
    public static final String EVENTS = "events";
    public EventList(JSONObject o)
    {
        super(o, EVENTS);
    }

    @Override
    public EventSummary getSummary(JSONObject jsonItem) {
        return new EventSummary(jsonItem);
    }
}
