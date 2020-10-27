package com.hipreme.mobbuy.utils;


import java.io.BufferedReader;
import java.io.IOException;

public class Stream
{
    public static String read(BufferedReader r)
    {
        StringBuilder b = new StringBuilder();
        String line = null;
        try
        {
            while((line = r.readLine()) != null)
            {
                b.append(line);
                b.append("\n");
            }
        }
        catch(IOException e){Error.print(e, Stream.class); return null;}
        return b.toString();
    }
}
