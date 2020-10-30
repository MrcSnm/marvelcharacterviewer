package com.hipreme.mobbuy.marvel.layouts;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration
{
    protected int offset;
    public SpaceItemDecoration(int offsetSize){offset = offsetSize;}

    @Override
    public void getItemOffsets(Rect outRect, View view,RecyclerView parent, RecyclerView.State state)
    {
        outRect.left = offset;
        outRect.right = offset;
        outRect.bottom = offset;
        if(parent.getChildLayoutPosition(view) == 0)
            outRect.top = offset;
    }
}
