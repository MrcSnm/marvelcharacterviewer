package com.hipreme.mobbuy.marvel.character.lists;
import com.hipreme.mobbuy.marvel.character.summaries.SeriesSummary;

import org.json.JSONObject;

public class SeriesList extends MarvelList<SeriesSummary>
{
    @Override
    public SeriesSummary getSummary(JSONObject jsonItem) {
        return new SeriesSummary(jsonItem);
    }

    public static final String SERIES = "series";
    public SeriesList(JSONObject o)
    {
        super(o, SERIES);
    }



}
