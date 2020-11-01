package com.hipreme.mobbuy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hipreme.mobbuy.global.GlobalState;
import com.hipreme.mobbuy.marvel.layouts.ComicSeriesListView;
import com.hipreme.mobbuy.marvel.layouts.SummaryView;

public class ItemViewActivity extends AppCompatActivity
{
    TextView txtItemName;
    TextView txtItemDescription;
    ImageView imgItemView;

    SummaryView comics;
    SummaryView series;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_view_layout);

        imgItemView = findViewById(R.id.imgItemView);
        txtItemName = findViewById(R.id.txtItemName);
        txtItemDescription = findViewById(R.id.txtItemDescription);

        txtItemName.setText(GlobalState.currentViewingCharacter.name);
        txtItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Glide
            .with(this)
            .load(GlobalState.currentViewingCharacter.thumbnail.getImageUrl())
            .into(imgItemView);
        txtItemDescription.setText(GlobalState.currentViewingCharacter.description);

        ((Button)findViewById(R.id.btnItemFavorite)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        comics = new SummaryView(new ComicSeriesListView(GlobalState.currentViewingCharacter, this, true), (RecyclerView)findViewById(R.id.rvComics));
        series = new SummaryView(new ComicSeriesListView(GlobalState.currentViewingCharacter, this, false), (RecyclerView)findViewById(R.id.rvSeries));

    }
}
