package com.hipreme.mobbuy.marvel.character;


import android.view.View;
import android.widget.ProgressBar;

import com.hipreme.mobbuy.R;
import com.hipreme.mobbuy.global.Storage;
import com.hipreme.mobbuy.marvel.MarvelAPI;
import com.hipreme.mobbuy.utils.Callback;
import com.hipreme.mobbuy.utils.JSONUtils;
import com.hipreme.mobbuy.utils.Pagination;
import com.hipreme.mobbuy.utils.Range;
import com.hipreme.mobbuy.utils.Resources;

import org.json.JSONObject;

import java.util.ArrayList;

public class CharacterNavigator
{
    private static int CURRENT_OFFSET = 0;
    public static final int LIMIT = 20;
    private static JSONUtils.JSONTask currentTask;

    protected static ProgressBar pb;

    public static int getCurrentOffset(){return CURRENT_OFFSET;}
    protected static class CharacterRange
    {
        Range range;
        ArrayList<Character> characters = new ArrayList<>(LIMIT);

        public CharacterRange(ArrayList<Character> c, int offset)
        {
            range = new Range(offset, offset+LIMIT);
            characters.addAll(c);
        }
    }

    private static ArrayList<Character> favorites = new ArrayList<>(); //Will only ever be used if favorites exists

    public static ArrayList<Character> getFavorites(){return favorites;}
    private static ArrayList<CharacterRange> navigatedOffsets = new ArrayList<>();
    private static Pagination pagination = new Pagination(new Callback<String, String>() {
        @Override
        public String execute(String param) {
            return MarvelAPI.getFromPublicPath(param);
        }
    }, "offset", "limit", "orderBy");

    public static int hasLoadedOffset(int offset)
    {
        for(int i = 0, len = navigatedOffsets.size(); i < len; i++)
        {
            CharacterRange r = navigatedOffsets.get(i);
            if(r.range.isInRange(offset))
                return i;
        }
        return -1;
    }

    public static void setOrderBy(String orderBy){pagination.setOrderBy(orderBy);}

    public static ArrayList<Character> getLoadedCharacters()
    {
        ArrayList<Character> ret = new ArrayList<>();
        for(CharacterRange c : navigatedOffsets)
        {
            ret.addAll(c.characters);
        }
        return ret;
    }

    public static boolean isLoading()
    {
        return currentTask != null && !currentTask.hasFinishedTask();
    }

    public static void getCharactersFromOffset(final Callback<Void, ArrayList<Character>> onGet)
    {
        //int rangeIndex;
        //if((rangeIndex = hasLoadedOffset(CURRENT_OFFSET+1)) == -1)
        //{
            if(pb != null)
                pb.setVisibility(View.VISIBLE);
            final int currOffset = CURRENT_OFFSET;
            currentTask = JSONUtils.getUrlJson(pagination.getPath(Resources.getString(R.string.marvel_characters),
                        true, true, true), new Callback<Void, JSONObject>() {
                    @Override
                    public Void execute(JSONObject param)
                    {
                        ArrayList<Character> chars = Character.getCharacters(param);
                        navigatedOffsets.add(new CharacterRange(chars, currOffset));
                        onGet.execute(chars);
                        currentTask = null;
                        pb.setVisibility(View.GONE);
                        return null;
                    }
                });
        //}
        //else
          //  onGet.execute(navigatedOffsets.get(rangeIndex).characters);
        CURRENT_OFFSET+= LIMIT; //Needs to be instant for not having racing conditions
        pagination.setOffset(CURRENT_OFFSET);
    }


    public static void setProgressBar(ProgressBar progressBar){pb = progressBar;}

    public static void loadFavorites()
    {
        if(Storage.favoriteExists() && favorites == null)
        {
            favorites = Storage.loadContent();
        }
    }

}
