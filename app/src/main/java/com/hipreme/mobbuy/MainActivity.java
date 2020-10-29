package com.hipreme.mobbuy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.hipreme.mobbuy.global.GlobalState;
import com.hipreme.mobbuy.global.NetworkManager;
import com.hipreme.mobbuy.marvel.character.Character;
import com.hipreme.mobbuy.global.Storage;
import com.hipreme.mobbuy.global.UI;
import com.hipreme.mobbuy.marvel.character.CharacterNavigator;
import com.hipreme.mobbuy.utils.Digest;
import com.hipreme.mobbuy.marvel.MarvelAPI;
import com.hipreme.mobbuy.utils.Resources;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalState.initialize(this);
        Resources.NetworkConnection t =Resources.getNetworkConnectionType();

        System.out.println(NetworkManager.hasInternet());
        //if(t == Resources.NetworkConnection.NONE)
        //{
            //Toast.makeText(this, "No internet connection found!", Toast.LENGTH_LONG).show();
        //}
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Storage.saveContent();
    }
}
