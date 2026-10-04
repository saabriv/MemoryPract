package com.example.memorypract.ui.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorypract.R;
import com.example.memorypract.data.modelos.AnimalItem;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {

    private List<AnimalItem> listaAnimales;

    public AnimalAdapter(List<AnimalItem> listaAnimales) {
        this.listaAnimales = listaAnimales;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animal, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        AnimalItem animal = listaAnimales.get(position);

        holder.imgAnimal.setImageResource(animal.getImagenResId());
        holder.txtAnimalName.setText(animal.getNombre());

        updateCardState(holder, animal.isFlipped());

        holder.cardAnimal.setOnClickListener(v -> {
            // Animación de giro (flip 3D)
            holder.cardAnimal.animate().rotationY(90f).setDuration(150).withEndAction(() -> {
                animal.setFlipped(!animal.isFlipped());
                updateCardState(holder, animal.isFlipped());
                holder.cardAnimal.setRotationY(270f);
                holder.cardAnimal.animate().rotationY(0f).setDuration(150).start();
            }).start();
        });
    }

    private void updateCardState(AnimalViewHolder holder, boolean isFlipped) {
        if (isFlipped) {
            holder.layoutFront.setVisibility(View.GONE);
            holder.layoutBack.setVisibility(View.VISIBLE);
            holder.cardAnimal.setCardBackgroundColor(0xFFFFF0F5);
        } else {
            holder.layoutFront.setVisibility(View.VISIBLE);
            holder.layoutBack.setVisibility(View.GONE);
            holder.cardAnimal.setCardBackgroundColor(0xFFFFF5F7);
        }
    }

    @Override
    public int getItemCount() {
        return listaAnimales.size();
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        CardView cardAnimal;
        LinearLayout layoutFront;
        LinearLayout layoutBack;
        ImageView imgAnimal;
        TextView txtAnimalName;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            cardAnimal = itemView.findViewById(R.id.cardAnimal);
            layoutFront = itemView.findViewById(R.id.layoutFront);
            layoutBack = itemView.findViewById(R.id.layoutBack);
            imgAnimal = itemView.findViewById(R.id.imgAnimal);
            txtAnimalName = itemView.findViewById(R.id.txtAnimalName);
        }
    }
}
