package com.hipreme.mobbuy.global;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import androidx.core.util.Pools;

public class UI
{
    protected static Pools.SimplePool<ProgressBar> progressBars;
    public static void start()
    {
        progressBars = new Pools.SimplePool<>(8);

    }

    public static ProgressBar getProgressBar(Context ctx, boolean show)
    {
        ProgressBar pb = progressBars.acquire();
        if(pb == null)
        {
            pb = new ProgressBar(ctx);
            pb.setIndeterminate(true);
        }
        else
        {
            pb.setProgress(0);

        }
        pb.setVisibility((show ? View.VISIBLE : View.INVISIBLE));
        return pb;
    }

    public static void release(ProgressBar pb){progressBars.release(pb); pb.setVisibility(View.INVISIBLE);}

}
