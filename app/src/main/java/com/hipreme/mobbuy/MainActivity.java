package com.hipreme.mobbuy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.hipreme.mobbuy.global.GlobalState;
import com.hipreme.mobbuy.global.NetworkManager;
import com.hipreme.mobbuy.marvel.character.Character;
import com.hipreme.mobbuy.global.Storage;
import com.hipreme.mobbuy.global.UI;
import com.hipreme.mobbuy.marvel.character.CharacterNavigator;
import com.hipreme.mobbuy.marvel.layouts.CharacterListView;
import com.hipreme.mobbuy.marvel.layouts.SpaceItemDecoration;
import com.hipreme.mobbuy.utils.Callback;
import com.hipreme.mobbuy.utils.Digest;
import com.hipreme.mobbuy.marvel.MarvelAPI;
import com.hipreme.mobbuy.utils.Resources;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected GridLayoutManager gridLayoutManager;
    protected CharacterListView characterListView;
    protected RadioGroup rgViewOptionsToggle;


    protected int totalItemCount;
    protected int visibleItemCount;
    protected int firstVisibleItemIndex;

    enum Options
    {
        CHARACTERS,
        FAVORITES
    }
    protected Options selectedOption;


    /*
        Guidelines for this class includes using the class from
        com.hipreme.mobbuy.character.CharacterNavigator

        CharacterNavigator is an access point for knowing how many
        characters has loaded, which characters are favorited, and provides
        callbacks for when the characters were loaded
     */

    /**
     * Make the view toggable, only one is able to be selected
     */
    static final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i)
        {
            //radioGroup.
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                view.setChecked(view.getId() == i);
            }
        }
    };


    /**
     * This method can only be used on character favorite,
     * as the togglebutton is not direct child from radiogroup
     * @param view
     */
    public void onToggleCharacterFavorite(View view)
    {
        RadioGroup rg  = findViewById(R.id.rgViewOptionsToggle);
        rg.check(view.getId());
    }

    protected void setFrameColor(boolean selected, Button btn, FrameLayout frame)
    {
        if(selected)
        {
            frame.setBackgroundColor(getResources().getColor(R.color.colorAccentDark, null));
            btn.setBackgroundColor(getResources().getColor(R.color.colorAccentDark, null));
        }
        else
        {
            frame.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));
            btn.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));
        }
    }

    protected void setFrameColor(boolean selected, int Button_id, int FrameLayout_id)
    {
        setFrameColor(selected, (Button)findViewById(Button_id), (FrameLayout)findViewById(FrameLayout_id));
    }

    protected void layoutInitialization()
    {
        rgViewOptionsToggle = findViewById(R.id.rgViewOptionsToggle);
        recyclerView = findViewById(R.id.characterListView);
        recyclerView.setLayoutManager(gridLayoutManager = new GridLayoutManager(this, 2));

        recyclerView.addItemDecoration(new SpaceItemDecoration(10));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy)
            {
                super.onScrolled(view, dx, dy);

                firstVisibleItemIndex = gridLayoutManager.findFirstVisibleItemPosition();
                visibleItemCount = gridLayoutManager.getChildCount();


                totalItemCount = gridLayoutManager.getItemCount();
                if(dy > 0) //Scroll up
                {
                    if(!CharacterNavigator.isLoading() && (totalItemCount - visibleItemCount) <= firstVisibleItemIndex+CharacterNavigator.LIMIT)
                    {
                        loadCharacters();
                    }
                }
            }
        });
        rgViewOptionsToggle.setOnCheckedChangeListener(ToggleListener);
        recyclerView.setHasFixedSize(true);

        CharacterNavigator.setProgressBar((ProgressBar)findViewById(R.id.mainProgressBar));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalState.initialize(this);


        layoutInitialization();
        CharacterNavigator.setOrderBy("name");

        CharacterNavigator.getCharactersFromOffset(new Callback<Void, ArrayList<Character>>() {
            @Override
            public Void execute(ArrayList<Character> param)
            {
                characterListView = new CharacterListView(param, MainActivity.this);
                recyclerView.setAdapter(characterListView); //Needs set adapter for the first load
                return null;
            }
        });
    }


    protected void loadCharacters()
    {
        CharacterNavigator.getCharactersFromOffset(new Callback<Void, ArrayList<Character>>() {
            @Override
            public Void execute(ArrayList<Character> param)
            {
                characterListView.addCharacters(param);
                return null;
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        GlobalState.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        GlobalState.onResume();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Storage.saveContent();
    }
}
