package com.hipreme.mobbuy.marvel.character;


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
    private static final int LIMIT = 20;

    public static int getCurrentOffset(){return CURRENT_OFFSET;}
    protected static class CharacterRange
    {
        Range range;
        ArrayList<Character> characters = new ArrayList<>(LIMIT);

        public CharacterRange(ArrayList<Character> c)
        {
            range = new Range(CURRENT_OFFSET, CURRENT_OFFSET+LIMIT);
            characters.addAll(c);
        }
    }

    private static ArrayList<Character> favorites = null; //Will only ever be used if favorites exists

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

    public static void getCharactersFromOffset(final Callback<Void, ArrayList<Character>> onGet)
    {
        int rangeIndex;
        if((rangeIndex = hasLoadedOffset(CURRENT_OFFSET)) == -1)
        {
            JSONUtils.getUrlJson(Resources.getRegisteredContext(),
                pagination.getPath(Resources.getString(R.string.marvel_characters),
                        true, true, true), new Callback<Void, JSONObject>() {
                    @Override
                    public Void execute(JSONObject param)
                    {
                        ArrayList<Character> chars = Character.getCharacters(param);
                        navigatedOffsets.add(new CharacterRange(chars));
                        onGet.execute(chars);
                        return null;
                    }
                });
        }
        else
            onGet.execute(navigatedOffsets.get(rangeIndex).characters);
        CURRENT_OFFSET+= LIMIT; //Needs to be instant for not having racing conditions
    }

    public static void loadFavorites()
    {
        if(Storage.favoriteExists() && favorites == null)
        {
            favorites = Storage.loadContent();
        }
    }

}
