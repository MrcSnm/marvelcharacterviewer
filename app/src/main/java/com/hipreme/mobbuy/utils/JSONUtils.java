package com.hipreme.mobbuy.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;


public class JSONUtils
{
    private static class JSONTask extends Web.LoadURLTask<JSONObject>
    {
        Callback<Void, JSONObject> onDataLoad;
        public JSONTask(Callback<Void, JSONObject> cb)
        {
            onDataLoad = cb;
        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            try{resultStored= new JSONObject(result);}
            catch(JSONException e){Error.print(e, JSONUtils.class);}
            if(onDataLoad != null)
                onDataLoad.execute(resultStored);
        }
    }
    public static JSONTask getUrlJson(String url, Callback<Void, JSONObject> cb)
    {
        return (JSONTask)(new JSONTask(cb).execute(url));
    }

}
