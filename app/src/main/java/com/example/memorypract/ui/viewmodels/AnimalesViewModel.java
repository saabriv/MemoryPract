package com.example.memorypract.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.memorypract.data.modelos.AnimalItem;

import java.util.ArrayList;
import java.util.List;

public class AnimalesViewModel extends ViewModel {
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

    public void inicializarAnimales(List<AnimalItem> animales) {
        List<AnimalItem> actual = _listaAnimales.getValue();
        if (actual == null || actual.isEmpty()) {
            _listaAnimales.setValue(animales);
        }
    }

    public void setAnimating(boolean animating) {
        _isAnimating.setValue(animating);
    }

    public void toggleFlipped() {
        Boolean flipped = _isFlipped.getValue();
        _isFlipped.setValue(flipped == null || !flipped);
    }

    public void setFlipped(boolean flipped) {
        _isFlipped.setValue(flipped);
    }

    public void registrarRespuesta(boolean esCorrecto) {
        if (esCorrecto) {
            Integer current = _correctCount.getValue();
            _correctCount.setValue((current != null ? current : 0) + 1);
        } else {
            Integer current = _incorrectCount.getValue();
            _incorrectCount.setValue((current != null ? current : 0) + 1);
        }
    }

    public void avanzarTarjeta() {
        List<AnimalItem> lista = _listaAnimales.getValue();
        Integer index = _currentIndex.getValue();
        int nextIndex = (index != null ? index : 0) + 1;

        _isFlipped.setValue(false);

        if (lista != null && nextIndex < lista.size()) {
            _currentIndex.setValue(nextIndex);
        } else {
            _isGameFinished.setValue(true);
        }
    }

    public void reiniciarJuego() {
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
        return null;
    }
}
