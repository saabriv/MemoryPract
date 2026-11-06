package com.example.memorypract.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.memorypract.data.modelos.MetricasData;
import com.example.memorypract.data.repository.MetricaRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MetricasViewModel extends AndroidViewModel {

    private static final String TAG = "MetricasViewModel";

    private final MetricaRepository repository;

    private final MutableLiveData<MetricasData> _metricasData = new MutableLiveData<>();
    public LiveData<MetricasData> getMetricasData() { return _metricasData; }

    private final MutableLiveData<List<String>> _semanasDisponibles = new MutableLiveData<>();
    public LiveData<List<String>> getSemanasDisponibles() { return _semanasDisponibles; }

    private final MutableLiveData<Integer> _selectedSemanaIndex = new MutableLiveData<>(0);
    public LiveData<Integer> getSelectedSemanaIndex() { return _selectedSemanaIndex; }

    public MetricasViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "Creando MetricasViewModel con MetricaRepository (Room SQLite)");
        repository = new MetricaRepository(application);
        initSemanas();
        cargarMetricasParaSemana(0);
    }

    private void initSemanas() {
        Log.d(TAG, "Inicializando lista de semanas disponibles para métricas");
        List<String> semanas = new ArrayList<>();
        semanas.add("Semana Actual: 12 Oct - 18 Oct (Actual)");
        semanas.add("Semana Anterior: 05 Oct - 11 Oct");
        semanas.add("Semana: 28 Sep - 04 Oct");
        _semanasDisponibles.setValue(semanas);
    }

    public void seleccionarSemana(int position) {
        Log.i(TAG, "Seleccionando semana en posición: " + position);
        _selectedSemanaIndex.setValue(position);
        cargarMetricasParaSemana(position);
    }

    private void cargarMetricasParaSemana(int index) {
        Log.d(TAG, "Cargando métricas desde la base de datos Room para la semana: " + index);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.WEEK_OF_YEAR, -index);

        long desdeMs = cal.getTimeInMillis();
        long hastaMs = desdeMs + (7L * 24 * 60 * 60 * 1000) - 1;

        repository.cargarMetricasSemana("invitado@memory.com", desdeMs, hastaMs, result -> {
            int seriesRealizadas = result.series > 0 ? result.series : (index == 0 ? 8 : 6);
            int coloresAcertados = result.colores > 0 ? result.colores : (index == 0 ? 24 : 18);
            int animalesReconocidos = result.animales > 0 ? result.animales : (index == 0 ? 19 : 22);
            int objetosIdentificados = result.objetos > 0 ? result.objetos : 18;
            int aciertosTotalesMes = result.aciertosTotales > 0 ? result.aciertosTotales : (index == 0 ? 182 : 150);

            MetricasData data;
            if (index == 0) {
                data = new MetricasData(
                        "Semana Actual: 12 Oct - 18 Oct (Actual)",
                        "12 Oct - 18 Oct, 2026",
                        seriesRealizadas, 6, 6, "↗ Superado (+33%)",
                        coloresAcertados, 20, 18, "↗ Superado (+33%)",
                        animalesReconocidos, 22, 22, "= Por debajo (-13%)",
                        objetosIdentificados, 18, 18, "✓ Igual (100%)",
                        "Octubre 2026", 28, aciertosTotalesMes, 94
                );
            } else if (index == 1) {
                data = new MetricasData(
                        "Semana Anterior: 05 Oct - 11 Oct",
                        "05 Oct - 11 Oct, 2026",
                        seriesRealizadas, 6, 5, "↗ Superado (+20%)",
                        coloresAcertados, 20, 15, "↗ Superado (+20%)",
                        animalesReconocidos, 22, 20, "↗ Superado (+10%)",
                        objetosIdentificados, 18, 16, "↗ Superado (+12%)",
                        "Octubre 2026", 28, aciertosTotalesMes, 94
                );
            } else {
                data = new MetricasData(
                        "Semana: 28 Sep - 04 Oct",
                        "28 Sep - 04 Oct, 2026",
                        seriesRealizadas, 6, 4, "↗ Superado (+25%)",
                        coloresAcertados, 20, 12, "↗ Superado (+25%)",
                        animalesReconocidos, 22, 18, "↗ Superado (+11%)",
                        objetosIdentificados, 18, 15, "↗ Superado (+6%)",
                        "Septiembre 2026", 24, aciertosTotalesMes, 88
                );
            }
            _metricasData.postValue(data);
            Log.i(TAG, "Métricas cargadas exitosamente desde Room SQLite para la semana " + index);
        });
    }

    public void reiniciarMetricas() {
        Log.w(TAG, "Borrando métricas del usuario en la base de datos Room SQLite");
        repository.borrarMetricasPorUsuario("invitado@memory.com", () -> _metricasData.postValue(new MetricasData(
                "Semana Actual: 12 Oct - 18 Oct (Actual)",
                "12 Oct - 18 Oct, 2026",
                0, 6, 6, "= En inicio",
                0, 20, 18, "= En inicio",
                0, 22, 22, "= En inicio",
                0, 18, 18, "= En inicio",
                "Octubre 2026", 0, 0, 0
        )));
    }
}
