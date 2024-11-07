package edu.pmdm.hipotenochas.spinner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

import edu.pmdm.hipotenochas.R;

public class Adaptador extends ArrayAdapter<Personaje> {

    public Adaptador(@NonNull Context context, @NonNull List<Personaje> objects) {
        super(context, android.R.layout.simple_spinner_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return crearVista(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return crearVista(position, convertView, parent);
    }

    private View crearVista(int posicion, View convertView, ViewGroup parent){
        View elemento = convertView;

        if (elemento == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            elemento = inflater.inflate(R.layout.spinner_fila, parent, false);
        }

        // Obtenemos referencia al elemento inflado de la imagen
        ImageView imagen = elemento.findViewById(R.id.personaje);

        // Obtenemos los datos a cargar en el imageview
        Personaje personaje = getItem(posicion);

        //Cargamos la imagen
        if (personaje != null){
            Drawable drawable = getContext().getDrawable(personaje.getImagen());
            imagen.setImageDrawable(drawable);
        }

        return elemento;

    }
}
