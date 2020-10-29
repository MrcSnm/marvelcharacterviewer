package com.hipreme.mobbuy.global;

import com.hipreme.mobbuy.utils.Error;
import com.hipreme.mobbuy.utils.Resources;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.widget.Toast;

import java.util.ArrayList;

import static android.net.ConnectivityManager.NetworkCallback;

public class NetworkManager
{
    private static boolean hasConnection;
    private static ConnectivityManager cm;
    private static NetworkCallback defaultCb;
    protected static ArrayList<NetCallback> registeredCallbacks;


    protected static class NetCallback
    {
        String name;
        NetworkRequest req;
        NetworkCallback cb;

        public NetCallback(String name, NetworkRequest request, NetworkCallback cb)
        {
            this.name = name;
            this.req = request;
            this.cb = cb;
        }
        @Override
        public boolean equals(Object o)
        {
            if(o instanceof String)
                return equals((String) o);
            else if(o instanceof NetCallback)
                return equals((NetCallback)o);
            else if(o instanceof NetworkCallback)
                return equals((NetworkCallback)o);
            else
                return super.equals(o);
        }
        public boolean equals(NetCallback cb)
        {
            return cb.name.equals(this.name);
        }
        public boolean equals(NetworkCallback callback)
        {
            return callback.equals(cb);
        }
        public boolean equals(String str)
        {
            return str.equals(name);
        }
    }

    public static boolean hasInternetConnection(){return hasConnection;}



    public static void startChecking(Context ctx)
    {

        if(cm != null)
        {
            Error.print("Attempt to initialize NetworkManager twice");
            return;
        }
        cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        defaultCb = new NetworkCallback()
        {
            @Override
            public void onAvailable(Network net)
            {
                super.onAvailable(net);
                hasConnection = true;
                Toast.makeText(Resources.getRegisteredContext(), "Network available", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onLost(Network net)
            {
                super.onLost(net);
                hasConnection = false;
                Toast.makeText(Resources.getRegisteredContext(), "Network unavailable", Toast.LENGTH_LONG).show();
            }
        };

        cm.registerDefaultNetworkCallback(defaultCb);
    }
    /*public static boolean hasInternet()
    {
        Network network = cm.getActiveNetwork();
        if(network == null)
            return false;
        else
        {
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            return capabilities != null &&
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
        }
    }*/

    public static void registerNetworkCallback(String name, NetworkRequest req, NetworkCallback cb)
    {
        if(!hasInitialized("register NetworkCallback"))
            return;
        if(registeredCallbacks.contains(name))
        {
            Error.print("Attempt to register a NetworkCallback with a name registered before!");
            return;
        }
        registeredCallbacks.add(new NetCallback(name, req, cb));
        cm.registerNetworkCallback(req, cb);
    }
    private static boolean hasInitialized(String attempt)
    {
        if(cm == null)
            Error.print("Attempt to "+ attempt + " before starting NetworkManager");
        return cm != null;
    }

    public static void unregisterNetworkCallback(String name)
    {
        if(!hasInitialized("unregister NetworkCallback"))
            return;
        int reg = -1;
        for(int i = 0, len = registeredCallbacks.size(); i < len; i++)
        {
            if(registeredCallbacks.get(i).equals(name))
                reg = i;
        }

        if(reg != -1)
        {
            cm.unregisterNetworkCallback(registeredCallbacks.get(reg).cb);
            registeredCallbacks.remove(reg);
        }
    }


}
