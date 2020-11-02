package com.hipreme.mobbuy.marvel.layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.hipreme.mobbuy.R;
import com.hipreme.mobbuy.marvel.MarvelAPI;
import com.hipreme.mobbuy.marvel.character.Character;
import com.hipreme.mobbuy.marvel.character.Image;
import com.hipreme.mobbuy.marvel.character.summaries.MarvelSummary;
import com.hipreme.mobbuy.utils.Callback;
import com.hipreme.mobbuy.utils.ImageUtils;

public class ComicSeriesListView extends RecyclerView.Adapter<ComicSeriesListView.Comics_Series_View>
{
    Character character;
    Context context;
    protected boolean comic = false;
    public ComicSeriesListView(Character c, Context ctx, boolean isComic)
    {
        character = c;
        context = ctx;
        comic = isComic;
    }
    public boolean isComic(){return comic;} public boolean isSeries(){return !comic;}

    @NonNull
    @Override
    public Comics_Series_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comic_series_layout, parent, false);
        return new Comics_Series_View(v);
    }

    @Override
    public void onBindViewHolder(final @NonNull Comics_Series_View holder, final int position)
    {
        final MarvelSummary item;
        if(comic)
            item = character.comics.items.get(position);
        else
            item = character.series.items.get(position);



        item.tryLoadThumbnail(new Callback<Void, Image>() {
            @Override
            public Void execute(Image param)
            {

                ImageUtils.loadImageInto(context,
                        holder.summaryImage,
                        param.getImageUrl(),
                        true);
                System.out.println("Summary url: " + param.getImageUrl());
                return null;
            }
        });
        holder.summaryName.setText(item.name);
    }

    @Override
    public int getItemCount()
    {
        if(comic)
            return character.comics.items.size();
        else
            return character.series.items.size();
    }

    public static class Comics_Series_View extends RecyclerView.ViewHolder
    {
        public TextView summaryName;
        public ImageView summaryImage;

        public Comics_Series_View(View v)
        {
            super(v);


            summaryName = v.findViewById(R.id.txtComicsSeriesViewSummaryName);
            summaryImage = v.findViewById(R.id.imgComicsSeriesViewSummaryImage);


        }
    }
}
