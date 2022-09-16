package com.lai.core;

public class StickerCoreJni {

    static {
        System.loadLibrary("native-render");
    }

    public StickerCoreJni() {
    }

    private native long create();
}
