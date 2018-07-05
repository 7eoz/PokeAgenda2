package com.example.sandrini.pokeagenda.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sandrini.pokeagenda.PokeDetailActivity;
import com.example.sandrini.pokeagenda.R;
import com.example.sandrini.pokeagenda.helper.PokemonImageHelper;
import com.example.sandrini.pokeagenda.model.Pokemon;

import java.util.List;

public class PokemonListItem extends BaseAdapter {


    private List<Pokemon> pokemonList;
    private LayoutInflater layoutInflater;

    public PokemonListItem(Context context, List<Pokemon> pokemonList){

        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pokemonList = pokemonList;

    }

    @Override
    public int getCount(){
        return this.pokemonList.size();
    }

    @Override
    public Object getItem(int position){
        return this.pokemonList.get(position);
    }

    @Override
    public long getItemId(int arg){
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = this.layoutInflater.inflate(R.layout.list_item, null);
        TextView pokemonName = (TextView) view.findViewById(R.id.poke_name);
        TextView pokemonSpecies = (TextView) view.findViewById(R.id.poke_species);
        ImageView pokemonImage = (ImageView) view.findViewById(R.id.poke_image);

        pokemonName.setText(pokemonList.get(position).getName());
        pokemonSpecies.setText(pokemonList.get(position).getSpecies());


        /*pokemonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(v.getContext(), PokeDetailActivity.class);
                System.out.println(pokemonList.get(position).toString());
                it.putExtra("pokemon_name",pokemonList.get(position).getName());
                it.putExtra("pokemon_specie",pokemonList.get(position).getSpecies());
                it.putExtra("pokemon_weight",pokemonList.get(position).getWeight());
                it.putExtra("pokemon_height",pokemonList.get(position).getHeight());
                it.putExtra("pokemon_image",pokemonList.get(position).getImage());
                it.putExtra("pokemon_treinador",pokemonList.get(position).getTrainerId());
                v.getContext().startActivity(it);
            }
        });*/
        ((ImageView)(view.findViewById(R.id.poke_image))).setImageBitmap(PokemonImageHelper.getBitmapFromString(pokemonList.get(position).getImage()));
        /*if(pokemonList.get(position).getImage() != null){
            Bitmap bitmap = BitmapFactory.decodeFile(pokemonList.get(position).getImage());
            Bitmap minimizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            pokemonImage.setImageBitmap(minimizedBitmap);
            pokemonImage.setScaleType(ImageView.ScaleType.FIT_XY);
        }*/
        return view;
    }

}
