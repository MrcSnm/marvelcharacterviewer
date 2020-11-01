package com.hipreme.mobbuy.utils;


import org.json.JSONException;
import org.json.JSONObject;


public class JSONUtils
{
    public static class JSONTask extends Web.LoadURLTask<JSONObject>
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
            if(!result.equals(""))
            {
                try{resultStored= new JSONObject(result);}
                catch(JSONException e){Error.print(e, JSONUtils.class);}
                if(onDataLoad != null)
                    onDataLoad.execute(resultStored);
            }
        }
    }
    public static JSONTask getUrlJson(String url, Callback<Void, JSONObject> cb, String... cacheKeyChompParams)
    {
        String[] p = new String[cacheKeyChompParams.length+1];
        p[0] = url;
        for(int i = 1, len = p.length; i < len; i++)
            p[i] = cacheKeyChompParams[i-1];
        return (JSONTask)(new JSONTask(cb).execute(p));
    }

}
