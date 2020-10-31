package com.hipreme.mobbuy.global;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ProgressBar;
import androidx.core.util.Pools;

public class UI
{
    //protected static Pools.SimplePool<SpannableString> stringPool;
    public static void start()
    {
        //stringPool= new Pools.SimplePool<>(128);


    }

    protected static SpannableString getSpannableString(String txt)
    {
//        SpannableString ret = stringPool.acquire();
        SpannableString ret = new SpannableString(txt);
        return ret;

    }
    public static SpannableString getUnderlinedText(CharSequence txt){return getUnderlinedText(txt.toString());}
    public static SpannableString getUnderlinedText(String txt)
    {
        SpannableString ret = getSpannableString(txt);
        ret.setSpan(new UnderlineSpan(), 0, txt.length(), 0);
        return ret;
    }

    public static void release(SpannableString ss)
    {
        //stringPool.release(ss);
    }

}
