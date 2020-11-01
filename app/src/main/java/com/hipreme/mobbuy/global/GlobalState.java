package com.hipreme.mobbuy.global;

import android.content.Context;
import androidx.annotation.NonNull;
import com.hipreme.mobbuy.utils.Error;
import com.hipreme.mobbuy.utils.Digest;
import com.hipreme.mobbuy.utils.Resources;
import com.hipreme.mobbuy.marvel.character.Character;

public class GlobalState
{
    private static boolean init;
    public static Character currentViewingCharacter = null;

    public static void initialize(@NonNull Context ctx)
    {
        if(init)
        {
            Error.print("Tried to initialize GlobalState twice!");
            return;
        }
        init = true;
        //Initializes MD5 instance
        Digest.startMD5();

        //Register the context for further use
        Resources.registerContext(ctx);

        //Unused
        UI.start();

        //Setup connection type checkers
        NetworkManager.startChecking(ctx);
        //Load favorites from storage
        Storage.loadFavorites();
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
