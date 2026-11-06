package com.example.memorypract.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.memorypract.data.modelos.AnimalItem;
import com.example.memorypract.data.repository.MetricaRepository;

import java.util.ArrayList;
import java.util.List;

public class AnimalesViewModel extends AndroidViewModel {

    private static final String TAG = "AnimalesViewModel";

    private final MetricaRepository metricaRepository;

    private final MutableLiveData<List<AnimalItem>> _listaAnimales = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<AnimalItem>> getListaAnimales() { return _listaAnimales; }

    private final MutableLiveData<Integer> _currentIndex = new MutableLiveData<>(0);
    public LiveData<Integer> getCurrentIndex() { return _currentIndex; }

    private final MutableLiveData<Integer> _correctCount = new MutableLiveData<>(0);
    public LiveData<Integer> getCorrectCount() { return _correctCount; }

    private final MutableLiveData<Integer> _incorrectCount = new MutableLiveData<>(0);
    public LiveData<Integer> getIncorrectCount() { return _incorrectCount; }

    private final MutableLiveData<Boolean> _isFlipped = new MutableLiveData<>(false);
    public LiveData<Boolean> isFlipped() { return _isFlipped; }

    private final MutableLiveData<Boolean> _isAnimating = new MutableLiveData<>(false);
    public LiveData<Boolean> isAnimating() { return _isAnimating; }

    private final MutableLiveData<Boolean> _isGameFinished = new MutableLiveData<>(false);
    public LiveData<Boolean> isGameFinished() { return _isGameFinished; }

    public AnimalesViewModel(@NonNull Application application) {
        super(application);
        metricaRepository = new MetricaRepository(application);
    }

    public void inicializarAnimales(List<AnimalItem> animales) {
        List<AnimalItem> actual = _listaAnimales.getValue();
        if (actual == null || actual.isEmpty()) {
            Log.d(TAG, "Inicializando lista de animales con " + (animales != null ? animales.size() : 0) + " elementos");
            _listaAnimales.setValue(animales);
        } else {
            Log.d(TAG, "La lista de animales ya estaba inicializada con " + actual.size() + " elementos");
        }
    }

    public void setAnimating(boolean animating) {
        Log.d(TAG, "Cambiando estado de animación: " + animating);
        _isAnimating.setValue(animating);
    }

    public void toggleFlipped() {
        Boolean flipped = _isFlipped.getValue();
        boolean nuevoFlipped = flipped == null || !flipped;
        Log.d(TAG, "Cambiando volteo de tarjeta a: " + nuevoFlipped);
        _isFlipped.setValue(nuevoFlipped);
    }

    public void setFlipped(boolean flipped) {
        Log.d(TAG, "Estableciendo volteo de tarjeta a: " + flipped);
        _isFlipped.setValue(flipped);
    }

    public void registrarRespuesta(boolean esCorrecto) {
        if (esCorrecto) {
            Integer current = _correctCount.getValue();
            int newCount = (current != null ? current : 0) + 1;
            Log.i(TAG, "Respuesta registrada: CORRECTA. Total aciertos=" + newCount);
            _correctCount.setValue(newCount);
            metricaRepository.registrarAcierto("invitado@memory.com", "ANIMALES", 1, 0);
        } else {
            Integer current = _incorrectCount.getValue();
            int newCount = (current != null ? current : 0) + 1;
            Log.w(TAG, "Respuesta registrada: INCORRECTA. Total errores=" + newCount);
            _incorrectCount.setValue(newCount);
            metricaRepository.registrarAcierto("invitado@memory.com", "ANIMALES", 0, 1);
        }
    }

    public void avanzarTarjeta() {
        List<AnimalItem> lista = _listaAnimales.getValue();
        Integer index = _currentIndex.getValue();
        int nextIndex = (index != null ? index : 0) + 1;

        _isFlipped.setValue(false);

        if (lista != null && nextIndex < lista.size()) {
            Log.i(TAG, "Avanzando a la tarjeta índice: " + nextIndex + " (" + lista.get(nextIndex).getNombre() + ")");
            _currentIndex.setValue(nextIndex);
        } else {
            Log.i(TAG, "Se completaron todas las tarjetas de animales. Finalizando juego.");
            _isGameFinished.setValue(true);
            metricaRepository.registrarAcierto("invitado@memory.com", "SERIE_COMPLETA", 1, 0);
        }
    }

    public void reiniciarJuego() {
        Log.i(TAG, "Reiniciando el juego de Animales");
        _currentIndex.setValue(0);
        _correctCount.setValue(0);
        _incorrectCount.setValue(0);
        _isFlipped.setValue(false);
        _isAnimating.setValue(false);
        _isGameFinished.setValue(false);
    }

    public AnimalItem getAnimalActual() {
        List<AnimalItem> lista = _listaAnimales.getValue();
        Integer index = _currentIndex.getValue();
        if (lista != null && index != null && index >= 0 && index < lista.size()) {
            return lista.get(index);
        }
        Log.w(TAG, "No se pudo obtener animal actual: lista o índice fuera de rango (index=" + index + ")");
        return null;
    }
}
