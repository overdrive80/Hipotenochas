package edu.pmdm.hipotenochas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import edu.pmdm.hipotenochas.spinner.Adaptador;
import edu.pmdm.hipotenochas.spinner.Personaje;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout constLayout;
    private Juego juego;
    private Nivel nivel;
    private int nivelDefecto = Nivel.PRINCIPIANTE;
    private Personaje personaje ;
    private List<Personaje> listaPersonajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Crear el ConstraintLayout
        constLayout = findViewById(R.id.constLayout);

        constLayout.post(new Runnable() {
            @Override
            public void run() {
                // Crear el nivel
                nivel = new Nivel(8, 8, 10);
                personaje = new Personaje(R.drawable.ic_hipo_spain);

                juego = Juego.getInstance();
                comenzarPartida();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.instrucciones) {
            mostrarInstrucciones();
            return true;
        } else if (id == R.id.nuevoJuego) {
            comenzarPartida();
            return true;
        } else if (id == R.id.config) {
            // Configurar ajustes
            setAjustes();
            return true;
        } else if (id == R.id.personaje) {
            setPersonaje();
            return true;
        }

        return false;
    }

    private void setPersonaje() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.seleccionar_hipotenocha);

        //Creamos el spinner
        Spinner spinner = new Spinner(this);

        listaPersonajes = getPersonajes();

        Adaptador adaptador = new Adaptador(this, listaPersonajes);
        spinner.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        spinner.setAdapter(adaptador);

        builder.setView(spinner);

        // Crear el diálogo
        AlertDialog dialog = builder.create();

        // Posicionar el diálogo en la parte superior de la pantalla
        dialog.getWindow().setGravity(Gravity.TOP);

        // El listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                personaje = listaPersonajes.get(i);
                juego.setPersonaje(personaje);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dialog.dismiss();
            }
        });

        // Mostrar el diálogo
        dialog.show();

    }

    private void setAjustes() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.seleccionar_nivel);

        String[] niveles = getResources().getStringArray(R.array.niveles);

        builder.setSingleChoiceItems(niveles, nivelDefecto,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nivel = crearNivel(i);
                    }
                });

        // Configurar el botón de confirmación
        builder.setPositiveButton(R.string.volver, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Mostrar la dificultad seleccionada
                //Toast.makeText(MainActivity.this, R.string.dificultad_seleccion + nivelSeleccionado, Toast.LENGTH_SHORT).show();

                dialog.dismiss();

                // Construir el tablero con el nivel seleccionado
                comenzarPartida();
            }
        });

        builder.show();
    }

    private Nivel crearNivel(int nivelSeleccionado) {

        Nivel nuevoNivel = null;

        switch (nivelSeleccionado) {
            case Nivel.AMATEUR:
                nuevoNivel = new Nivel(12, 12, 30);
                nivelDefecto = Nivel.AMATEUR;
                break;
            case Nivel.AVANZADO:
                nuevoNivel = new Nivel(16, 16, 60);
                nivelDefecto = Nivel.AVANZADO;
                break;
            default: // Nivel facil
                nuevoNivel = new Nivel(8, 8, 10);
                nivelDefecto = Nivel.PRINCIPIANTE;
        }

        return nuevoNivel;
    }

    private void mostrarInstrucciones() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.instrucciones)
                .setMessage(R.string.instrucciones_texto)
                .setPositiveButton(R.string.aceptar, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void comenzarPartida() {

        juego.setPersonaje(personaje);
        juego.construirTablero(MainActivity.this, nivel);
    }

    private List<Personaje> getPersonajes() {
        int[] personajes = new int[]{
                R.drawable.ic_hipo_enfermera_base,
                R.drawable.ic_hipo_flamenca_base,
                R.drawable.ic_hipo_spain_base,
                R.drawable.ic_hipo_matrix_base,
                R.drawable.ic_hipo_militar_base,
                R.drawable.ic_hipo_talaverana_base};

        // Convertir el array int[] a una lista de Integer
        listaPersonajes = new ArrayList<>();
        for (int personaje : personajes) {
            listaPersonajes.add(new Personaje(personaje));  // Agrega cada valor del array a la lista
        }

        return listaPersonajes;
    }
}




