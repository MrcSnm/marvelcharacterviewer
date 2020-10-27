package com.hipreme.mobbuy.character;

import com.hipreme.mobbuy.utils.Error;

import org.json.JSONObject;

public class Image
{
    public static final String URL = "path";
    public static final String TYPE = "extension";
    public String url;
    public String type;

    public Image(JSONObject o)
    {
        try
        {
            url = o.getString(URL);
            type = o.getString(TYPE);
        }
        catch (Exception e){Error.print(e, Image.class);}
    }

}
