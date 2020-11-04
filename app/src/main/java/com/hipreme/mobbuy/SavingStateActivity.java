package com.hipreme.mobbuy;

import androidx.appcompat.app.AppCompatActivity;

import com.hipreme.mobbuy.global.GlobalState;
import com.hipreme.mobbuy.global.Favorites;

public class SavingStateActivity extends AppCompatActivity
{
    @Override
    protected void onPause()
    {
        super.onPause();
        GlobalState.onPause();
        Favorites.saveFavorites();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        GlobalState.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Favorites.saveFavorites();
    }
}
