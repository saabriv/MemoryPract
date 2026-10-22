package com.example.memorypract.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.memorypract.data.modelos.MetricasData;

import java.util.ArrayList;
import java.util.List;

public class MetricasViewModel extends ViewModel {

    private final MutableLiveData<MetricasData> _metricasData = new MutableLiveData<>();
    public LiveData<MetricasData> getMetricasData() { return _metricasData; }

    private final MutableLiveData<List<String>> _semanasDisponibles = new MutableLiveData<>();
    public LiveData<List<String>> getSemanasDisponibles() { return _semanasDisponibles; }

    private final MutableLiveData<Integer> _selectedSemanaIndex = new MutableLiveData<>(0);
    public LiveData<Integer> getSelectedSemanaIndex() { return _selectedSemanaIndex; }

    public MetricasViewModel() {
        initSemanas();
        cargarMetricasParaSemana(0);
    }

    private void initSemanas() {
        List<String> semanas = new ArrayList<>();
        semanas.add("Semana Actual: 12 Oct - 18 Oct (Actual)");
        semanas.add("Semana Anterior: 05 Oct - 11 Oct");
        semanas.add("Semana: 28 Sep - 04 Oct");
        _semanasDisponibles.setValue(semanas);
    }

    public void seleccionarSemana(int position) {
        _selectedSemanaIndex.setValue(position);
        cargarMetricasParaSemana(position);
    }

    private void cargarMetricasParaSemana(int index) {
        if (index == 0) {
            _metricasData.setValue(new MetricasData(
                    "Semana Actual: 12 Oct - 18 Oct (Actual)",
                    "12 Oct - 18 Oct, 2026",
                    8, 6, 6, "↗ Superado (+33%)",
                    24, 20, 18, "↗ Superado (+33%)",
                    19, 22, 22, "= Por debajo (-13%)",
                    18, 18, 18, "✓ Igual (100%)",
                    "Octubre 2026", 28, 182, 94
            ));
        } else if (index == 1) {
            _metricasData.setValue(new MetricasData(
                    "Semana Anterior: 05 Oct - 11 Oct",
                    "05 Oct - 11 Oct, 2026",
                    6, 6, 5, "↗ Superado (+20%)",
                    18, 20, 15, "↗ Superado (+20%)",
                    22, 22, 20, "↗ Superado (+10%)",
                    18, 18, 16, "↗ Superado (+12%)",
                    "Octubre 2026", 28, 182, 94
            ));
        } else {
            _metricasData.setValue(new MetricasData(
                    "Semana: 28 Sep - 04 Oct",
                    "28 Sep - 04 Oct, 2026",
                    5, 6, 4, "↗ Superado (+25%)",
                    15, 20, 12, "↗ Superado (+25%)",
                    20, 22, 18, "↗ Superado (+11%)",
                    16, 18, 15, "↗ Superado (+6%)",
                    "Septiembre 2026", 24, 150, 88
            ));
        }
    }

    public void reiniciarMetricas() {
        _metricasData.setValue(new MetricasData(
                "Semana Actual: 12 Oct - 18 Oct (Actual)",
                "12 Oct - 18 Oct, 2026",
                0, 6, 6, "= En inicio",
                0, 20, 18, "= En inicio",
                0, 22, 22, "= En inicio",
                0, 18, 18, "= En inicio",
                "Octubre 2026", 0, 0, 0
        ));
    }
}
