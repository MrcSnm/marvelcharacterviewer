package com.hipreme.mobbuy.character.lists;

import com.hipreme.mobbuy.character.summaries.MarvelSummary;
import com.hipreme.mobbuy.utils.Error;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class MarvelList<T extends MarvelSummary>
{
    public static final String AVAILABLE = "available";
    public static final String RETURNED = "returned";
    public static final String COLLECTION_URI = "collectionURI";
    public static final String ITEMS = "items";

    public int available;
    public int returned;
    public String collectionURI;

    public JSONObject json;
    public ArrayList<T> items;

    public MarvelList(JSONObject o,  String entryPoint)
    {
        try
        {
            JSONObject entry = o.getJSONObject(entryPoint);
            this.json = entry;
            available = entry.getInt(AVAILABLE);
            returned  = entry.getInt(RETURNED);
            collectionURI = entry.getString(COLLECTION_URI);
            JSONArray arr = entry.getJSONArray(ITEMS);
            items = new ArrayList<>();
            for(int i = 0, len = arr.length(); i < len; i++)
            {
                T summary = getSummary(arr.getJSONObject(i));
                items.add(summary);
            }
        }
        catch(Exception e){Error.print(e, MarvelList.class);}
    }
    public abstract T getSummary(JSONObject jsonItem);

}
