package com.hipreme.mobbuy;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hipreme.mobbuy.global.Favorites;
import com.hipreme.mobbuy.global.GlobalState;
import com.hipreme.mobbuy.marvel.character.Character;
import com.hipreme.mobbuy.global.UI;
import com.hipreme.mobbuy.marvel.character.CharacterNavigator;
import com.hipreme.mobbuy.marvel.layouts.CharacterListView;
import com.hipreme.mobbuy.marvel.layouts.SpaceItemDecoration;
import com.hipreme.mobbuy.marvel.widgets.FavoriteButton;
import com.hipreme.mobbuy.utils.Callback;

import java.util.ArrayList;

import static com.hipreme.mobbuy.global.GlobalState.currentViewingCharacter;

public class MainActivity extends SavingStateActivity{

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

    protected ArrayList<ToggleButton> rgViewOptionToggleGroup;


    /*
        Guidelines for this class includes using the class from
        com.hipreme.mobbuy.character.CharacterNavigator

        CharacterNavigator is an access point for knowing how many
        characters has loaded, which characters are favorited, and provides
        callbacks for when the characters were loaded
     */



    @Override
    protected void onResume()
    {
        super.onResume();
        updateRecyclerView(); //-> Updates recyclerview based on other activities;
    }


    /**
     * This method is a base method for activating character toggle and
     * favorite toggle, each one must call this method first to activate
     * radio group check
     * @param view
     */
    protected void onToggleCharacterFavorite(View view)
    {
        RadioGroup rg  = findViewById(R.id.rgViewOptionsToggle);
        rg.check(0);
        rg.check(view.getId());
    }

    public void onToggleCharacter(View v)
    {
        onToggleCharacterFavorite(v);
        selectedOption = Options.CHARACTERS;
        characterListView.setCharacters(CharacterNavigator.getLoadedCharacters());
    }
    public void onToggleFavorite(View v)
    {
        onToggleCharacterFavorite(v);
        selectedOption = Options.FAVORITES;
        ArrayList<Character> favs = Favorites.getFavorites();

        characterListView.setCharacters(favs);

    }


    protected void layoutInitialization()
    {
        rgViewOptionsToggle = findViewById(R.id.rgViewOptionsToggle);
        rgViewOptionToggleGroup = new ArrayList<>();

        rgViewOptionToggleGroup.add((ToggleButton)findViewById(R.id.btnCharacter));
        rgViewOptionToggleGroup.add((ToggleButton)findViewById(R.id.btnFavorites));
        recyclerView = findViewById(R.id.characterListView);
        recyclerView.setLayoutManager(gridLayoutManager = new GridLayoutManager(this, 2));

        recyclerView.addItemDecoration(new SpaceItemDecoration(10));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy)
            {
                super.onScrolled(view, dx, dy);
                if(selectedOption == Options.FAVORITES)
                    return;
                firstVisibleItemIndex = gridLayoutManager.findFirstVisibleItemPosition();
                visibleItemCount = gridLayoutManager.getChildCount();


                totalItemCount = gridLayoutManager.getItemCount();
                if(dy > 0) //Scroll up
                {
                    if(!CharacterNavigator.isLoading() && (firstVisibleItemIndex+ CharacterNavigator.LIMIT > totalItemCount))
                        loadCharacters();
                }
            }
        });

        /**
         * Make the view toggable, only one is able to be selected
         */
        rgViewOptionsToggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, final int i)
            {
                for (int j = 0; j < rgViewOptionToggleGroup.size(); j++)
                {
                    final ToggleButton view = rgViewOptionToggleGroup.get(j);
                    boolean checkState = view.getId() == i;

                    if(checkState && view.isChecked())
                        continue;
                    else if(checkState)
                    {
                        TextView txt = findViewById(R.id.txtSelectedOption);
                        txt.setText(view.getText());

                        view.setText(UI.getUnderlinedText(view.getText()));
                    }
                    else
                    {
                        view.setText(view.getText());
                    }

                    view.setChecked(checkState);
                }
            }
        });
        recyclerView.setHasFixedSize(true);

        CharacterNavigator.setProgressBar(findViewById(R.id.mainProgressBar));
        currentViewingCharacter = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalState.initialize(this);

        final SwipeRefreshLayout refreshLayout = findViewById(R.id.pullToRefresh);
        refreshLayout.setOnRefreshListener(() ->
        {
            if(!CharacterNavigator.isLoading())
                CharacterNavigator.getCharactersFromOffset((ArrayList<Character> param) ->
                {
                    characterListView.setCharacters(CharacterNavigator.getLoadedCharacters());
                    refreshLayout.setRefreshing(false);
                    return null;
                }, (voidParam)->
                {
                    refreshLayout.setRefreshing(false);
                    return null;
                });
        });


        layoutInitialization();
        CharacterNavigator.setOrderBy("name");

        //Load favorites from storage
        if(Favorites.favoriteExists())
        {
            characterListView = new CharacterListView(Favorites.loadFavorites(), MainActivity.this);
            recyclerView.setAdapter(characterListView);
            loadCharacters();
        }
        else
        {
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
    }

    protected void updateRecyclerView()
    {
        if(recyclerView != null && characterListView != null)
            for(int i = 0, len = recyclerView.getChildCount(); i < len; i++)
            {
                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));

                CharacterListView.CharacterView v = (CharacterListView.CharacterView)holder;
                FavoriteButton.updateButtonBackground(v.favoriteButton, characterListView.getCharacterAt(v.getAdapterPosition()));
            }
    }


    protected void loadCharacters()
    {
        CharacterNavigator.getCharactersFromOffset(new Callback<Void, ArrayList<Character>>() {
            @Override
            public Void execute(ArrayList<Character> param)
            {
                //Always includes favorites
/*                ArrayList<Character> chars = Favorites.getFavorites();
                if(selectedOption != Options.FAVORITES)
                {

                    chars.addAll(CharacterNavigator.getUnfavoritedCharacters
                            (CharacterNavigator.getLoadedCharacters()));
                }*/
                characterListView.setCharacters(CharacterNavigator.getLoadedCharacters());
                return null;
            }
        });
    }
}
