package com.mkr.ocr.imagetotext.converter.Clases;

import android.graphics.drawable.Drawable;

public class SettingItem {
    private Drawable img;
    private String name;
    private boolean switcher;
    private boolean hasSwitcher;
    public SettingItem(Drawable img, String name, boolean switcher, boolean hasSwitcher){
        this.img = img;
        this.name = name;
        this.switcher = switcher;
        this.hasSwitcher = hasSwitcher;
    }

    public Drawable getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public boolean isHasSwitcher() {
        return hasSwitcher;
    }
    public boolean isSwitcher() {
        return switcher;
    }
    public void setIsSwitcher(boolean switcher){
        this.switcher = switcher;
    }
    public void setName(String name){
        this.name = name;
    }

}
