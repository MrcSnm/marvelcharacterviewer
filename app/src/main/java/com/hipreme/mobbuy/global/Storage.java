package com.hipreme.mobbuy.global;

import com.hipreme.mobbuy.marvel.character.Character;
import com.hipreme.mobbuy.utils.FileUtils;
import com.hipreme.mobbuy.utils.Error;

import org.json.JSONObject;

import java.util.ArrayList;

public class Storage
{
    public static final String FAVORITE_FILENAME = "FavoritedCharacters.json";
    public static final String FAVORITES_NAME = "favorites";
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

    public static boolean saveFavorites()
    {
        if(!isDirty)
            return false;
        StringBuilder toSave = new StringBuilder("{\"favorites\":[");

        for(int i = 0, len = favoritesJSON.size(); i <  len; i++)
        {
            toSave.append(favoritesJSON.get(i).objReference.toString());
            System.out.println("Favorited: " + favoritesJSON.get(i).name);

            if(i+1 != len)
                toSave.append("\n,");
        }
        toSave.append("]}");
        FileUtils.saveFileStreamed(FAVORITE_FILENAME, toSave.toString());
        isDirty = false;
        return true;
    }

    public static ArrayList<Character> loadFavorites()
    {
        ArrayList<Character> ret;
        String json = (favoriteText == null || favoriteText.equals("")) ? FileUtils.readFileStreamed(FAVORITE_FILENAME) : favoriteText;
        try
        {
            JSONObject obj = new JSONObject(json);
            ret = Character.getCharactersRaw(obj.getJSONArray(FAVORITES_NAME));

            System.out.println("This is the greatest line on the storage for checking what is happening");
            for(Character c : ret)
            {
                favoriteContent(c);
            }
            isDirty = false;
        }
        catch (Exception e){Error.print(e);return null;}
        return (ArrayList<Character>)ret.clone();
    }

    /**
     *
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


    /**
     * Gets the favorites, use loadFavorites instead for reading from file
     */
    public static ArrayList<Character> getFavorites(){return (ArrayList<Character>)favoritesJSON.clone();}
}
