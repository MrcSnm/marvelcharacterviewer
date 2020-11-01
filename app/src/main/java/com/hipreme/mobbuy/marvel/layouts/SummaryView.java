package com.hipreme.mobbuy.marvel.layouts;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SummaryView
{
   RecyclerView recycler;
   LinearLayoutManager linear;
   ComicSeriesListView listView;
   Context ctx;


   public SummaryView(ComicSeriesListView comicSeries, RecyclerView rv)
   {
       recycler = rv;
       ctx = comicSeries.context;
       rv.setLayoutManager(linear = new LinearLayoutManager(ctx, RecyclerView.HORIZONTAL, false));
       rv.addItemDecoration(new SpaceItemDecoration(10));
       rv.setHasFixedSize(true);
       rv.setAdapter(comicSeries);
       listView = comicSeries;
   }

}
