package edu.pmdm.hipotenochas;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import edu.pmdm.hipotenochas.spinner.Personaje;

public class Juego implements EstadoJuego {
    private static Juego instancia;
    private int bombas = 10, filas = 8, cols = 8;
    private Tablero tablero;
    private Generador gen;
    private int[][] minas;
    private Casilla[][] casillas;
    private GridLayout tableroLayout;
    private Context contexto;
    private int celdasDescubiertas = 0, celdasSinBomba;
    private Personaje personaje;

    //Modelo Singleton
    private Juego() {
    }

    public static Juego getInstance() {
        if (instancia == null) {
            instancia = new Juego();
        }

        return instancia;
    }

    public void construirTablero(Context contexto, Nivel nivel) {
        this.contexto = contexto;
        this.filas = nivel.getFilas();
        this.cols = nivel.getColumnas();
        this.bombas = nivel.getBombas();

        casillas = new Casilla[filas][cols];

        // Generamos el GridLayout y lo asignamos al Constraint
        generarTablero();
        generarMinas();
        crearCasillas();

        celdasSinBomba = filas * cols - bombas;
    }

    private void generarTablero() {
        MainActivity main = (MainActivity) contexto;
        ConstraintLayout constLayout = main.findViewById(R.id.constLayout);

        //Borramos las vistas del anterior layout y vaciamos memoria
        if (tableroLayout != null) {
            tableroLayout.removeAllViews();
            tableroLayout = null;
        }

        tablero = new Tablero(contexto, filas, cols);
        tableroLayout = tablero.getTableroLayout();

        constLayout.addView(tableroLayout);
    }

    private void generarMinas() {

        gen = new Generador(bombas, filas, cols);
        gen.generarDatosTablero();
        minas = gen.getTableroMinas();

        mostrarMatriz(minas);
    }

    private void mostrarMatriz(int[][] minas) {

        for (int i = 0; i < filas; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%3s ", minas[i][j]));
            }

            Log.d("minas", sb.toString());
        }
    }

    private void crearCasillas() {
        // Crear botones para cada celda
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < cols; j++) {
                Casilla casilla = new Casilla(contexto, i, j);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = tablero.getDimenCelda();
                params.height = params.width;

                casilla.setLayoutParams(params);
                Drawable drawable = ContextCompat.getDrawable(contexto, R.drawable.boton);
                casilla.setBackground(drawable);
                casilla.setListener(this);
                tableroLayout.addView(casilla);
                casillas[i][j] = casilla;
            }
        }
    }

    public void haPerdido(String mensaje) {
        Toast.makeText(contexto, mensaje, Toast.LENGTH_LONG).show();
        bloquearTablero();
    }

    public void bloquearTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < cols; j++) {
                casillas[i][j].setEnabled(false);
            }
        }
    }

    public void haGanado(String mensaje) {
        Toast.makeText(contexto, mensaje, Toast.LENGTH_LONG).show();
        bloquearTablero();
    }

    @Override
    public void onClick(int x, int y) {
        if (x < 0 || y < 0 || x >= filas || y >= cols) {
            // Salir si las coordenadas están fuera del rango
            return;
        }

        Casilla casilla = getCasilla(x, y);
        int valorCelda = getValor(x, y);

        if (!casilla.estaDestapada()) {
            // Configuramos la celda como destapada
            casilla.setEstaDestapada(true);
            celdasDescubiertas++;

            //Mostramos el valor de la celda
            Drawable drawable = mostrarCelda(valorCelda);
            casilla.setBackground(drawable);

            // Si el valor de la celda es 0, descubrir celdas adyacentes
            if (getValor(x, y) == 0) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {

                        // Calcular coordenadas de la celda adyacente
                        int x2 = x + i;
                        int y2 = y + j;

                        //Omitir recursividad sobre la celda actual
                        if (!(i == 0 && j == 0)) {
                            onClick(x2, y2);
                        }
                    }
                }
            }

            // Si el valor es -1, significa que se hizo clic en una mina
            if (getValor(x, y) == -1) {
                drawable = mostrarCelda(-2);
                casilla.setBackground(drawable);
                haPerdido("Has perdido, era una hipotenocha.");
            }
        }

        //Verificar si hay victoria
        if (todasDescubiertas()) {
            haGanado("Has encontrado todas las hipotenochas. ¡Enhorabuena!");
        }
    }

    private boolean todasDescubiertas() {
        return celdasDescubiertas == celdasSinBomba;
    }

    @Override
    public void onLongClick(int x, int y) {
        Casilla casilla = getCasilla(x, y);
        int valorCelda = getValor(x, y);

        if (valorCelda == -1) {
            //SIGUE JUEGO
            Drawable drawable = mostrarCelda(valorCelda);
            casilla.setBackground(drawable);

            bombas--;
            if (bombas <= 0) {
                haGanado("Has encontrado todas las hipotenochas. ¡Enhorabuena!");
                return;
            }

            Toast.makeText(contexto, "Has encontrado una hipotenocha", Toast.LENGTH_LONG).show();

        } else {
            //DERROTA 2
            Drawable drawable = mostrarCelda(valorCelda);
            casilla.setBackground(drawable);
            haPerdido("Has perdido. No era una hipotenocha");
        }
    }

    public Casilla getCasilla(int x, int y) {

        return casillas[x][y];
    }

    public Drawable mostrarCelda(int valorCelda) {
        Drawable imagenBoton = null;

        switch (valorCelda) {
            case -1: // Es hipotenocha
                imagenBoton = getImagenFondo(personaje.getImagen());
                break;
            case -2: // No es hipotenocha
                imagenBoton = ContextCompat.getDrawable(contexto, R.drawable.no_hipotenocha);
                break;
            case 1:  // Hay 1 bomba alrededor
                imagenBoton = ContextCompat.getDrawable(contexto, R.drawable.number_1);
                break;
            case 2:  // Hay 2 bombas alrededor
                imagenBoton = ContextCompat.getDrawable(contexto, R.drawable.number_2);
                break;
            case 3:  // Hay 3 bombas alrededor
                imagenBoton = ContextCompat.getDrawable(contexto, R.drawable.number_3);
                break;
            case 4:  // Hay 4 bombas alrededor
                imagenBoton = ContextCompat.getDrawable(contexto, R.drawable.number_4);
                break;
            case 5:  // Hay 4 bombas alrededor
                imagenBoton = ContextCompat.getDrawable(contexto, R.drawable.number_5);
                break;
            case 6:  // Hay 4 bombas alrededor
                imagenBoton = ContextCompat.getDrawable(contexto, R.drawable.number_6);
                break;
            case 7:  // Hay 4 bombas alrededor
                imagenBoton = ContextCompat.getDrawable(contexto, R.drawable.number_7);
                break;
            case 8:  // Hay 4 bombas alrededor
                imagenBoton = ContextCompat.getDrawable(contexto, R.drawable.number_8);
                break;
            default:
                imagenBoton = ContextCompat.getDrawable(contexto, R.drawable.number_0);
                break;
        }

        return imagenBoton;
    }

    private Drawable getImagenFondo(int imagenId) {
        if (imagenId == R.drawable.ic_hipo_enfermera_base) {
            return contexto.getDrawable(R.drawable.ic_hipo_enfermera);
        }
        if (imagenId == R.drawable.ic_hipo_flamenca_base) {
            return contexto.getDrawable(R.drawable.ic_hipo_flamenca);
        }
        if (imagenId == R.drawable.ic_hipo_matrix_base) {
            return contexto.getDrawable(R.drawable.ic_hipo_matrix);
        }
        if (imagenId == R.drawable.ic_hipo_militar_base) {
            return contexto.getDrawable(R.drawable.ic_hipo_militar);
        }
        if (imagenId == R.drawable.ic_hipo_talaverana_base) {
            return contexto.getDrawable(R.drawable.ic_hipo_talaverana);
        }
        return contexto.getDrawable(R.drawable.ic_hipo_spain);
    }

    public void setPersonaje(Personaje personaje) {
        this.personaje = personaje;
    }

    public int getValor(int x, int y) {
        return minas[x][y];
    }
}

