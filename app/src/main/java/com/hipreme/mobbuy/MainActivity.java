package com.hipreme.mobbuy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hipreme.mobbuy.marvel.character.Character;
import com.hipreme.mobbuy.global.Storage;
import com.hipreme.mobbuy.global.UI;
import com.hipreme.mobbuy.utils.Digest;
import com.hipreme.mobbuy.marvel.MarvelAPI;
import com.hipreme.mobbuy.utils.Resources;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    ArrayList<Character> chars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Digest.startMD5();
        Resources.registerContext(this);
        UI.start();

        String myUrl = MarvelAPI.getFromPublicPath(R.string.marvel_characters);

        /*JSONUtils.getUrlJson(this, myUrl, new Callback<Void, JSONObject>() {
            @Override
            public Void execute(JSONObject param)
            {
                System.out.println(param);
                chars = Character.getCharacters(param);
                Storage.favoriteContent(chars.get(0));
                Storage.saveContent();
                return null;
            }
        });*/
        if(Storage.favoriteExists())
        {
            ArrayList<Character> chars = Storage.loadContent();
        }

    }
}
