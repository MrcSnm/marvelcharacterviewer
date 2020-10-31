package com.hipreme.mobbuy.marvel.character;

import com.hipreme.mobbuy.marvel.character.lists.ComicList;
import com.hipreme.mobbuy.marvel.character.lists.EventList;
import com.hipreme.mobbuy.marvel.character.lists.SeriesList;
import com.hipreme.mobbuy.marvel.character.lists.StoryList;
import com.hipreme.mobbuy.utils.Error;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Character
{
    public static final String THUMBNAIL = "thumbnail";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String RESOURCE_URI = "resourceURI";

    public static final String DATA = "data";
    public static final String RESULTS = "results";

    public long id;
    public String name;
    public String description;
    public String resourceURI;
    public Image thumbnail;
    public ComicList comics;
    public StoryList stories;
    public EventList events;
    public SeriesList series;

    //For serializing
    public JSONObject objReference;


    public boolean isFavorited = false;


    public void toggleFavorite(){isFavorited = !isFavorited;}


    public Character(JSONObject o)
    {
        try
        {
            objReference = o;
            id = o.getLong(ID);
            name = o.getString(NAME);
            description= o.getString(DESCRIPTION);
            resourceURI = o.getString(RESOURCE_URI);
            thumbnail = new Image(o.getJSONObject(THUMBNAIL));
            comics = new ComicList(o);
            stories = new StoryList(o);
            events = new EventList(o);
            series = new SeriesList(o);
        }
        catch (Exception e){Error.print(e);}

    }

    public static ArrayList<Character> getCharactersRaw(JSONArray apiArray)
    {
        try
        {
            ArrayList<Character> ret = new ArrayList<>();
            for(int i = 0, len = apiArray.length(); i < len; i++)
            {
                JSONObject chars = apiArray.getJSONObject(i);
                ret.add(new Character(chars));
            }
            return ret;
        }
        catch (Exception e)
        {
            Error.print(e, Character.class, "Error at serializing data");
        }
        return null;
    }
    public static ArrayList<Character> getCharacters(JSONObject apiObj)
    {
        try
        {
            JSONObject data = apiObj.getJSONObject(DATA);
            JSONArray results = data.getJSONArray(RESULTS);
            return getCharactersRaw(results);
        }
        catch (Exception e)
        {
            Error.print(e, Character.class, "Error at serializing data");
        }
        return null;
    }
}
