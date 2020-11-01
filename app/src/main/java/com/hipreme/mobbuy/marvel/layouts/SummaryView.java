package com.hipreme.mobbuy.marvel.layouts;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SummaryView
{
   RecyclerView recycler;
   GridLayoutManager grid;
   ComicSeriesListView listView;
   Context ctx;


   public SummaryView(ComicSeriesListView comicSeries, RecyclerView rv)
   {
       recycler = rv;
       ctx = comicSeries.context;
       rv.setLayoutManager(grid = new GridLayoutManager(ctx, 2));
       rv.addItemDecoration(new SpaceItemDecoration(10));
       rv.setHasFixedSize(true);
       rv.setAdapter(comicSeries);
       listView = comicSeries;
   }

}
