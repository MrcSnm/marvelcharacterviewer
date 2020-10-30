package com.hipreme.mobbuy.marvel.character;

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

    public String getImageUrl()
    {
        //This code is required if the the manifest don't have the value
        //android:usesCleartextTraffic="true"
        /*int index;
        if((index = url.indexOf("http://")) != -1)
        {
            url = "https://"+ url.substring(index+"http://".length());
        }*/
        return url+"."+type;
    }

}
