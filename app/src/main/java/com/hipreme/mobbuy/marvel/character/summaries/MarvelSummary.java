package com.hipreme.mobbuy.marvel.character.summaries;

import com.hipreme.mobbuy.marvel.MarvelAPI;
import com.hipreme.mobbuy.marvel.character.Image;
import com.hipreme.mobbuy.utils.Callback;
import com.hipreme.mobbuy.utils.Error;
import com.hipreme.mobbuy.utils.JSONUtils;
import com.hipreme.mobbuy.utils.Storage;

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
    public JSONObject resourceJSON;
    public String resourceURI;
    public String name;

    public Image thumbnail;

    protected boolean hasLoadedThumbnail = false;
    protected JSONUtils.JSONTask currentTask;
    protected Callback<Void, Image> currentOnLoad;

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

    public void loadSummary(JSONObject summaryObj)
    {
        try
        {
            thumbnail = new Image(summaryObj.getJSONObject(URI_DATA)
                    .getJSONArray(URI_RESULTS)
                    .getJSONObject(URI_INDEX)
                    .getJSONObject(URI_THUMBNAIL));
            hasLoadedThumbnail = true;
            resourceJSON = summaryObj;
        }
        catch (Exception e){Error.print(e);hasLoadedThumbnail= false;}
    }

    public void tryLoadThumbnail(final Callback<Void, Image> onImageLoad, boolean storeOnStorage)
    {
        if(currentTask != null)
        {
            currentTask.cancel(true);
            currentTask = null;

            Callback cb = (onImageLoad == null) ? currentOnLoad : onImageLoad.concat(currentOnLoad);
            tryLoadThumbnail(cb, storeOnStorage);
        }
        else if(thumbnail != null) //Ignore storing
            onImageLoad.execute(thumbnail);
        else
        {
            final int storeValue;
            if(storeOnStorage)
                storeValue = Storage.addToLoadList("Loading thumbnail");
            else
                storeValue = -1; //Same as null
            currentOnLoad = onImageLoad;
            currentTask = JSONUtils.getUrlJson(resourceURI + MarvelAPI.generateApiKeyString(), (JSONObject param) ->
            {
                loadSummary(param);
                if(onImageLoad != null)
                    onImageLoad.execute(thumbnail);
                Storage.finishLoading(storeValue);
                currentTask = null;
                return null;
            }, "ts,apikey,hash");
        }
    }

    public void tryLoadThumbnail(final Callback<Void, Image> onImageLoad)
    {
        tryLoadThumbnail(onImageLoad, false);
    }

}