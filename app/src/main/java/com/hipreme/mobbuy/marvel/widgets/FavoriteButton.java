package com.hipreme.mobbuy.marvel.widgets;

import android.view.View;
import android.widget.Button;

import androidx.core.content.res.ResourcesCompat;

import com.hipreme.mobbuy.R;
import com.hipreme.mobbuy.global.Storage;
import com.hipreme.mobbuy.utils.Resources;
import com.hipreme.mobbuy.marvel.character.Character;

public class FavoriteButton
{

    public static void favoriteCharacter(Character c, Button b)
    {
        Storage.favoriteContent(c);
        updateButtonBackground(b, c);
    }

    public static void setupButton(final Button btn, final Character c)
    {
        updateButtonBackground(btn, c);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteCharacter(c, btn);
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
}
