package com.example.memorypract.data.modelos;

public class ObjetoItem {
        private String nombre;
        private int imagenResId;


        public ObjetoItem(String nombre, int imagenResId) {
            this.nombre = nombre;
            this.imagenResId = imagenResId;
        }

        public String getNombre() {
            return nombre;
        }

        public int getImagenResId() {
            return imagenResId;
        }

}
