package com.hipreme.mobbuy.character.lists;
import com.hipreme.mobbuy.character.summaries.ComicSummary;

import org.json.JSONObject;

public class ComicList extends MarvelList<ComicSummary>
{
    public static final String COMICS = "comics";
    public ComicList(JSONObject o){super(o, COMICS);}

    @Override
    public ComicSummary getSummary(JSONObject jsonItem) {
        return new ComicSummary(jsonItem);
    }
}
