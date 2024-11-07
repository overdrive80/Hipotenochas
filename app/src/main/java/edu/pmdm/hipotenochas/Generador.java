package edu.pmdm.hipotenochas;

import java.util.Random;

public class Generador {
    private int numMinas;
    private int filas;
    private int cols;
    private int[][] tableroMinas;

    public Generador(int numMinas, int filas, int cols){
        this.numMinas = numMinas;
        this.filas = filas;
        this.cols = cols;
    }

    public void generarDatosTablero(){
        tableroMinas = soltarMinas();

        tableroMinas = situarVecinos();

    }

    private int[][] situarVecinos() {
        int[][] nuevoTablero = tableroMinas.clone();

        for (int i = 0; i < filas; i++){
            for (int j = 0; j < cols; j++){

                // Si no es una bomba
                if (!esMina(i,j)){

                    int numero = calcularPerimetro(i, j);
                    nuevoTablero[i][j] = numero;

                }
            }
        }

        return nuevoTablero;
    }

    private int calcularPerimetro(int x, int y) {
        int numero = 0;

        // Controlamos el perimetro de la celda por coordenadas
        if (esMina(x-1, y-1)) { numero++;} //Arriba-Izq.
        if (esMina(x+0, y-1)) { numero++;} //Arriba.
        if (esMina(x+1, y-1)) { numero++;} //Arriba-Der.
        if (esMina(x-1, y+0)) { numero++;} //Izq.
        if (esMina(x+1, y+0)) { numero++;} //Der.
        if (esMina(x-1, y+1)) { numero++;} //Abajo-Izq.
        if (esMina(x+0, y+1)) { numero++;} //Abajo.
        if (esMina(x+1, y+1)) { numero++;} //Abajo-Der.

        return numero;
    }

    /**
     * Determina si es una mina o no en base a las coordenadas
     * en busca de la matriz
     * @param x
     * @param y
     * @return
     */
    private boolean esMina(int x, int y){

        if( x >= 0 && y >= 0 && x < cols && y < filas ) {
            if (tableroMinas[x][y] == -1) {
                return true;
            }
        }
        return false;
    }

    public int[][] getTableroMinas(){
        return tableroMinas;
    }

    private int[][] soltarMinas(){

        Random random = new Random();
        // Por defecto, la matriz está compuesta de ceros
        int[][] tablero = new int[filas][cols];

        //Colocamos las bombas
        while(numMinas > 0) {
            int x = random.nextInt(filas);
            int y = random.nextInt(cols);

            if (tablero[x][y] == 0){
                tablero[x][y] = -1;
                numMinas--;
            }
        }

        return tablero;
    }
}
