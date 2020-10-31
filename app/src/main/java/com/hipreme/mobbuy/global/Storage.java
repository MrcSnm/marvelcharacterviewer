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

    private static ArrayList<Character> favoritesJSON = new ArrayList<>(); //To be appended at the end of program

    /**
     * Does a basic check if favorite exists, if it exists, it's value is already assigned
     * @return
     */
    public static boolean favoriteExists()
    {
        return !(favoriteText = FileUtils.readFileStreamed(FAVORITE_FILENAME)).equals("");
    }

    public static boolean favoriteContent(Character c)
    {
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

    public static boolean saveContent()
    {
        StringBuilder toSave = new StringBuilder("{\nfavorites:[");

        for(int i = 0, len = favoritesJSON.size(); i <  len; i++)
        {
            toSave.append(favoritesJSON.get(i).objReference.toString());
            System.out.println("Favorited: " + favoritesJSON.get(i).name);

            if(i+1 != len)
                toSave.append("\n,");
        }
        toSave.append("]}");
        FileUtils.saveFileStreamed(FAVORITE_FILENAME, toSave.toString());
        return true;
    }

    public static ArrayList<Character> loadContent()
    {
        ArrayList<Character> ret;
        String json = (favoriteText == null) ? FileUtils.readFileStreamed(FAVORITE_FILENAME) : favoriteText;
        try
        {
            JSONObject obj = new JSONObject(json);
            favoritesJSON = ret = Character.getCharactersRaw(obj.getJSONArray(FAVORITES_NAME));

            for(Character c : favoritesJSON)
            {
                System.out.println(c.name + " was loaded");
                favoriteContent(c);
            }
        }
        catch (Exception e){Error.print(e);return null;}

        return ret;

    }
}
