package com.example.memorypract.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.memorypract.data.local.AppDatabase;
import com.example.memorypract.data.local.MetricaDao;
import com.example.memorypract.data.modelos.MetricaEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MetricaRepository {

    private static final String TAG = "MetricaRepository";

    private final MetricaDao metricaDao;
    private final ExecutorService executor;

    public interface MetricaCallback<T> {
        void onResult(T result);
    }

    public MetricaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        metricaDao = db.metricaDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public void registrarAcierto(String usuarioCorreo, String categoria, int aciertos, int fallos) {
        executor.execute(() -> {
            String correoValido = (usuarioCorreo != null && !usuarioCorreo.trim().isEmpty()) ? usuarioCorreo.trim() : "invitado@memory.com";
            MetricaEntity entity = new MetricaEntity(
                    correoValido,
                    categoria,
                    aciertos,
                    fallos,
                    System.currentTimeMillis()
            );
            long id = metricaDao.insertarMetrica(entity);
            Log.i(TAG, "Registrada métrica en SQLite id=" + id + ", correo=" + correoValido + ", cat=" + categoria + ", aciertos=" + aciertos);
        });
    }

    public LiveData<List<MetricaEntity>> obtenerPorUsuarioLiveData(String usuarioCorreo) {
        String correoValido = (usuarioCorreo != null && !usuarioCorreo.trim().isEmpty()) ? usuarioCorreo.trim() : "invitado@memory.com";
        return metricaDao.obtenerPorUsuarioLiveData(correoValido);
    }

    public void cargarMetricasCompletas(String usuarioCorreo,
                                        long desdeMs, long hastaMs,
                                        long desdeAntMs, long hastaAntMs,
                                        long desdeMesMs, long hastaMesMs,
                                        MetricaCallback<MetricasReporte> callback) {
        executor.execute(() -> {
            String correoValido = (usuarioCorreo != null && !usuarioCorreo.trim().isEmpty()) ? usuarioCorreo.trim() : "invitado@memory.com";

            int seriesActual = metricaDao.obtenerSeriesPorFecha(correoValido, desdeMs, hastaMs);
            int coloresActual = metricaDao.obtenerAciertosPorCategoriaYFecha(correoValido, "COLORES", desdeMs, hastaMs);
            int animalesActual = metricaDao.obtenerAciertosPorCategoriaYFecha(correoValido, "ANIMALES", desdeMs, hastaMs);
            int objetosActual = metricaDao.obtenerAciertosPorCategoriaYFecha(correoValido, "OBJETOS", desdeMs, hastaMs);

            int seriesAnterior = metricaDao.obtenerSeriesPorFecha(correoValido, desdeAntMs, hastaAntMs);
            int coloresAnterior = metricaDao.obtenerAciertosPorCategoriaYFecha(correoValido, "COLORES", desdeAntMs, hastaAntMs);
            int animalesAnterior = metricaDao.obtenerAciertosPorCategoriaYFecha(correoValido, "ANIMALES", desdeAntMs, hastaAntMs);
            int objetosAnterior = metricaDao.obtenerAciertosPorCategoriaYFecha(correoValido, "OBJETOS", desdeAntMs, hastaAntMs);

            int seriesTotalesMes = metricaDao.obtenerSeriesPorFecha(correoValido, desdeMesMs, hastaMesMs);
            int aciertosTotalesMes = metricaDao.obtenerAciertosTotalesPorFecha(correoValido, desdeMesMs, hastaMesMs);

            MetricasReporte reporte = new MetricasReporte(
                    seriesActual, coloresActual, animalesActual, objetosActual,
                    seriesAnterior, coloresAnterior, animalesAnterior, objetosAnterior,
                    seriesTotalesMes, aciertosTotalesMes
            );

            if (callback != null) {
                callback.onResult(reporte);
            }
        });
    }

    public void borrarMetricasPorUsuario(String usuarioCorreo, Runnable onComplete) {
        executor.execute(() -> {
            String correoValido = (usuarioCorreo != null && !usuarioCorreo.trim().isEmpty()) ? usuarioCorreo.trim() : "invitado@memory.com";
            metricaDao.borrarPorUsuario(correoValido);
            Log.w(TAG, "Borradas métricas de SQLite para usuario: " + correoValido);
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static class MetricasReporte {
        public final int seriesActual;
        public final int coloresActual;
        public final int animalesActual;
        public final int objetosActual;

        public final int seriesAnterior;
        public final int coloresAnterior;
        public final int animalesAnterior;
        public final int objetosAnterior;

        public final int seriesTotalesMes;
        public final int aciertosTotalesMes;

        public MetricasReporte(int seriesActual, int coloresActual, int animalesActual, int objetosActual,
                               int seriesAnterior, int coloresAnterior, int animalesAnterior, int objetosAnterior,
                               int seriesTotalesMes, int aciertosTotalesMes) {
            this.seriesActual = seriesActual;
            this.coloresActual = coloresActual;
            this.animalesActual = animalesActual;
            this.objetosActual = objetosActual;
            this.seriesAnterior = seriesAnterior;
            this.coloresAnterior = coloresAnterior;
            this.animalesAnterior = animalesAnterior;
            this.objetosAnterior = objetosAnterior;
            this.seriesTotalesMes = seriesTotalesMes;
            this.aciertosTotalesMes = aciertosTotalesMes;
        }
    }
}
