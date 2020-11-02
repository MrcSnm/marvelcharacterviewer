package com.hipreme.mobbuy.marvel.layouts;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SummaryView
{
   RecyclerView recycler;
   LinearLayoutManager linear;
   ComicSeriesListView listView;
   Context ctx;


   public SummaryView(ComicSeriesListView comicSeries, RecyclerView rv, TextView noText)
   {
       recycler = rv;
       ctx = comicSeries.context;
       rv.setLayoutManager(linear = new LinearLayoutManager(ctx, RecyclerView.HORIZONTAL, false));
       rv.addItemDecoration(new SpaceItemDecoration(10));
       rv.setHasFixedSize(true);
       rv.setAdapter(comicSeries);
       listView = comicSeries;
       if(comicSeries.getItemCount() == 0)
       {
           noText.setVisibility(View.VISIBLE);
           rv.setVisibility(View.GONE);
       }
   }

}
