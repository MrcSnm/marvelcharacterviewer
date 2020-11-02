package com.hipreme.mobbuy.marvel.widgets;

import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hipreme.mobbuy.R;
import com.hipreme.mobbuy.global.Storage;
import com.hipreme.mobbuy.marvel.character.Image;
import com.hipreme.mobbuy.marvel.character.summaries.ComicSummary;
import com.hipreme.mobbuy.marvel.character.summaries.SeriesSummary;
import com.hipreme.mobbuy.utils.Callback;
import com.hipreme.mobbuy.utils.FileUtils;
import com.hipreme.mobbuy.utils.Resources;
import com.hipreme.mobbuy.marvel.character.Character;

public class FavoriteButton
{

    public static void favoriteCharacter(Character c, Button b, @Nullable  RecyclerView.Adapter adapter)
    {
        Storage.favoriteContent(c);
        saveImagesAsUserData(c);
        updateButtonBackground(b, c);
        if(adapter != null)
            adapter.notifyDataSetChanged();

    }

    public static void setupButton(final Button btn, final Character c, @Nullable final RecyclerView.Adapter adapter)
    {
        updateButtonBackground(btn, c);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteCharacter(c, btn, adapter);
            }
        });
    }
    public static void updateButtonBackground(Button favoriteButton, Character c)
    {
        favoriteButton.setBackground(ResourcesCompat.getDrawable(
                Resources.getRegisteredContext().getResources(),
                (c.isFavorited) ? R.drawable.ic_star_yellow_24dp :
                        R.drawable.ic_star_border_black_24dp,
                null));
    }

    public static void saveImagesAsUserData(Character c)
    {
        FileUtils.saveBitmapFromURL(c.thumbnail.getImageUrl());

        for(ComicSummary cs : c.comics.items)
        {
            cs.tryLoadThumbnail(new Callback<Void, Image>() {
                @Override
                public Void execute(Image param)
                {
                    FileUtils.saveBitmapFromURL(param.getImageUrl());
                    return null;
                }
            });
        }

        for(SeriesSummary ss : c.series.items)
        {
            ss.tryLoadThumbnail(new Callback<Void, Image>() {
                @Override
                public Void execute(Image param)
                {
                    FileUtils.saveBitmapFromURL(param.getImageUrl());
                    return null;
                }
            });
        }
        FileUtils.saveBitmapFromURL(c.thumbnail.getImageUrl());

    }
}
