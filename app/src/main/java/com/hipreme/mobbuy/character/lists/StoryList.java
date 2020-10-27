package com.hipreme.mobbuy.character.lists;
import com.hipreme.mobbuy.character.summaries.StorySummary;

import org.json.JSONObject;

public class StoryList extends MarvelList<StorySummary>
{
    public static final String STORIES = "stories";
    public StoryList(JSONObject o){super(o, STORIES);}
}
