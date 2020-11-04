package com.hipreme.mobbuy.utils;

import android.os.AsyncTask;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hipreme.mobbuy.global.NetworkManager;
import com.hipreme.mobbuy.utils.Storage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Web
{

    /**
     * @param url Target to remove parameters
     * @param paramsToChompKey Parameters to chomp, each seaparated by a ,
     * @return
     */
    public static String chompURLParams(String url, String paramsToChompKey)
    {
        if(paramsToChompKey != null && !paramsToChompKey.equals(""))
        {
            String[] params = paramsToChompKey.split(",");
            int paramsStartIndex = url.indexOf("?");
            for (String p : params) {
                int index = url.indexOf(p, paramsStartIndex);
                if (index == -1) {
                    Error.print("Param named '" + p + "' not found for the key '" + url + "'");
                    continue;
                }

                int cutToIndex = url.indexOf("&", index);
                if (cutToIndex == -1)
                    cutToIndex = url.length();
                url = url.substring(0, index) + url.substring((cutToIndex == url.length()) ? cutToIndex : cutToIndex + 1);
            }
        }
        return url;
    }

    /**
     * First parameter must be the url
     * @param params
     * @return
     */
    public static String chompURLParams(String... params)
    {
        String toChomp="";
        if(params.length > 2)
        {
            for(int i = 1, len = params.length; i < len; i++)
            {
                toChomp+=params[i];
                if(i+1 != len)
                    toChomp+=",";
            }
        }
        else if(params.length == 2)
            toChomp = params[1];
        return chompURLParams(params[0], toChomp);

    }

    //This class contains auto-caching utility
    public static class LoadURLTask<T> extends AsyncTask<String, String, String>
    {
        T resultStored;
        private boolean finishedTask;


        protected String checkCache(String... urlAndToChomp)
        {
            String url;
            if(urlAndToChomp.length >= 2)
                url = chompURLParams(urlAndToChomp);
            else
                url = urlAndToChomp[0];
            return Storage.readFromCache(FileUtils.linkToFile(url));
        }

        @Override
        protected String doInBackground(String... params)
        {
            String cache = checkCache(params);
            if(!cache.equals(""))
                return cache;
            //Check internet connection
            if(!NetworkManager.hasInternetConnection())
                return "";
            InputStream stream;
            URL link;
            BufferedReader r = null;
            try
            {
                link = new URL(params[0]);
                stream = link.openStream();
                r = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

                String ret = Stream.read(r);
                if(!ret.equals(""))
                {
                    String toSave = chompURLParams(params);
                    Storage.cacheContent(FileUtils.linkToFile(toSave), ret);
                }
                return ret;
            }
            catch(Exception e)
            {
                Error.print(e, JSONUtils.class);
                return null;
            }
            finally
            {
                if(r != null)
                {
                    try{r.close();}
                    catch(Exception e){Error.print(e);}
                }
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            finishedTask = true;
        }

        public boolean hasFinishedTask(){return finishedTask;}


    }


    public static void timeoutTask(@NonNull Handler h, @Nullable AsyncTask task, long millis, @Nullable Callback<Void, Void> onTimeout)
    {
        h.removeCallbacksAndMessages(null);
        h.postDelayed(() ->
        {
            if(task != null)
                task.cancel(true);
            if(onTimeout != null)
                onTimeout.execute(null);
        }, millis);
    }
    public static Handler timeoutTask(AsyncTask task, long millis, Callback<Void, Void> onTimeout)
    {
        Handler h = new Handler();
        timeoutTask(h, task, millis, onTimeout);
        return h;
    }

    public static Handler timeoutTask(AsyncTask task, long millis) {return timeoutTask(task, millis, null); }

}
