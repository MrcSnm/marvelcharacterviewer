package com.hipreme.mobbuy.marvel.layouts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hipreme.mobbuy.ItemViewActivity;
import com.hipreme.mobbuy.R;
import com.hipreme.mobbuy.global.GlobalState;
import com.hipreme.mobbuy.global.Storage;
import com.hipreme.mobbuy.marvel.character.Character;
import com.hipreme.mobbuy.marvel.character.CharacterNavigator;
import com.hipreme.mobbuy.marvel.widgets.FavoriteButton;

import java.util.ArrayList;
import java.util.Comparator;

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
        FavoriteButton.setupButton(holder.favoriteButton, c, this);

    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }


    protected void sort()
    {
        characterList.sort(new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                return o1.name.compareTo(o2.name);
            }
        });
    }

    public void addCharacters(ArrayList<Character> chars)
    {
        //Check if it is on favorites
        characterList.addAll(CharacterNavigator.getUnfavoritedCharacters(chars));
        sort();
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
        public Button favoriteButton;
        public CharacterListView charListView;
        public CharacterView(View view, final CharacterListView charListView)
        {
            super(view);
            charImage = view.findViewById(R.id.imgItemView);
            charName = view.findViewById(R.id.txtItemTitle);
            favoriteButton = view.findViewById(R.id.btnItemFavorite);
            this.charListView = charListView;



            view.setOnClickListener(new View.OnClickListener()
            {
                /**
                 * Got to the detail page
                 * @param v
                 */
                @Override
                public void onClick(View v)
                {

                    Intent intent = new Intent(v.getContext(), ItemViewActivity.class);
                    GlobalState.currentViewingCharacter = charListView.characterList.get(getAdapterPosition());
                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}
