package com.example.memorypract.ui.controladores;

public class AnimalItem {
    private String nombre;
    private int imagenResId;
    private boolean isFlipped;

    public AnimalItem(String nombre, int imagenResId) {
        this.nombre = nombre;
        this.imagenResId = imagenResId;
        this.isFlipped = false;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }
}
