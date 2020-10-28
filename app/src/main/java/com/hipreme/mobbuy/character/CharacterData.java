package com.hipreme.mobbuy.character;

import org.json.JSONObject;

public class CharacterData
{
    public JSONObject obj;

    public CharacterData(Character c)
    {
        obj = c.objReference;
    }
}
