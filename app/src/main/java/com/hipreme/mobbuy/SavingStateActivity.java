package com.hipreme.mobbuy;

import androidx.appcompat.app.AppCompatActivity;

import com.hipreme.mobbuy.global.GlobalState;
import com.hipreme.mobbuy.global.Storage;

public class SavingStateActivity extends AppCompatActivity
{
    @Override
    protected void onPause()
    {
        super.onPause();
        GlobalState.onPause();
        Storage.saveFavorites();
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
        Storage.saveFavorites();
    }
}
