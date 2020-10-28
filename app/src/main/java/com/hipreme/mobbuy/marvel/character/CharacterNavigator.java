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
    private ArrayList<Long> navigatedIDs;
    private ArrayList<Range> navigatedOffsets;

    public ArrayList<Character> loadedCharacters;

    public static int CURRENT_OFFSET = 0;
    public static int LIMIT = 20;

    public Pagination pagination;

    public CharacterNavigator()
    {
        navigatedIDs = new ArrayList<>();
        loadedCharacters = new ArrayList<>();
        pagination = new Pagination(new Callback<String, String>() {
            @Override
            public String execute(String param) {
                return MarvelAPI.getFromPublicPath(param);
            }
        }, "offset", "limit", "orderBy");
    }

    public void getCharactersFromOffset(final Callback<Void, ArrayList<Character>> onGet)
    {
        JSONUtils.getUrlJson(Resources.getRegisteredContext(),
                pagination.getPath(Resources.getString(R.string.marvel_characters),
                        true, true, true), new Callback<Void, JSONObject>() {
            @Override
            public Void execute(JSONObject param)
            {
                ArrayList<Character> chars = Character.getCharacters(param);
                loadedCharacters.addAll(chars);
                Storage.saveContent();
                onGet.execute(chars);
                return null;
            }
        });
        CURRENT_OFFSET+= LIMIT; //Needs to be instant for not having racing conditions
    }

}
