package com.hipreme.mobbuy.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.hipreme.mobbuy.R;

import java.util.ArrayList;

public class Storage
{
    private static ArrayList<String> loadingList = new ArrayList<String>();
    private static Callback onFinishLoading;
    private static int loadCount=0;
    private static ProgressBar progressBar;

    public static final int PROGRESS_BAR_ID = R.id.storageProgressBar;


    /**
     * Determinated progress bar!
     * @param pb
     */
    protected static void setProgressBar(ProgressBar pb)
    {
        progressBar = pb;
        pb.setIndeterminate(false);
    }

    public static void setProgressBar(Activity atv)
    {
        ProgressBar pb = atv.findViewById(PROGRESS_BAR_ID);
        if(pb == null)
        {
            Error.print("Your activity must contain a progress bar called 'storageProgressBar'");
            return;
        }
        setProgressBar(pb);
    }

    /**
     * @param name for the metadata
     * @return a ID for being called with finishLoading
     */
    public static int addToLoadList(String name)
    {
        loadingList.add(name);
        if(progressBar != null)
        {
            progressBar.setMax(loadingList.size());
            progressBar.setVisibility(View.VISIBLE);
        }
        return loadingList.size()-1;
    }

    public static void finishLoading(int loadID)
    {
        if(loadID >= 0)
        {
            loadCount++;
            if(loadCount==loadingList.size())
            {
                if(onFinishLoading != null)
                    onFinishLoading.execute(null);
                loadCount = 0;
                loadingList.clear();
            }
            if(progressBar != null)
            {
                if(loadCount==0)
                    progressBar.setVisibility(View.GONE);
                else
                    progressBar.setProgress(loadCount);
            }
        }
    }

    public static void setOnFinishLoading(final Callback<Void, Void> onFinish)
    {
        if(onFinishLoading != null)
        {
            final Callback cb = onFinishLoading;
            onFinishLoading = cb.concat(onFinish);
        }
        else
            onFinishLoading = onFinish;
    }


    /**
     * @param contentKey Cache name
     * @param contentValue Content of the cache
     */
    public static void cacheContent(String contentKey, String contentValue)
    {
        FileUtils.saveCacheFile(contentKey, contentValue);
    }

    public static String readFromCache(String contentKey)
    {
        return FileUtils.readCacheFile(contentKey);
    }
}
