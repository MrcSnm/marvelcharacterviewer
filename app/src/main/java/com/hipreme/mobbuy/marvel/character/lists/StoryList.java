package com.hipreme.mobbuy.marvel.character.lists;
import com.hipreme.mobbuy.marvel.character.summaries.StorySummary;

import org.json.JSONObject;

public class StoryList extends MarvelList<StorySummary>
{
    public static final String STORIES = "stories";

    @Override
    public StorySummary getSummary(JSONObject jsonItem) {
        return new StorySummary(jsonItem);
    }

    public StoryList(JSONObject o){super(o, STORIES);}
}
