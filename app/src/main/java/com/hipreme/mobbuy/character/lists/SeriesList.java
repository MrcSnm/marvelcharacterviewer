package com.hipreme.mobbuy.character.lists;
import com.hipreme.mobbuy.character.summaries.SeriesSummary;

import org.json.JSONObject;

public class SeriesList extends MarvelList<SeriesSummary>
{
    public static final String SERIES = "series";
    public SeriesList(JSONObject o)
    {
        super(o, SERIES);
    }



}
