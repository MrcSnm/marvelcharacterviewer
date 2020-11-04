package com.hipreme.mobbuy.utils;

import java.util.ArrayList;

public interface Callback<T, P> {

    T execute(P param);

    default Callback<T, P> concat(final Callback<T, P> cb2)
    {
        return (param) ->
        {
            Callback.this.execute(param);
            return cb2.execute(param);
        };
    }
    default Callback<ArrayList<T>, P> concatList(final Callback<T, P> cb2)
    {
        return (param) ->
        {
            ArrayList<T> rets = new ArrayList<>();
            rets.add(Callback.this.execute(param));
            rets.add(cb2.execute(param));
            return rets;
        };
    }
}
