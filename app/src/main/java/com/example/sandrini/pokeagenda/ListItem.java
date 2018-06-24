package com.example.sandrini.pokeagenda;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListItem extends ArrayAdapter {

    private final Activity context;
    private final String[] pokemonName;
    private final String[] pokemonSpecies;
    private final Integer[] pokemonImage;


    public ListItem(Activity context, String[] name, String[] species, Integer[] image) {
        super(context, R.layout.list_item, name);
        this.context = context;
        this.pokemonName = name;
        this.pokemonSpecies = species;
        this.pokemonImage = image;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null,true);
        TextView nameItem = (TextView) rowView.findViewById(R.id.pokemon_name);
        TextView speciesItem = (TextView) rowView.findViewById(R.id.pokemon_species);
        ImageView imageItem = (ImageView) rowView.findViewById(R.id.pokemon_image);
        nameItem.setText(pokemonName[position]);
        speciesItem.setText(pokemonSpecies[position]);
        imageItem.setImageResource(pokemonImage[position]);
        return rowView;
    }
}
