package com.example.memorypract.data.modelos;

public class ColorItem {
    private String nombre;
    private int ColorHex;

    public ColorItem(String nombre, int ColorHex){
        this.nombre = nombre;
        this.ColorHex = ColorHex;
    }

    public int getColorHex() {
        return ColorHex;
    }

    public String getNombre() {
        return nombre;
    }
}
