package edu.pmdm.hipotenochas;

public class Nivel {
    public static final int PRINCIPIANTE = 0;
    public static final int AMATEUR = 1;
    public static final int AVANZADO = 2;

    private int filas;
    private int columnas;
    private int bombas;

    public Nivel(int filas, int columnas, int bombas) {
        this.filas = filas;
        this.columnas = columnas;
        this.bombas = bombas;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public int getBombas() {
        return bombas;
    }

    public void setBombas(int bombas) {
        this.bombas = bombas;
    }
}
