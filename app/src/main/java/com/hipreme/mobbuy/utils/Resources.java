package com.hipreme.mobbuy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.hipreme.mobbuy.global.NetworkManager;

public class Resources
{
    private static boolean hasConnection;
    private static Context registeredContext;

    public enum NetworkConnection
    {
        NONE,
        MOBILE,
        WIFI,
        ETHERNET
    }
    public static void registerContext(Context ctx){registeredContext = ctx;}

    public static String getString(int r_id)
    {
        if(registeredContext == null)
            Error.print("No Context is registered!");
        return registeredContext.getString(r_id);
    }
    public static Context getRegisteredContext(){return registeredContext;}


    private static boolean hasNetworkConnection()
    {
        if(registeredContext != null)
        {
            ConnectivityManager cm = (ConnectivityManager)registeredContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        else
            Error.print("No registered context found!");
        return false;
    }


    private static boolean hasInternetConnection()
    {
        ConnectivityManager cm = (ConnectivityManager)registeredContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        cm.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback()
        {
          @Override
          public void onAvailable(Network network)
          {
              hasConnection = true;
          }
          @Override
          public void onLost(Network network)
          {
              hasConnection = false;
          }
        });
        return hasConnection;
    }

    public static NetworkConnection getNetworkConnectionType()
    {
        if(registeredContext != null)
        {
            if(NetworkManager.isOnWifi())
                return NetworkConnection.WIFI;
            else if(NetworkManager.isOnEthernet())
                return NetworkConnection.ETHERNET;
            else if(NetworkManager.isOnMobileData())
                return NetworkConnection.MOBILE;
        }
        return NetworkConnection.NONE;
    }

}
