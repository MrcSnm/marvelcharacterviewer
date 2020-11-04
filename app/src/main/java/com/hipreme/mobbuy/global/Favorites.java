package com.hipreme.mobbuy.global;

import com.hipreme.mobbuy.marvel.character.Character;
import com.hipreme.mobbuy.utils.Callback;
import com.hipreme.mobbuy.utils.FileUtils;
import com.hipreme.mobbuy.utils.Error;
import com.hipreme.mobbuy.utils.Storage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Favorites
{
    public static final String FAVORITE_FILENAME = "FavoritedCharacters.json";
    public static final String FAVORITE_EXTENDED_FILENAME= "FavoritedCharactersExtension.json";
    public static final String FAVORITES_NAME = "favorites";
    public static final String COMICS_NAME = "comics";
    public static final String SERIES_NAME = "series";

    private static String favoriteText = "";
    private static boolean isDirty = false;

    private static ArrayList<Character> favoritesJSON = new ArrayList<>(); //To be appended at the end of program

    /**
     * Does a basic check if favorite exists, if it exists, it's value is already assigned
     * @return
     */
    public static boolean favoriteExists()
    {
        favoriteText = FileUtils.readFileStreamed(FAVORITE_FILENAME);
        return !(favoriteText.equals(""));
    }

    public static boolean favoriteContent(Character c)
    {
        isDirty = true;
        c.toggleFavorite();
        if(favoritesJSON.contains(c))
        {
            favoritesJSON.remove(c);
            return false;
        }
        favoritesJSON.add(c);
        System.out.println(c.name + " was added to favorites");
        return true;
    }


    /**
     * This will be the base method, where it will save favorites, if the thumbnails weren't loaded,
     * they won't be favorited.
     * @return
     */
    public static boolean saveFavorites()
    {
        if(!isDirty)
            return false;
        StringBuilder toSave = new StringBuilder("{\"favorites\":[");

        for(int i = 0, len = favoritesJSON.size(); i <  len; i++)
        {
            Character c= favoritesJSON.get(i);
            toSave.append(c.objReference.toString());

            if(i+1 != len)
                toSave.append(",\n");
        }
        toSave.append("]}");

        saveFavoritesExtended();
        FileUtils.saveFileStreamed(FAVORITE_FILENAME, toSave.toString());
        isDirty = false;
        return true;
    }

    public static void saveFavoritesExtended()
    {
        //First check thumbs
        boolean scheduleStorageSave = false;
        for(int i = 0, len = favoritesJSON.size(); i <  len; i++)
        {
            Character c = favoritesJSON.get(i);
            for (int j = 0, len2 = c.comics.items.size(); j < len2; j++)
            {
                if (c.comics.items.get(j).resourceJSON == null)
                {
                    c.comics.items.get(j).tryLoadThumbnail(null, true);
                    scheduleStorageSave = true;
                }
            }
        }
        if(scheduleStorageSave)
        {
            Storage.setOnFinishLoading((param) ->
            {
                saveComicsSeries();
                return null;
            });
        }
        else
            saveComicsSeries();
    }



    protected static void saveComicsSeries()
    {
        StringBuilder comics = new StringBuilder("{\"comics\":[");
        StringBuilder series = new StringBuilder("\"series\":[");


        for(int i = 0, len = favoritesJSON.size(); i <  len; i++)
        {
            Character c = favoritesJSON.get(i);
            comics.append("\n[");
            for(int j = 0, len2 = c.comics.items.size(); j < len2; j++)
            {
                comics.append(c.comics.items.get(j).resourceJSON.toString());
                if(j+1 != len2)
                    comics.append(",\n");
            }
            if(i+1 != len)
                comics.append("\n],");
            else
                comics.append("\n]");
            series.append("\n[");
            for(int j = 0, len2 = c.series.items.size(); j < len2; j++)
            {
                series.append(c.series.items.get(j).resourceJSON.toString());
                if(j+1 != len2)
                    series.append(",\n");
            }
            if(i+1 != len)
                series.append("\n],");
            else
                series.append("\n]");
        }

        String toSave;

        toSave = comics.toString();
        toSave+= "\n],";
        toSave+= series.toString();
        toSave+="\n]}";

        FileUtils.saveFileStreamed(FAVORITE_EXTENDED_FILENAME, toSave);
    }

    public static ArrayList<Character> loadFavorites()
    {
        ArrayList<Character> ret;
        String json = (favoriteText == null || favoriteText.equals("")) ? FileUtils.readFileStreamed(FAVORITE_FILENAME) : favoriteText;
        try
        {
            JSONObject obj = new JSONObject(json);
            ret = Character.getCharactersRaw(obj.getJSONArray(FAVORITES_NAME));

            for(Character c : ret)
            {
                if(!c.isFavorited)
                    favoriteContent(c);
            }
            loadFavoritesExtended(ret);
            isDirty = false;
        }
        catch (Exception e){Error.print(e);return null;}
        return ret;
    }

    public static void loadFavoritesExtended(ArrayList<Character> chars)
    {
        if(!FileUtils.fileExists(FAVORITE_EXTENDED_FILENAME))
            return;
        try
        {
            JSONObject comicSeries = new JSONObject(FileUtils.readFileStreamed(FAVORITE_EXTENDED_FILENAME));
            for(int i = 0, len = chars.size(); i<len; i++)
            {
                Character c = chars.get(i);
                JSONArray comics = comicSeries.getJSONArray(COMICS_NAME).getJSONArray(i);
                JSONArray series = comicSeries.getJSONArray(SERIES_NAME).getJSONArray(i);
                for(int j = 0, len2 = c.comics.items.size(); j < len2; j++)
                    c.comics.items.get(j).loadSummary(comics.getJSONObject(j));
                for(int j = 0, len2 = c.series.items.size(); j < len2; j++)
                    c.series.items.get(j).loadSummary(series.getJSONObject(j));
            }

        }
        catch (Exception e){Error.print(e);}
    }

    /**
     * Gets the favorites, use loadFavorites instead for reading from file
     */
    public static ArrayList<Character> getFavorites(){return (ArrayList<Character>)favoritesJSON.clone();}
}
