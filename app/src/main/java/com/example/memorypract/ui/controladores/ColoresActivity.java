package com.example.memorypract.ui.controladores;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorypract.R;
import com.example.memorypract.ui.adaptadores.ColorAdapter;
import com.example.memorypract.ui.viewmodels.ColoresViewModel;

import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class ColoresActivity extends AppCompatActivity {

    private ColoresViewModel viewModel;
    private TextView txtColorAdivinar;
    private TextView txtFeedback;
    private KonfettiView konfettiView;
    private RecyclerView rvColores;
    private View lastBorderView;

    private void lanzarConfeti() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        Party party = new PartyFactory(emitterConfig)
                .angle(270)
                .spread(90)
                .position(new Position.Relative(0.5, 0.4))
                .timeToLive(2000L)
                .build();

        if (konfettiView != null) {
            konfettiView.start(party);
        }
    }

    private void deshabilitarBotones() {
        if (rvColores != null) {
            rvColores.setClickable(false);
            rvColores.setEnabled(false);
        }
    }

    private void habilitarBotones() {
        if (rvColores != null) {
            rvColores.setClickable(true);
            rvColores.setEnabled(true);
        }
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
        konfettiView = findViewById(R.id.konfettiView);
        rvColores = findViewById(R.id.rvColores);

        viewModel = new ViewModelProvider(this).get(ColoresViewModel.class);

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(ColoresActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.btnAnimales).setOnClickListener(v -> {
            Intent intent = new Intent(ColoresActivity.this, AnimalesActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnObjetos).setOnClickListener(v -> {
            Intent intent = new Intent(ColoresActivity.this, PantallaobjetosActivity.class);
            startActivity(intent);
        });

        viewModel.getColorToGuess().observe(this, color -> {
            if (color != null) {
                txtColorAdivinar.setText(color);
            }
        });

        viewModel.getColorItems().observe(this, items -> {
            if (items != null) {
                ColorAdapter adapter = new ColorAdapter(items, (colorItem, borderView) -> {
                    lastBorderView = borderView;
                    viewModel.verificarColor(colorItem.getNombre());
                });
                rvColores.setAdapter(adapter);
            }
        });

        viewModel.getVerificationEvent().observe(this, result -> {
            if (result == null) return;

            final View borderView = lastBorderView;

            if (result.isCorrect) {
                if (borderView != null) {
                    borderView.setBackgroundResource(R.drawable.border_correct);
                }
                txtFeedback.setText(R.string.message_correct);
                txtFeedback.setTextColor(0xFF4ADE80);
                txtFeedback.setVisibility(View.VISIBLE);
                lanzarConfeti();
                deshabilitarBotones();

                new Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    if (borderView != null) {
                        borderView.setBackgroundResource(R.drawable.border_transparent);
                    }
                    txtFeedback.setVisibility(View.INVISIBLE);
                    habilitarBotones();

                    viewModel.finalizarVerificacion(true);

                    if (result.isCompleted) {
                        Toast.makeText(this, R.string.message_complete, Toast.LENGTH_LONG).show();
                    }
                }, 1500);

            } else {
                if (borderView != null) {
                    borderView.setBackgroundResource(R.drawable.border_incorrect);
                }
                txtFeedback.setText(R.string.message_incorrect);
                txtFeedback.setTextColor(0xFFF87171);
                txtFeedback.setVisibility(View.VISIBLE);
                deshabilitarBotones();

                new Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    if (borderView != null) {
                        borderView.setBackgroundResource(R.drawable.border_transparent);
                    }
                    txtFeedback.setVisibility(View.INVISIBLE);
                    habilitarBotones();

                    viewModel.finalizarVerificacion(false);
                }, 1200);
            }
        });
    }
}
