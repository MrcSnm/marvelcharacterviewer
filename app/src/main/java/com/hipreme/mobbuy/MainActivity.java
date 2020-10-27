package com.hipreme.mobbuy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hipreme.mobbuy.global.UI;
import com.hipreme.mobbuy.utils.Callback;
import com.hipreme.mobbuy.utils.Digest;
import com.hipreme.mobbuy.utils.FileUtils;
import com.hipreme.mobbuy.utils.JSONUtils;
import com.hipreme.mobbuy.utils.MarvelAPI;
import com.hipreme.mobbuy.utils.Resources;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE


    ArrayList<com.hipreme.mobbuy.character.Character> chars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Digest.startMD5();
        Resources.registerContext(this);
        UI.start();

        String myUrl = MarvelAPI.getFromPublicPath(R.string.marvel_characters);

        JSONUtils.getUrlJson(this, myUrl, new Callback<Void, JSONObject>() {
            @Override
            public Void execute(JSONObject param)
            {
                System.out.println(param);
                chars = com.hipreme.mobbuy.character.Character.getCharacters(param);
                return null;
            }
        });

    }
}
