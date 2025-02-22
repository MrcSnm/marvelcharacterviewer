package com.hipreme.mobbuy.marvel.character.summaries;

import com.hipreme.mobbuy.utils.Error;
import org.json.JSONObject;

public class StorySummary extends MarvelSummary
{
    public static final String TYPE = "type";
    public String type;
    /**
     * The parameter is already generated on the container class "MarvelList"
     * @param o
     */
    public StorySummary(JSONObject o)
    {
        super(o);
        try{type = json.getString(TYPE);}
        catch(Exception e){Error.print(e, StorySummary.class);}
    }
}
