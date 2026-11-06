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
        cargarMetricasParaSemana(position);
    }

    private String calcularTagComparacion(int actual, int anterior) {
        if (anterior <= 0) {
            return "Sin datos";
        }
        int diff = actual - anterior;
        if (diff > 0) {
            int pct = (diff * 100) / anterior;
            return "↑ Superado (+" + pct + "%)";
        } else if (diff < 0) {
            int pct = (Math.abs(diff) * 100) / anterior;
            return "↓ Por debajo (-" + pct + "%)";
        } else {
            return "= Igual (100%)";
        }
    }

    private void cargarMetricasParaSemana(int index) {
        Log.d(TAG, "Cargando métricas desde la base de datos Room para la semana: " + index);

        // Rango de la semana seleccionada
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.WEEK_OF_YEAR, -index);

        long desdeMs = cal.getTimeInMillis();
        long hastaMs = desdeMs + (7L * 24 * 60 * 60 * 1000) - 1;

        // Rango de la semana anterior a la seleccionada
        long desdeAntMs = desdeMs - (7L * 24 * 60 * 60 * 1000);
        long hastaAntMs = desdeMs - 1;

        // Rango del mes actual
        Calendar calMes = Calendar.getInstance();
        calMes.set(Calendar.DAY_OF_MONTH, 1);
        calMes.set(Calendar.HOUR_OF_DAY, 0);
        calMes.set(Calendar.MINUTE, 0);
        calMes.set(Calendar.SECOND, 0);
        calMes.set(Calendar.MILLISECOND, 0);
        long desdeMesMs = calMes.getTimeInMillis();
        long hastaMesMs = System.currentTimeMillis();

        repository.cargarMetricasCompletas("invitado@memory.com",
                desdeMs, hastaMs,
                desdeAntMs, hastaAntMs,
                desdeMesMs, hastaMesMs,
                reporte -> {

                    int seriesActual = reporte.seriesActual;
                    int coloresActual = reporte.coloresActual;
                    int animalesActual = reporte.animalesActual;
                    int objetosActual = reporte.objetosActual;

                    int seriesAnterior = reporte.seriesAnterior;
                    int coloresAnterior = reporte.coloresAnterior;
                    int animalesAnterior = reporte.animalesAnterior;
                    int objetosAnterior = reporte.objetosAnterior;

                    String seriesTag = calcularTagComparacion(seriesActual, seriesAnterior);
                    String coloresTag = calcularTagComparacion(coloresActual, coloresAnterior);
                    String animalesTag = calcularTagComparacion(animalesActual, animalesAnterior);
                    String objetosTag = calcularTagComparacion(objetosActual, objetosAnterior);

                    int seriesTotalesMes = reporte.seriesTotalesMes;
                    int aciertosTotalesMes = reporte.aciertosTotalesMes;

                    // % Cumplimiento calculado basado en BD (Meta mensual: 150 aciertos)
                    int metaMesAciertos = 150;
                    int porcentajeCumplimiento = aciertosTotalesMes > 0 ? Math.min(100, (aciertosTotalesMes * 100) / metaMesAciertos) : 0;

                    String nombreSemana;
                    String rangoFechaText;
                    String nombreMesText = "Octubre 2026";

                    if (index == 0) {
                        nombreSemana = "Semana Actual: 12 Oct - 18 Oct (Actual)";
                        rangoFechaText = "12 Oct - 18 Oct, 2026";
                    } else if (index == 1) {
                        nombreSemana = "Semana Anterior: 05 Oct - 11 Oct";
                        rangoFechaText = "05 Oct - 11 Oct, 2026";
                    } else {
                        nombreSemana = "Semana: 28 Sep - 04 Oct";
                        rangoFechaText = "28 Sep - 04 Oct, 2026";
                        nombreMesText = "Septiembre 2026";
                    }

                    MetricasData data = new MetricasData(
                            nombreSemana,
                            rangoFechaText,
                            seriesActual, 6, seriesAnterior, seriesTag,
                            coloresActual, 20, coloresAnterior, coloresTag,
                            animalesActual, 22, animalesAnterior, animalesTag,
                            objetosActual, 18, objetosAnterior, objetosTag,
                            nombreMesText, seriesTotalesMes, aciertosTotalesMes, porcentajeCumplimiento
                    );

                    _metricasData.postValue(data);
                    Log.i(TAG, "Métricas cargadas exitosamente desde Room SQLite para la semana " + index);
                });
    }

    public void reiniciarMetricas() {
        Log.w(TAG, "Borrando métricas del usuario en la base de datos Room SQLite");
        repository.borrarMetricasPorUsuario("invitado@memory.com", () -> _metricasData.postValue(new MetricasData(
                "Semana Actual: 12 Oct - 18 Oct (Actual)",
                "12 Oct - 18 Oct, 2026",
                0, 6, 0, "Sin datos",
                0, 20, 0, "Sin datos",
                0, 22, 0, "Sin datos",
                0, 18, 0, "Sin datos",
                "Octubre 2026", 0, 0, 0
        )));
    }
}
