package com.hipreme.mobbuy.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.hipreme.mobbuy.global.UI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Web
{
    public static class LoadURLTask<T> extends AsyncTask<String, String, String>
    {
        T resultStored;
        private boolean finishedTask;

        @Override
        protected String doInBackground(String... params)
        {
            InputStream stream;
            URL link;
            BufferedReader r = null;
            try
            {
                link = new URL(params[0]);
                stream = link.openStream();
                r = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                return Stream.read(r);
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
}
