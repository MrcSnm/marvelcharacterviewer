package com.hipreme.mobbuy.global;

import android.content.Context;
import android.net.Network;

import androidx.annotation.NonNull;
import com.hipreme.mobbuy.utils.Error;
import com.hipreme.mobbuy.marvel.character.CharacterNavigator;
import com.hipreme.mobbuy.utils.Digest;
import com.hipreme.mobbuy.utils.Resources;

public class GlobalState
{
    private static boolean init;
    public static void initialize(@NonNull Context ctx)
    {
        if(init)
        {
            Error.print("Tried to initialize GlobalState twice!");
            return;
        }
        init = true;
        Digest.startMD5();
        Resources.registerContext(ctx);
        UI.start();
        NetworkManager.startChecking(ctx);
        CharacterNavigator.loadFavorites();
    }

    public static void onPause()
    {
        NetworkManager.onPause();
    }
    public static void onResume()
    {
        NetworkManager.onResume();
    }
}
