package com.hipreme.mobbuy.utils;

import android.util.Log;

import java.util.ArrayList;

public class Pagination
{
    Callback<String, String> basePathGetter;
    String offsetParamName = "offset";
    String maxParamName = "max";
    String orderByParamName = "orderBy";

    long currentOffset = 0;
    public long maxItems = 20;

    private ArrayList<String> acceptedOrderBy = null;
    private String orderBy;

    public Pagination(Callback<String, String> pathGetter)
    {
        basePathGetter = pathGetter;
    }

    public Pagination(Callback<String, String> pathGetter, String offsetName, String maxName, String orderName)
    {
        this(pathGetter);
        offsetParamName = offsetName;
        maxParamName = maxName;
        orderByParamName = orderName;
    }

    public void setAcceptedOrderValues(String[] values)
    {
        acceptedOrderBy = new ArrayList<>();
        for(String v : values)
            acceptedOrderBy.add(v);
    }
    public void setAcceptedOrderValues(ArrayList<String> values)
    {
        acceptedOrderBy = values;
    }

    public String getPath(String path){return basePathGetter.execute(path);}
    public String getPath(String path, boolean useOffset, boolean useMax, boolean useOrder)
    {
        String p = getPath(path);

        if(useOffset)
            p+= ((p.lastIndexOf("?") != -1) ? "&" : "?") +offsetParamName+"="+currentOffset;
        if(useMax && maxItems != 0)
            p+= "&"+maxParamName+"="+maxItems;
        if(useOrder && !orderBy.equals(null))
            p+= "&"+orderByParamName+"="+orderBy;

        Log.i("Pagination", p);
        return p;
    }
    public void setOffset(long offset)
    {
        this.currentOffset = offset;
    }
    public void addOffset(long offset)
    {
        setOffset(this.currentOffset+offset);
    }
    public long getOffset(){return this.currentOffset;}

    public boolean setOrderBy(String order)
    {
        if(acceptedOrderBy != null && !acceptedOrderBy.contains(order))
            return false;

        orderBy = order;
        return true;
    }



}
