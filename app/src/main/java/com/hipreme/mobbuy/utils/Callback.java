package com.hipreme.mobbuy.utils;

public interface Callback<T, P> {

    T execute(P param);
}
