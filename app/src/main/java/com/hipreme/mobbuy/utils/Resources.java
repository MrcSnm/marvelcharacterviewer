package com.hipreme.mobbuy.utils;

import android.content.Context;

public class Resources
{
    private static Context registeredContext;
    public static void registerContext(Context ctx){registeredContext = ctx;}

    public static String getString(int r_id)
    {
        if(registeredContext == null)
            Error.print("No Context is registered!");

        return registeredContext.getString(r_id);
    }

    public static Context getRegisteredContext(){return registeredContext;}
}
