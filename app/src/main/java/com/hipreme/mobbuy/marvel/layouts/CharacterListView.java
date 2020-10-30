package com.hipreme.mobbuy.marvel.layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hipreme.mobbuy.R;
import com.hipreme.mobbuy.marvel.character.Character;
import com.hipreme.mobbuy.marvel.character.Image;

import java.util.ArrayList;

public class CharacterListView extends RecyclerView.Adapter<CharacterListView.CharacterView>
{
    ArrayList<Character> characterList;
    int characterCreated;
    Context context;
    public CharacterListView(ArrayList<Character> chars, Context ctx)
    {
        context = ctx;
        characterList = chars;
    }


    @NonNull @Override public CharacterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new CharacterView(view);
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
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    public void addCharacters(ArrayList<Character> chars)
    {
        characterList.addAll(chars);
    }

    public static class CharacterView extends RecyclerView.ViewHolder
    {
        ImageView charImage;
        TextView charName;
        Button favoriteButton;
        public CharacterView(View view)
        {
            super(view);
            charImage = view.findViewById(R.id.imgItemView);
            charName = view.findViewById(R.id.txtItemTitle);
            favoriteButton = view.findViewById(R.id.btnItemFavorite);
        }
    }
}
