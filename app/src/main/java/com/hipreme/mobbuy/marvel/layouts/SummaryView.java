package com.hipreme.mobbuy.marvel.layouts;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hipreme.mobbuy.R;

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
       listView = comicSeries;
   }

}
