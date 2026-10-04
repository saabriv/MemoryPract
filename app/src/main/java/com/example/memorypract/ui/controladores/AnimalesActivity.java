package com.example.memorypract.ui.controladores;

import android.content.Intent;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.memorypract.R;

import java.util.ArrayList;
import java.util.List;

public class AnimalesActivity extends AppCompatActivity {

    private List<AnimalItem> listaAnimales;
    private int currentIndex = 0;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private boolean isFlipped = false;
    private boolean isAnimating = false;

    private CardView cardFlashcard;
    private LinearLayout layoutFront;
    private LinearLayout layoutBack;
    private ImageView imgAnimal;
    private TextView txtAnimalName;
    private TextView txtProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animales);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(AnimalesActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        findViewById(R.id.btnColores).setOnClickListener(v -> {
            Intent intent = new Intent(AnimalesActivity.this, ColoresActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btnObjetos).setOnClickListener(v -> {
            Intent intent = new Intent(AnimalesActivity.this, PantallaobjetosActivity.class);
            startActivity(intent);
        });

        cardFlashcard = findViewById(R.id.cardFlashcard);
        
        // Ajustar la distancia de la cámara para que el giro 3D preserve la forma y tamaño sin deformarse ni agrandarse
        float scale = getResources().getDisplayMetrics().density;
        cardFlashcard.setCameraDistance(8000 * scale);

        layoutFront = findViewById(R.id.layoutFront);
        layoutBack = findViewById(R.id.layoutBack);
        imgAnimal = findViewById(R.id.imgAnimal);
        txtAnimalName = findViewById(R.id.txtAnimalName);
        txtProgress = findViewById(R.id.txtProgress);
        Button btnCorrecto = findViewById(R.id.btnCorrecto);
        Button btnIncorrecto = findViewById(R.id.btnIncorrecto);

        listaAnimales = new ArrayList<>();
        listaAnimales.add(new AnimalItem(getString(R.string.animal_perro), R.drawable.perro));
        listaAnimales.add(new AnimalItem(getString(R.string.animal_gato), R.drawable.gato));
        listaAnimales.add(new AnimalItem(getString(R.string.animal_conejo), R.drawable.conejo));
        listaAnimales.add(new AnimalItem(getString(R.string.animal_elefante), R.drawable.elefante));
        listaAnimales.add(new AnimalItem(getString(R.string.animal_leon), R.drawable.leon));
        listaAnimales.add(new AnimalItem(getString(R.string.animal_jirafa), R.drawable.jirafa));

        cargarAnimalActual();

        // Tocar la tarjeta en el frente para voltearla
        cardFlashcard.setOnClickListener(v -> {
            if (isAnimating || isFlipped) return;
            voltearTarjeta();
        });

        // Botón Correcto
        btnCorrecto.setOnClickListener(v -> {
            if (isAnimating) return;
            correctCount++;
            Toast.makeText(this, R.string.message_correct, Toast.LENGTH_SHORT).show();
            avanzarSiguienteTarjeta();
        });

        // Botón Incorrecto
        btnIncorrecto.setOnClickListener(v -> {
            if (isAnimating) return;
            incorrectCount++;
            Toast.makeText(this, R.string.message_incorrect, Toast.LENGTH_SHORT).show();
            avanzarSiguienteTarjeta();
        });
    }

    private void voltearTarjeta() {
        isAnimating = true;
        // Giro 3D natural preservando la forma exacta de la tarjeta
        cardFlashcard.animate()
                .rotationY(90f)
                .setDuration(150)
                .withEndAction(() -> {
                    isFlipped = true;
                    updateCardViews();

                    cardFlashcard.setRotationY(270f);
                    cardFlashcard.animate()
                            .rotationY(360f)
                            .setDuration(150)
                            .withEndAction(() -> {
                                cardFlashcard.setRotationY(0f);
                                isAnimating = false;
                            })
                            .start();
                })
                .start();
    }

    private void avanzarSiguienteTarjeta() {
        isAnimating = true;
        // Deslizar la tarjeta actual hacia la IZQUIERDA
        cardFlashcard.animate()
                .translationX(-400f)
                .rotation(-15f)
                .alpha(0f)
                .setDuration(220)
                .withEndAction(() -> {
                    currentIndex++;
                    if (currentIndex < listaAnimales.size()) {
                        isFlipped = false;
                        updateCardViews();
                    } else {
                        isAnimating = false;
                        mostrarResumenFinal();
                        return;
                    }

                    // Posicionar la nueva tarjeta a la DERECHA para que entre desde la derecha
                    cardFlashcard.setTranslationX(400f);
                    cardFlashcard.setRotation(15f);
                    cardFlashcard.setAlpha(0f);
                    cardFlashcard.setScaleX(0.85f);
                    cardFlashcard.setScaleY(0.85f);

                    // Deslizar la nueva carta al centro suavemente
                    cardFlashcard.animate()
                            .translationX(0f)
                            .rotation(0f)
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(280)
                            .withEndAction(() -> isAnimating = false)
                            .start();
                }).start();
    }

    private void mostrarResumenFinal() {
        String mensaje = "¡Juego completado!\n\n" +
                "✅ Correctas: " + correctCount + "\n" +
                "❌ Incorrectas: " + incorrectCount + "\n" +
                "📊 Total de tarjetas: " + listaAnimales.size();

        new AlertDialog.Builder(this)
                .setTitle("Resumen de Resultados")
                .setMessage(mensaje)
                .setPositiveButton("Volver a intentarlo", (dialog, which) -> {
                    correctCount = 0;
                    incorrectCount = 0;
                    currentIndex = 0;
                    isFlipped = false;
                    cargarAnimalActual();
                })
                .setNegativeButton("Volver al inicio", (dialog, which) -> {
                    Intent intent = new Intent(AnimalesActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                })
                .setCancelable(false)
                .show();
    }

    private void cargarAnimalActual() {
        isFlipped = false;
        isAnimating = false;
        if (cardFlashcard != null) {
            cardFlashcard.setRotationY(0f);
            cardFlashcard.setTranslationX(0f);
            cardFlashcard.setRotation(0f);
            cardFlashcard.setAlpha(1f);
            cardFlashcard.setScaleX(1f);
            cardFlashcard.setScaleY(1f);
        }
        updateCardViews();
    }

    private void updateCardViews() {
        AnimalItem animal = listaAnimales.get(currentIndex);
        imgAnimal.setImageResource(animal.getImagenResId());
        txtAnimalName.setText(animal.getNombre());
        txtProgress.setText("Animal " + (currentIndex + 1) + " de " + listaAnimales.size());

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
