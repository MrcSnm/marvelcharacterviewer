package com.hipreme.mobbuy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileUtils
{
    public static boolean saveFileStreamed(String filename, String content)
    {
        Context ctx = Resources.getRegisteredContext();
        try
        {
            FileOutputStream f = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            f.write(content.getBytes());
            return true;
        }
        catch(Exception e){Error.print(e, FileUtils.class, "Error at stream save"); return false;}
    }

    public static String readFileStreamed(String filename)
    {
        Context ctx = Resources.getRegisteredContext();
        if(!FileUtils.fileExists(filename))
            return "";
        try
        {
            FileInputStream f = ctx.openFileInput(filename);
            InputStreamReader input = new InputStreamReader(f);
            StringBuilder bd = new StringBuilder();

            BufferedReader reader = new BufferedReader(input);
            String line;

            while((line = reader.readLine()) != null)
                bd.append(line).append("\n");
            return bd.toString();
        }
        catch (Exception e){Error.print(e, FileUtils.class, "Could not read file " + filename);return "";}
    }
    public static String[] getFiles(){return Resources.getRegisteredContext().fileList();}

    public static boolean fileExists(String filename)
    {
        for(String f : getFiles())
        {
            if(filename.equals(f))
                return true;
        }
        return false;
    }


    public static String linkToFile(String link)
    {
        String file;
        file = link.replaceAll(":", "#");
        file = file.replaceAll("/", "%");
        file = file.replaceAll("\\?", "!");

        assert(fileToLink(file).equals(link)):
        "File to link does not return the same link generated on linkToFile";
        return file;
    }

    public static String fileToLink(String file)
    {
        String link;
        link = file.replaceAll("%", "/");
        link = link.replaceAll("#", ":");
        link = link.replaceAll("!", "?");

        assert(linkToFile(link).equals(file)):
                "Link to file does not return the same link generated on fileToLink";
        return link;
    }

    public static void saveCacheFile(String cacheFileName, String content)
    {
        try
        {
            File f = new File(Resources.getRegisteredContext().getCacheDir(), cacheFileName);

            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(content);
            writer.close();
        }
        catch (Exception e){Error.print(e, FileUtils.class, "Could not create cache file");}
    }
    public static String readCacheFile(String cacheFileName)
    {
        Context ctx = Resources.getRegisteredContext();
        File cacheFile = new File(ctx.getCacheDir(), cacheFileName);
        if(cacheFile.exists())
        {
            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(cacheFile));
                String line;
                StringBuilder bd = new StringBuilder();
                while((line = reader.readLine()) != null)
                    bd.append(line).append("\n");
                reader.close();
                return bd.toString();
           }
           catch (Exception e){Error.print(e, FileUtils.class, "Could not read cache");}
        }
        return "";
    }


    public static void saveBitmapFromURL(final String url)
    {
        Glide
            .with(Resources.getRegisteredContext())
            .asBitmap()
            .load(url)
            .into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    saveBitmap(linkToFile(url), resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
    }

    public static Bitmap loadBitmap(String filename)
    {
        if(fileExists(filename))
            return BitmapFactory.decodeFile(Resources.getRegisteredContext().getFilesDir()+"/"+filename);
        else

            return null;
    }


    /**
     * Glide is a dependency
     * @param b
     */
    public static boolean saveBitmap(String filename, Bitmap b)
    {
        FileOutputStream stream;
        Context ctx = Resources.getRegisteredContext();
        try
        {
            stream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
        }
        catch (Exception e){Error.print(e);}
        return false;
    }


}
