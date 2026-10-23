package com.example.memorypract.ui.controladores;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.memorypract.R;
import com.example.memorypract.data.modelos.AnimalItem;
import com.example.memorypract.data.modelos.ObjetoItem;
import com.example.memorypract.ui.viewmodels.ObjetosViewModel;

import java.util.ArrayList;
import java.util.List;

public class PantallaobjetosActivity extends AppCompatActivity {

    private ObjetosViewModel viewModel;
    private CardView cardFlashcard;
    private LinearLayout layoutFront;
    private LinearLayout layoutBack;
    private ImageView imgObjeto;
    private TextView txtObjetoName;
    private TextView txtProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pantallaobjetos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(ObjetosViewModel.class);

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(PantallaobjetosActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.btnAnimales).setOnClickListener(v -> {
            Intent intent = new Intent(PantallaobjetosActivity.this, AnimalesActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnColores).setOnClickListener(v -> {
            Intent intent = new Intent(PantallaobjetosActivity.this, ColoresActivity.class);
            startActivity(intent);
        });

        cardFlashcard = findViewById(R.id.cardFlashcard);
        float scale = getResources().getDisplayMetrics().density;
        cardFlashcard.setCameraDistance(8000 * scale);

        layoutFront = findViewById(R.id.layoutFront);
        layoutBack = findViewById(R.id.layoutBack);
        imgObjeto = findViewById(R.id.imgObjeto);
        txtObjetoName = findViewById(R.id.txtObjetoName);
        txtProgress = findViewById(R.id.txtProgress);
        Button btnCorrecto = findViewById(R.id.btnCorrecto);
        Button btnIncorrecto = findViewById(R.id.btnIncorrecto);


        List<ObjetoItem> lista = new ArrayList<>();
        lista.add(new ObjetoItem(getString(R.string.objeto_alumnos_jpg), R.drawable.foto_alumnos_jpg));
        lista.add(new ObjetoItem(getString(R.string.objeto_botas), R.drawable.foto_botas));
        lista.add(new ObjetoItem(getString(R.string.objeto_cartuchera), R.drawable.foto_cartuchera_jpg));
        lista.add(new ObjetoItem(getString(R.string.objeto_hoja), R.drawable.foto_hoja_jpg));
        lista.add(new ObjetoItem(getString(R.string.objeto_nido), R.drawable.foto_nido_jpg));
        lista.add(new ObjetoItem(getString(R.string.objeto_nube), R.drawable.foto_nube));

        viewModel.inicializarObjetos(lista);

        viewModel.getCurrentIndex().observe(this, index -> actualizarVistaCard());
        viewModel.isFlipped().observe(this, flipped -> actualizarEstructuraCard(Boolean.TRUE.equals(flipped)));

        viewModel.isGameFinished().observe(this, finished -> {
            if (Boolean.TRUE.equals(finished)) {
                mostrarResumenFinal();
            }
        });

        cardFlashcard.setOnClickListener(v -> {
            if (Boolean.TRUE.equals(viewModel.isAnimating().getValue()) || Boolean.TRUE.equals(viewModel.isFlipped().getValue())) return;
            voltearTarjeta();
        });

        btnCorrecto.setOnClickListener(v -> {
            if (Boolean.TRUE.equals(viewModel.isAnimating().getValue())) return;
            viewModel.registrarRespuesta(true);
            Toast.makeText(this, R.string.message_correct, Toast.LENGTH_SHORT).show();
            avanzarSiguienteTarjeta();
        });

        btnIncorrecto.setOnClickListener(v -> {
            if (Boolean.TRUE.equals(viewModel.isAnimating().getValue())) return;
            viewModel.registrarRespuesta(false);
            Toast.makeText(this, R.string.message_incorrect, Toast.LENGTH_SHORT).show();
            avanzarSiguienteTarjeta();
        });
    }

    private void voltearTarjeta() {
        viewModel.setAnimating(true);
        cardFlashcard.animate()
                .rotationY(90f)
                .setDuration(150)
                .withEndAction(() -> {
                    viewModel.setFlipped(true);

                    cardFlashcard.setRotationY(270f);
                    cardFlashcard.animate()
                            .rotationY(360f)
                            .setDuration(150)
                            .withEndAction(() -> {
                                cardFlashcard.setRotationY(0f);
                                viewModel.setAnimating(false);
                            })
                            .start();
                })
                .start();
    }

    private void avanzarSiguienteTarjeta() {
        viewModel.setAnimating(true);
        cardFlashcard.animate()
                .translationX(-400f)
                .rotation(-15f)
                .alpha(0f)
                .setDuration(220)
                .withEndAction(() -> {
                    viewModel.avanzarTarjeta();

                    if (Boolean.TRUE.equals(viewModel.isGameFinished().getValue())) {
                        viewModel.setAnimating(false);
                        return;
                    }

                    cardFlashcard.setTranslationX(400f);
                    cardFlashcard.setRotation(15f);
                    cardFlashcard.setAlpha(0f);
                    cardFlashcard.setScaleX(0.85f);
                    cardFlashcard.setScaleY(0.85f);

                    cardFlashcard.animate()
                            .translationX(0f)
                            .rotation(0f)
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(280)
                            .withEndAction(() -> viewModel.setAnimating(false))
                            .start();
                }).start();
    }

    private void mostrarResumenFinal() {
        int correctas = viewModel.getCorrectCount().getValue() != null ? viewModel.getCorrectCount().getValue() : 0;
        int incorrectas = viewModel.getIncorrectCount().getValue() != null ? viewModel.getIncorrectCount().getValue() : 0;
        List<ObjetoItem> lista = viewModel.getListaObjetos().getValue();
        int total = lista != null ? lista.size() : 0;

        String mensaje = "¡Juego completado!\n\n" +
                "✅ Correctas: " + correctas + "\n" +
                "❌ Incorrectas: " + incorrectas + "\n" +
                "📊 Total de tarjetas: " + total;

        new AlertDialog.Builder(this)
                .setTitle("Resumen de Resultados")
                .setMessage(mensaje)
                .setPositiveButton("Volver a intentarlo", (dialog, which) -> {
                    viewModel.reiniciarJuego();
                    actualizarVistaCard();
                })
                .setNegativeButton("Volver al inicio", (dialog, which) -> {
                    Intent intent = new Intent(PantallaobjetosActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                })
                .setCancelable(false)
                .show();
    }

    private void actualizarVistaCard() {
        ObjetoItem objeto = viewModel.getObjetoActual();
        if (objeto == null) return;

        List<ObjetoItem> lista = viewModel.getListaObjetos().getValue();
        Integer index = viewModel.getCurrentIndex().getValue();
        int currentIndexVal = index != null ? index : 0;
        int total = lista != null ? lista.size() : 0;

        imgObjeto.setImageResource(objeto.getImagenResId());
        txtObjetoName.setText(objeto.getNombre());
        txtProgress.setText("Objeto " + (currentIndexVal + 1) + " de " + total);

        if (cardFlashcard != null) {
            cardFlashcard.setRotationY(0f);
            cardFlashcard.setTranslationX(0f);
            cardFlashcard.setRotation(0f);
            cardFlashcard.setAlpha(1f);
            cardFlashcard.setScaleX(1f);
            cardFlashcard.setScaleY(1f);
        }
        actualizarEstructuraCard(Boolean.TRUE.equals(viewModel.isFlipped().getValue()));
    }

    private void actualizarEstructuraCard(boolean isFlipped) {
        if (isFlipped) {
            layoutFront.setVisibility(View.GONE);
            layoutBack.setVisibility(View.VISIBLE);
            cardFlashcard.setCardBackgroundColor(0xFFFFF0F5);
        } else {
            layoutFront.setVisibility(View.VISIBLE);
            layoutBack.setVisibility(View.GONE);
            cardFlashcard.setCardBackgroundColor(0xFFFFF5F7);
        }
    }
}
