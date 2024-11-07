package edu.pmdm.hipotenochas;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;

public class Tablero {
    private GridLayout tableroLayout;
    private static final int margenTablero = 64;
    private int filas;
    private int cols;

    private Context context;
    private int dimenCelda;

    public Tablero(Context context, int filas, int cols) {
        this.context = context;
        this.filas = filas;
        this.cols = cols;

        crearGridLayout();
    }

    // Método para devolver el GridLayout configurado
    public GridLayout getTableroLayout() {
        return tableroLayout;
    }

    private void crearGridLayout() {

        // Crear el GridLayout
        tableroLayout = new GridLayout(context);
        tableroLayout.setRowCount(filas);
        tableroLayout.setColumnCount(cols);

        // Configurar LayoutParams del GridLayout
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams.setMargins(margenTablero, margenTablero, margenTablero, margenTablero);
        tableroLayout.setLayoutParams(layoutParams);

        // Configurar márgenes para el GridLayout
        tableroLayout.setUseDefaultMargins(false);

        int ancho = (context.getResources().getDisplayMetrics().widthPixels - margenTablero * 2) / cols;
        dimenCelda = ancho;

    }

    public int getDimenCelda() {
        return dimenCelda;
    }
}

