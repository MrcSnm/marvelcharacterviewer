package com.hipreme.mobbuy.marvel.layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hipreme.mobbuy.R;
import com.hipreme.mobbuy.global.Storage;
import com.hipreme.mobbuy.marvel.character.Character;
import com.hipreme.mobbuy.marvel.character.CharacterNavigator;
import com.hipreme.mobbuy.utils.Resources;

import java.util.ArrayList;

public class CharacterListView extends RecyclerView.Adapter<CharacterListView.CharacterView>
{
    ArrayList<Character> characterList;
    Context context;
    public CharacterListView(ArrayList<Character> chars, Context ctx)
    {
        context = ctx;
        characterList = chars;
    }


    @NonNull @Override public CharacterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new CharacterView(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterView holder, int i)
    {
        Character c = characterList.get(i);
        holder.charName.setText(c.name);
        Glide
            .with(context)
            .load(c.thumbnail.getImageUrl())
            .into(holder.charImage);
        holder.updateFavoriteButton(c);
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    public void addCharacters(ArrayList<Character> chars)
    {
        characterList.addAll(chars);
        notifyDataSetChanged();
    }

    public void setCharacters(ArrayList<Character> chars)
    {
        characterList = chars;
        notifyDataSetChanged();
    }

    public static class CharacterView extends RecyclerView.ViewHolder
    {
        ImageView charImage;
        TextView charName;
        Button favoriteButton;
        CharacterListView charListView;
        public CharacterView(View view, final CharacterListView charListView)
        {
            super(view);
            charImage = view.findViewById(R.id.imgItemView);
            charName = view.findViewById(R.id.txtItemTitle);
            favoriteButton = view.findViewById(R.id.btnItemFavorite);
            this.charListView = charListView;


            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    int pos = getAdapterPosition();
                    Character c = charListView.characterList.get(pos);
                    Storage.favoriteContent(c);
                    updateFavoriteButton(c);
                }
            });
        }

        public void updateFavoriteButton(Character c)
        {
            favoriteButton.setBackground(ResourcesCompat.getDrawable(
                    Resources.getRegisteredContext().getResources(),
                    (c.isFavorited) ? R.drawable.ic_star_yellow_24dp :
                            R.drawable.ic_star_border_black_24dp,
                    null));
        }
    }
}
