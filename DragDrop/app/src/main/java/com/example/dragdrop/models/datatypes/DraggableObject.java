package com.example.dragdrop.models.datatypes;

import android.graphics.drawable.Drawable;

public class DraggableObject {
    public int getID() {
        return ID;
    }

    private int ID;
    public String Name;
    public Drawable Drawable;

    public DraggableObject(int id, String name, Drawable drawable){
        this.ID = id;
        this.Name = name;
        this.Drawable = drawable;
    }
}
