package com.example.memorypract.ui.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.memorypract.R;
import com.example.memorypract.data.modelos.MetricasData;
import com.example.memorypract.ui.viewmodels.MetricasViewModel;

public class MetricasActivity extends AppCompatActivity {

    private MetricasViewModel viewModel;

    private Spinner spinnerSemana;
    private TextView tvSelectedWeekBadge;

    // Series
    private TextView tvSeriesValue;
    private TextView tvSeriesGoal;
    private ProgressBar pbSeries;
    private TextView tvSeriesVsPrev;
    private TextView tvSeriesTag;

    // Colores
    private TextView tvColoresValue;
    private TextView tvColoresGoal;
    private ProgressBar pbColores;
    private TextView tvColoresVsPrev;
    private TextView tvColoresTag;

    // Animales
    private TextView tvAnimalesValue;
    private TextView tvAnimalesGoal;
    private ProgressBar pbAnimales;
    private TextView tvAnimalesVsPrev;
    private TextView tvAnimalesTag;

    // Objetos
    private TextView tvObjetosValue;
    private TextView tvObjetosGoal;
    private ProgressBar pbObjetos;
    private TextView tvObjetosVsPrev;
    private TextView tvObjetosTag;

    // Summary
    private TextView tvMonthlySummaryTitle;
    private TextView tvSeriesTotalesMes;
    private TextView tvAciertosTotalesMes;
    private TextView tvMonthlyMessage;

    private boolean isInitialSpinnerSelection = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_metricas);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(MetricasViewModel.class);

        initViews();
        setupListeners();
        observeViewModel();
    }

    private void initViews() {
        spinnerSemana = findViewById(R.id.spinnerSemana);
        tvSelectedWeekBadge = findViewById(R.id.tvSelectedWeekBadge);

        tvSeriesValue = findViewById(R.id.tvSeriesValue);
        tvSeriesGoal = findViewById(R.id.tvSeriesGoal);
        pbSeries = findViewById(R.id.pbSeries);
        tvSeriesVsPrev = findViewById(R.id.tvSeriesVsPrev);
        tvSeriesTag = findViewById(R.id.tvSeriesTag);

        tvColoresValue = findViewById(R.id.tvColoresValue);
        tvColoresGoal = findViewById(R.id.tvColoresGoal);
        pbColores = findViewById(R.id.pbColores);
        tvColoresVsPrev = findViewById(R.id.tvColoresVsPrev);
        tvColoresTag = findViewById(R.id.tvColoresTag);

        tvAnimalesValue = findViewById(R.id.tvAnimalesValue);
        tvAnimalesGoal = findViewById(R.id.tvAnimalesGoal);
        pbAnimales = findViewById(R.id.pbAnimales);
        tvAnimalesVsPrev = findViewById(R.id.tvAnimalesVsPrev);
        tvAnimalesTag = findViewById(R.id.tvAnimalesTag);

        tvObjetosValue = findViewById(R.id.tvObjetosValue);
        tvObjetosGoal = findViewById(R.id.tvObjetosGoal);
        pbObjetos = findViewById(R.id.pbObjetos);
        tvObjetosVsPrev = findViewById(R.id.tvObjetosVsPrev);
        tvObjetosTag = findViewById(R.id.tvObjetosTag);

        tvMonthlySummaryTitle = findViewById(R.id.tvMonthlySummaryTitle);
        tvSeriesTotalesMes = findViewById(R.id.tvSeriesTotalesMes);
        tvAciertosTotalesMes = findViewById(R.id.tvAciertosTotalesMes);
        tvMonthlyMessage = findViewById(R.id.tvMonthlyMessage);
    }

    private void setupListeners() {
        findViewById(R.id.btnSettings).setOnClickListener(v -> {
            Intent intent = new Intent(MetricasActivity.this, ConfiguracionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnReiniciar).setOnClickListener(v -> {
            viewModel.reiniciarMetricas();
            Toast.makeText(MetricasActivity.this, "Métricas reiniciadas", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnVolverMenu).setOnClickListener(v -> {
            Intent intent = new Intent(MetricasActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        spinnerSemana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInitialSpinnerSelection) {
                    isInitialSpinnerSelection = false;
                    return;
                }
                viewModel.seleccionarSemana(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void observeViewModel() {
        viewModel.getSemanasDisponibles().observe(this, semanas -> {
            if (semanas != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, semanas);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSemana.setAdapter(adapter);
            }
        });

        viewModel.getMetricasData().observe(this, data -> {
            if (data != null) {
                updateUI(data);
            }
        });
    }

    private void updateUI(MetricasData data) {
        tvSelectedWeekBadge.setText(data.getRangoSemana());

        // Series
        tvSeriesValue.setText(String.valueOf(data.getSeriesRealizadas()));
        tvSeriesGoal.setText("Objetivo: " + data.getSeriesObjetivo());
        int progressSeries = data.getSeriesObjetivo() > 0 ? (data.getSeriesRealizadas() * 100 / data.getSeriesObjetivo()) : 0;
        pbSeries.setProgress(Math.min(progressSeries, 100));
        tvSeriesVsPrev.setText("vs Semana Anterior (" + data.getSeriesAnterior() + ")");
        tvSeriesTag.setText(data.getSeriesTag());

        // Colores
        tvColoresValue.setText(String.valueOf(data.getColoresAcertados()));
        tvColoresGoal.setText("Objetivo: " + data.getColoresObjetivo());
        int progressColores = data.getColoresObjetivo() > 0 ? (data.getColoresAcertados() * 100 / data.getColoresObjetivo()) : 0;
        pbColores.setProgress(Math.min(progressColores, 100));
        tvColoresVsPrev.setText("vs Semana Anterior (" + data.getColoresAnterior() + ")");
        tvColoresTag.setText(data.getColoresTag());

        // Animales
        tvAnimalesValue.setText(String.valueOf(data.getAnimalesReconocidos()));
        tvAnimalesGoal.setText("Objetivo: " + data.getAnimalesObjetivo());
        int progressAnimales = data.getAnimalesObjetivo() > 0 ? (data.getAnimalesReconocidos() * 100 / data.getAnimalesObjetivo()) : 0;
        pbAnimales.setProgress(Math.min(progressAnimales, 100));
        tvAnimalesVsPrev.setText("vs Semana Anterior (" + data.getAnimalesAnterior() + ")");
        tvAnimalesTag.setText(data.getAnimalesTag());

        // Objetos
        tvObjetosValue.setText(String.valueOf(data.getObjetosIdentificados()));
        tvObjetosGoal.setText("Objetivo: " + data.getObjetosObjetivo());
        int progressObjetos = data.getObjetosObjetivo() > 0 ? (data.getObjetosIdentificados() * 100 / data.getObjetosObjetivo()) : 0;
        pbObjetos.setProgress(Math.min(progressObjetos, 100));
        tvObjetosVsPrev.setText("vs Semana Anterior (" + data.getObjetosAnterior() + ")");
        tvObjetosTag.setText(data.getObjetosTag());

        // Summary
        tvMonthlySummaryTitle.setText("Resumen Mensual (" + data.getResumenMesNombre() + ")");
        tvSeriesTotalesMes.setText(String.valueOf(data.getSeriesTotalesMes()));
        tvAciertosTotalesMes.setText(String.valueOf(data.getAciertosTotalesMes()));
        tvMonthlyMessage.setText("✨ ¡Excelente ritmo! Tienes un " + data.getPorcentajeCumplimiento() + "% de cumplimiento de tus metas mensuales.");
    }
}
