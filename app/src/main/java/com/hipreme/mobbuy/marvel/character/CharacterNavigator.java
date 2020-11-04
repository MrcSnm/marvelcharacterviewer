package com.hipreme.mobbuy.marvel.character;


import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hipreme.mobbuy.R;
import com.hipreme.mobbuy.global.Favorites;
import com.hipreme.mobbuy.marvel.MarvelAPI;
import com.hipreme.mobbuy.utils.Callback;
import com.hipreme.mobbuy.utils.JSONUtils;
import com.hipreme.mobbuy.utils.Pagination;
import com.hipreme.mobbuy.utils.Range;
import com.hipreme.mobbuy.utils.Resources;
import com.hipreme.mobbuy.utils.Web;

import org.json.JSONObject;

import java.util.ArrayList;

public class CharacterNavigator
{
    private static int CURRENT_OFFSET = 0;
    public static final int LIMIT = 20;
    public static final long TIMEOUT = 30000; //Thirty seconds
    private static JSONUtils.JSONTask currentTask;

    protected static ProgressBar pb;
    protected static Handler handler = new Handler();

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

    public static void onDestroy()
    {
        if(currentTask != null)
        {
            currentTask.cancel(true);
            if(pb != null)
            {
                pb.setVisibility(View.GONE);
                Toast.makeText(pb.getContext(), "Connection timeout! Please try at another time", Toast.LENGTH_LONG).show();
            }
            currentTask = null;
        }
    }

    /**
     * Used for getting only those which are needed
     * @param charsRaw
     * @return
     */
    public static ArrayList<Character> getUnfavoritedCharacters(ArrayList<Character> charsRaw)
    {
        ArrayList<Character> ret = new ArrayList<>();
        ArrayList<Character> favs = Favorites.getFavorites();

        for(Character c : charsRaw)
        {
            if(!favs.contains(c))
                ret.add(c);
        }
        return ret;
    }

    /**
     * This function needs to be called surround by an if(!isLoading()) getCharactersFromOffset
     * The check could be inside from itself, but it is less performant and it is less flexible,
     * although the offset only increases if the loading was succesful
     * @param onGet
     */
    public static void getCharactersFromOffset(final Callback<Void, ArrayList<Character>> onGet, Callback<Void,Void> onTimeout)
    {
        if(pb != null)
            pb.setVisibility(View.VISIBLE);
        final int currOffset = CURRENT_OFFSET;
        currentTask = JSONUtils.getUrlJson(pagination.getPath(Resources.getString(R.string.marvel_characters),
                    true, true, true), new Callback<Void, JSONObject>() {
                @Override
                public Void execute(JSONObject param)
                {
                    ArrayList<Character> chars = Character.getCharacters(param);
                    if(chars != null)
                    {
                        navigatedOffsets.add(new CharacterRange(chars, currOffset));
                        onGet.execute(chars);
                        pagination.setOffset(CURRENT_OFFSET+= LIMIT); //Needs to be instant for not having racing conditions
                    }
                    pb.setVisibility(View.GONE);

                    currentTask = null;
                    handler.removeCallbacksAndMessages(null);
                    return null;
                }
            }, "ts,apikey,hash");

        handler.removeCallbacksAndMessages(null);
        Web.timeoutTask(handler,  currentTask, TIMEOUT, (param) ->
        {
            onDestroy();
            if(onTimeout!=null)
                onTimeout.execute(null);
            return null;
        });

    }
    public static void getCharactersFromOffset(final Callback<Void, ArrayList<Character>> onGet) {getCharactersFromOffset(onGet, null);}


    public static void setProgressBar(ProgressBar progressBar){pb = progressBar;}

}
