package com.hipreme.mobbuy.utils;

public class Range
{
    public int start;
    public int end;
    public Range(int start, int end){this.start = start; this.end = end;}

    public final boolean isInRange(int n){return n>= start && n <= end;}
}
