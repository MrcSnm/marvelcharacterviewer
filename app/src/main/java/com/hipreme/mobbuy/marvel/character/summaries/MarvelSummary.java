package com.hipreme.mobbuy.marvel.character.summaries;

import com.hipreme.mobbuy.marvel.MarvelAPI;
import com.hipreme.mobbuy.marvel.character.Image;
import com.hipreme.mobbuy.utils.Callback;
import com.hipreme.mobbuy.utils.Error;
import com.hipreme.mobbuy.utils.JSONUtils;

import org.json.JSONObject;

public class MarvelSummary
{
    public static final String RESOURCE_URI = "resourceURI";
    public static final String NAME = "name";


    //Follow that order
    public static final String URI_DATA = "data";
    public static final String URI_RESULTS = "results";
    public static final    int URI_INDEX = 0;
    public static final String URI_THUMBNAIL = "thumbnail";

    public JSONObject json;
    public String resourceURI;
    public String name;

    public Image thumbnail;

    protected boolean hasLoadedThumbnail = false;
    protected JSONUtils.JSONTask currentTask;

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

    public void tryLoadThumbnail(final Callback<Void, Image> onImageLoad)
    {
        if(currentTask != null)
        {
            currentTask.cancel(true);
            currentTask = null;
            tryLoadThumbnail(onImageLoad);
            return;
        }
        else if(thumbnail != null)
        {
            onImageLoad.execute(thumbnail);
        }
        else
        {
            currentTask = JSONUtils.getUrlJson(resourceURI + MarvelAPI.generateApiKeyString(), new Callback<Void, JSONObject>()
            {
                @Override
                public Void execute(JSONObject param)
                {
                    try
                    {
                        thumbnail = new Image(param.getJSONObject(URI_DATA)
                                .getJSONArray(URI_RESULTS)
                                .getJSONObject(URI_INDEX)
                                .getJSONObject(URI_THUMBNAIL));
                        hasLoadedThumbnail = true;
                        onImageLoad.execute(thumbnail);
                        currentTask = null;
                    }
                    catch (Exception e){Error.print(e);hasLoadedThumbnail =false;}
                    return null;
                }
            }, "ts,apikey,hash");
        }
    }


}