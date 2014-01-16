package com.readboy.learnword.util;

public class AiWriteLib {

    private final static String LIBNAME = "TestAiWrite";

    static {
        System.loadLibrary(LIBNAME);
    }

    public native void mainLoad();

    public native void readFromAssets(Object assetManager, String fileName);

    public native byte[] getWriteString(int[] points, int type);

    public native void clear();
}
