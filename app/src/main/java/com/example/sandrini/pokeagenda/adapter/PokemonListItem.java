package com.example.sandrini.pokeagenda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sandrini.pokeagenda.R;
import com.example.sandrini.pokeagenda.model.Pokemon;

import java.util.List;

public class PokemonListItem extends BaseAdapter {

    private List<Pokemon> pokemons;
    private Context context;

    public PokemonListItem(Context context, List<Pokemon> pokemons){
        this.context = context;
        this.pokemons = pokemons;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pokemon pokemon = pokemons.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;

        if(view == null) {
            view = inflater.inflate(R.layout.list_item,null);
        }

        TextView pokemonName = (TextView) view.findViewById(R.id.pokemon_name);
        TextView pokemonSpecies = (TextView) view.findViewById(R.id.pokemon_species);
        ImageView pokemonImage = (ImageView) view.findViewById(R.id.pokemon_image);

        pokemonName.setText(pokemon.getName());
        pokemonSpecies.setText(pokemon.getSpecies());
        if(pokemon.getImage() != null){
            Bitmap bitmap = BitmapFactory.decodeFile(pokemon.getImage());
            Bitmap minimizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            pokemonImage.setImageBitmap(minimizedBitmap);
            pokemonImage.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        return view;
    }
}
