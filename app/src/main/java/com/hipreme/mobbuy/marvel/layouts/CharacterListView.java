package com.hipreme.mobbuy.marvel.layouts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hipreme.mobbuy.marvel.character.Character;

import java.util.ArrayList;

public class CharacterListView extends RecyclerView.Adapter<CharacterListView.CharacterView>
{
    ArrayList<Character> characterList;
    Context context;
    @NonNull
    @Override
    public CharacterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class CharacterView extends RecyclerView.ViewHolder
    {
        Character ref;
        public CharacterView(View view, Character c)
        {
            super(view);
            ref = c;
        }
    }
}
