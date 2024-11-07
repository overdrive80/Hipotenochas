package edu.pmdm.hipotenochas;

import android.content.Context;
import android.view.View;

public class Casilla extends View implements View.OnClickListener, View.OnLongClickListener {
    private int x;
    private int y;
    private Context context;
    private EstadoJuego listener;
    private boolean estaDestapada = false;

    public Casilla(Context context, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
        this.context = context;

        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    /*** METODOS DE ACCION - LISTENERS **/
    @Override
    public void onClick(View view) {
        listener.onClick(x, y);
    }

    @Override
    public boolean onLongClick(View view) {
        listener.onLongClick(x, y);
        return true;
    }

    public void setListener(EstadoJuego listener) {
        this.listener = listener;
    }

    public boolean estaDestapada() {
        return this.estaDestapada;
    }

    public void setEstaDestapada(boolean destapada) {
        this.estaDestapada = destapada;
    }
}
