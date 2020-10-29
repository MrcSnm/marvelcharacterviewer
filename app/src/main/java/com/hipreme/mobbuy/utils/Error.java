package com.hipreme.mobbuy.utils;

import android.util.Log;

public class Error
{
    public static final boolean IS_VERBOSE = true;
    public static final boolean TRACE_ERROR = true;
    public static void print(Exception e)
    {
        print();
        StringBuilder toPrint = new StringBuilder();
        if(IS_VERBOSE)
        {
            for(StackTraceElement element: e.getStackTrace())
            {
                toPrint.append(element.toString());
                toPrint.append("\n");
            }
            toPrint.append("__________________________________________________________\n");
            toPrint.append("Message : ");

        }
        toPrint.append(e.getMessage());
        toPrint.append("\n__________________________________________________________\n");

        Log.e("Error Utils",toPrint.toString());

    }
    public static void print(Exception e, Class cls)
    {
        Error.print(e);
        System.out.println("\n\n");
        Log.e(cls.getName() + " Error!", "");
        System.out.println("\n");
    }
    public static void print(Exception e, Class cls, String msg)
    {
        Error.print(e, cls);
        Log.e("Error Utils", msg);
    }
    public static void print(String msg)
    {
        print();Log.e("Error Utils", "Error Ocurred!\n\t" + msg);
    }
    public static void print()
    {
        if(!TRACE_ERROR) //This reduces to compile time evaluation for removing this expensive call
            return;

        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        StackTraceElement callPlace = stack[1];
        int counter = 1;
        String fName = callPlace.getFileName();

        while(fName.equals("Error.java") || fName.equals("Thread.java"))
        {
            counter++;
            callPlace = stack[counter];
            fName = callPlace.getFileName();
        }
        String toPrint = "Error found at " +
                callPlace.getMethodName() + "(" +
                callPlace.getFileName() + ":" +
                callPlace.getLineNumber() + ")";
        Log.e("Error Utils", toPrint);
    }
}
