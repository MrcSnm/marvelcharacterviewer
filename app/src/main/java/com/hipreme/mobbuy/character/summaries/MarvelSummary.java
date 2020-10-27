package com.hipreme.mobbuy.character.summaries;

import com.hipreme.mobbuy.utils.Error;

import org.json.JSONObject;

public class MarvelSummary
{
    public static final String RESOURCE_URI = "resourceURI";
    public static final String NAME = "name";
    public JSONObject json;
    public String resourceURI;
    public String name;

    /**
     * The parameter is already generated on the container class "MarvelList"
     * @param o
     */
    public MarvelSummary(JSONObject o)
    {
        json = o;
        try
        {
            resourceURI = o.getString(RESOURCE_URI);
            name = o.getString(NAME);
        }
        catch(Exception e){
            Error.print(e, MarvelSummary.class);}
    }

    public void start(){}
}