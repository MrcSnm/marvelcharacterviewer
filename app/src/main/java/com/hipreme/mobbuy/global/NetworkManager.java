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
    private static boolean isMobileData;
    private static boolean isWifi;
    private static boolean isEthernet;

    private static ConnectivityManager cm;
    private static NetworkCallback defaultCb;
    protected static ArrayList<NetCallback> registeredCallbacks;

    protected static ArrayList<NetCallback> pausedCallbacks;


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
    public static boolean isOnMobileData(){return hasConnection && isMobileData;}
    public static boolean isOnEthernet(){return hasConnection && isEthernet;}
    public static boolean isOnWifi(){return hasConnection && isWifi;}



    public static void startChecking(Context ctx)
    {

        if(cm != null)
        {
            Error.print("Attempt to initialize NetworkManager twice");
            return;
        }
        registeredCallbacks = new ArrayList<>();
        pausedCallbacks = new ArrayList<>();
        cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.registerDefaultNetworkCallback(defaultCb = new NetworkCallback()
        {
            @Override
            public void onAvailable(Network net)
            {
                super.onAvailable(net);
                hasConnection = true;
                //Toast.makeText(Resources.getRegisteredContext(), "Network available", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onLost(Network net)
            {
                super.onLost(net);
                hasConnection = false;
                Toast.makeText(Resources.getRegisteredContext(), "Network unavailable please try reconnecting", Toast.LENGTH_LONG).show();
            }
        });
        registerNetworkCallback("__MobileDataChecker__", new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).build(), new
                NetworkCallback()
                {
                    @Override
                    public void onAvailable(Network net){super.onAvailable(net);isMobileData = true;}
                    @Override
                    public void onLost(Network net){super.onLost(net);isMobileData = false;}
                });
        registerNetworkCallback("__WifiChecker__", new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build(), new
                NetworkCallback()
                {
                    @Override
                    public void onAvailable(Network net){super.onAvailable(net);isWifi= true;}
                    @Override
                    public void onLost(Network net){super.onLost(net);isWifi= false;}
                });
        registerNetworkCallback("__EthernetChecker__", new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET).build(), new
                NetworkCallback()
                {
                    @Override
                    public void onAvailable(Network net){super.onAvailable(net);isEthernet= true;}
                    @Override
                    public void onLost(Network net){super.onLost(net);isEthernet= false;}
                });
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

    protected static int getRegisteredCallbackIndex(String name)
    {
        int reg = -1;
        for(int i = 0, len = registeredCallbacks.size(); i < len; i++)
        {
            if(registeredCallbacks.get(i).equals(name))
            {
                reg = i;
                break;
            }
        }
        return reg;
    }

    public static void pauseCallback(String name)
    {
        if(!hasInitialized("pause callback"))
            return;
        int index = getRegisteredCallbackIndex(name);
        if(index != -1)
        {
            NetCallback cb = registeredCallbacks.get(index);
            cm.unregisterNetworkCallback(cb.cb);
            registeredCallbacks.remove(cb);
            pausedCallbacks.add(cb);
        }
    }

    public static void unregisterNetworkCallback(String name)
    {
        if(!hasInitialized("unregister NetworkCallback"))
            return;
        int reg = getRegisteredCallbackIndex(name);
        if(reg != -1)
        {
            cm.unregisterNetworkCallback(registeredCallbacks.get(reg).cb);
            registeredCallbacks.remove(reg);
        }
    }

    /**
     * Pauses every registered callback
     */
    public static void onPause()
    {
        while(registeredCallbacks.size() > 0)
            pauseCallback(registeredCallbacks.get(0).name);
    }

    public static void onResume()
    {
        while(pausedCallbacks.size() > 0)
        {
            NetCallback cb = pausedCallbacks.get(0);
            pausedCallbacks.remove(cb);
            registerNetworkCallback(cb.name, cb.req, cb.cb);
        }
    }


}
