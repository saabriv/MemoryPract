package com.example.memorypract.ui.controladores;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.memorypract.R;
import com.example.memorypract.ui.controladores.ColorItem;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    public interface OnColorClickListener {
        void onColorClick(ColorItem colorItem, View borderView);
    }
    private List<ColorItem> listaColoresItems;
    private OnColorClickListener listener;

    public ColorAdapter(List<ColorItem> listaColoresItems, OnColorClickListener listener) {
        this.listaColoresItems = listaColoresItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        ColorItem colorActual = listaColoresItems.get(position);
        holder.cardColor.setCardBackgroundColor(colorActual.getColorHex());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onColorClick(colorActual, holder.borderColor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaColoresItems.size();
    }

    public static class ColorViewHolder extends RecyclerView.ViewHolder {
        CardView cardColor;
        View borderColor;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            cardColor = itemView.findViewById(R.id.card_color);
            borderColor = itemView.findViewById(R.id.border_color);
        }
    }
}
