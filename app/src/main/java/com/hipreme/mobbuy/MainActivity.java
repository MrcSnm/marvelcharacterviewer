package com.hipreme.mobbuy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.hipreme.mobbuy.marvel.widgets.FavoriteButton;
import com.hipreme.mobbuy.utils.Callback;
import com.hipreme.mobbuy.utils.Digest;
import com.hipreme.mobbuy.marvel.MarvelAPI;
import com.hipreme.mobbuy.utils.Resources;

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
        ArrayList<Character> favs = Storage.getFavorites();

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

        CharacterNavigator.setProgressBar((ProgressBar)findViewById(R.id.mainProgressBar));
        currentViewingCharacter = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalState.initialize(this);


        layoutInitialization();
        CharacterNavigator.setOrderBy("name");

        if(Storage.favoriteExists())
        {
            characterListView = new CharacterListView(Storage.getFavorites(), MainActivity.this);
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


    protected void loadCharacters()
    {
        CharacterNavigator.getCharactersFromOffset(new Callback<Void, ArrayList<Character>>() {
            @Override
            public Void execute(ArrayList<Character> param)
            {
                characterListView.setCharacters(CharacterNavigator.getLoadedCharacters());
                return null;
            }
        });
    }
}
