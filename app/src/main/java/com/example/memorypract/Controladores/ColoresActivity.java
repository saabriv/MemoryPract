package com.example.memorypract.Controladores;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.memorypract.R;
//Confetti
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class ColoresActivity extends AppCompatActivity {
    private final String[] listaColores = {"AZUL", "ROSA", "VERDE", "VIOLETA"};
    private int indiceActual = 0;
    private TextView txtColorAdivinar;
    private TextView txtFeedback;

    private nl.dionsegijn.konfetti.xml.KonfettiView konfettiView;

    private void lanzarConfeti(){
        int colorAzul = 0xFF60A5FA;
        int colorVerde = 0xFF4ADE80;
        int colorRosa = 0xFFF472B6;
        int colorPurpura = 0xFFA78BFA;

        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        Party party = new PartyFactory(emitterConfig)
                .angle(270)
                .spread(90)
                .position(new Position.Relative(0.5, 0.4))
                .timeToLive(2000L)
                .build();

        konfettiView.start(party);

    }

    private void verificarColor(String colorSeleccionado, View borderView) {
        String colorCorrecto = listaColores[indiceActual];

        if (colorSeleccionado.equals(colorCorrecto)){
            // Mostrar borde verde y texto correcto en pantalla
            borderView.setBackgroundResource(R.drawable.border_correct);
            txtFeedback.setText("¡CORRECTO!");
            txtFeedback.setTextColor(0xFF4ADE80); // Verde hex
            txtFeedback.setVisibility(View.VISIBLE);
            lanzarConfeti();

            deshabilitarBotones();

            new Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                borderView.setBackgroundResource(R.drawable.border_transparent);
                txtFeedback.setVisibility(View.INVISIBLE);
                habilitarBotones();

                indiceActual++;
                if(indiceActual < listaColores.length){
                    txtColorAdivinar.setText(listaColores[indiceActual]);
                } else {
                    Toast.makeText(this, "FELICIDADES COMPLETASTE TODOS LOS COLORES", Toast.LENGTH_LONG).show();
                    indiceActual = 0;
                    txtColorAdivinar.setText(listaColores[indiceActual]);
                }
            }, 1500); // 1.5 segundos

        } else {
            // Mostrar borde rojo e incorrecto
            borderView.setBackgroundResource(R.drawable.border_incorrect);
            txtFeedback.setText("INCORRECTO PRUEBA OTRA VEZ");
            txtFeedback.setTextColor(0xFFF87171); // Rojo hex
            txtFeedback.setVisibility(View.VISIBLE);

            deshabilitarBotones();

            new Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                borderView.setBackgroundResource(R.drawable.border_transparent);
                txtFeedback.setVisibility(View.INVISIBLE);
                habilitarBotones();
            }, 1200); // 1.2 segundos
        }
    }

    private void deshabilitarBotones() {
        findViewById(R.id.card_blue).setClickable(false);
        findViewById(R.id.card_green).setClickable(false);
        findViewById(R.id.card_pink).setClickable(false);
        findViewById(R.id.card_purple).setClickable(false);
    }

    private void habilitarBotones() {
        findViewById(R.id.card_blue).setClickable(true);
        findViewById(R.id.card_green).setClickable(true);
        findViewById(R.id.card_pink).setClickable(true);
        findViewById(R.id.card_purple).setClickable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_colores);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtColorAdivinar = findViewById(R.id.txtColorAdivinar);
        txtFeedback = findViewById(R.id.txtFeedback);

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(ColoresActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        findViewById(R.id.btnAnimales).setOnClickListener(v -> {
            Intent intent = new Intent(ColoresActivity.this, AnimalesActivity.class);
            startActivity(intent);
        });
        konfettiView = findViewById(R.id.konfettiView);

        findViewById(R.id.card_blue).setOnClickListener(v -> verificarColor("AZUL", findViewById(R.id.border_blue)));
        findViewById(R.id.card_green).setOnClickListener(v -> verificarColor("VERDE", findViewById(R.id.border_green)));
        findViewById(R.id.card_pink).setOnClickListener(v -> verificarColor("ROSA", findViewById(R.id.border_pink)));
        findViewById(R.id.card_purple).setOnClickListener(v -> verificarColor("VIOLETA", findViewById(R.id.border_purple)));

    }
}