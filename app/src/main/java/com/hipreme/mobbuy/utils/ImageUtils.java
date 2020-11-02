package com.hipreme.mobbuy.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUtils
{

    public static void loadImageInto(Context context, ImageView target, String url, boolean useUserDataCache)
    {
        String url2 = FileUtils.linkToFile(url);
        if(useUserDataCache && FileUtils.fileExists(url2))
        {
            target.setImageBitmap(FileUtils.loadBitmap(url2));
        }
        else
            Glide
                .with(context)
                .load(url)
                .into(target);
    }

}
