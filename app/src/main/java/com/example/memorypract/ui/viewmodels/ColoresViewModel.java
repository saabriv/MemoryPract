package com.example.memorypract.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.memorypract.data.modelos.ColorItem;
import com.example.memorypract.utils.GameConstants;

import java.util.ArrayList;
import java.util.List;

public class ColoresViewModel extends ViewModel {
    private final String[] listaColores = GameConstants.LISTA_COLORES;

    private final MutableLiveData<Integer> _indiceActual = new MutableLiveData<>(0);
    public LiveData<Integer> getIndiceActual() { return _indiceActual; }

    private final MutableLiveData<String> _colorToGuess = new MutableLiveData<>();
    public LiveData<String> getColorToGuess() { return _colorToGuess; }

    private final MutableLiveData<List<ColorItem>> _colorItems = new MutableLiveData<>();
    public LiveData<List<ColorItem>> getColorItems() { return _colorItems; }

    public static class VerificationResult {
        public final String colorSeleccionado;
        public final boolean isCorrect;
        public final boolean isCompleted;

        public VerificationResult(String colorSeleccionado, boolean isCorrect, boolean isCompleted) {
            this.colorSeleccionado = colorSeleccionado;
            this.isCorrect = isCorrect;
            this.isCompleted = isCompleted;
        }
    }

    private final MutableLiveData<VerificationResult> _verificationEvent = new MutableLiveData<>();
    public LiveData<VerificationResult> getVerificationEvent() { return _verificationEvent; }

    private final MutableLiveData<Boolean> _isProcessing = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsProcessing() { return _isProcessing; }

    public ColoresViewModel() {
        initGame();
    }

    public void initGame() {
        List<ColorItem> items = new ArrayList<>();
        items.add(new ColorItem("AZUL", GameConstants.COLOR_AZUL));
        items.add(new ColorItem("VERDE", GameConstants.COLOR_VERDE));
        items.add(new ColorItem("ROSA", GameConstants.COLOR_ROSA));
        items.add(new ColorItem("VIOLETA", GameConstants.COLOR_VIOLETA));
        _colorItems.setValue(items);

        if (_indiceActual.getValue() == null) {
            _indiceActual.setValue(0);
        }
        updateColorToGuess();
    }

    private void updateColorToGuess() {
        Integer index = _indiceActual.getValue();
        if (index != null && index >= 0 && index < listaColores.length) {
            _colorToGuess.setValue(listaColores[index]);
        }
    }

    public void verificarColor(String colorSeleccionado) {
        if (Boolean.TRUE.equals(_isProcessing.getValue())) {
            return;
        }

        Integer index = _indiceActual.getValue();
        if (index == null) index = 0;

        String colorCorrecto = listaColores[index];
        boolean esCorrecto = colorSeleccionado.equals(colorCorrecto);

        _isProcessing.setValue(true);

        if (esCorrecto) {
            int siguienteIndice = index + 1;
            boolean completado = siguienteIndice >= listaColores.length;
            _verificationEvent.setValue(new VerificationResult(colorSeleccionado, true, completado));
        } else {
            _verificationEvent.setValue(new VerificationResult(colorSeleccionado, false, false));
        }
    }

    public void finalizarVerificacion(boolean fueCorrecto) {
        _isProcessing.setValue(false);
        if (fueCorrecto) {
            Integer index = _indiceActual.getValue();
            if (index == null) index = 0;
            int siguienteIndice = index + 1;
            if (siguienteIndice >= listaColores.length) {
                siguienteIndice = 0;
            }
            _indiceActual.setValue(siguienteIndice);
            updateColorToGuess();
        }
    }

    public void reiniciar() {
        _indiceActual.setValue(0);
        _isProcessing.setValue(false);
        updateColorToGuess();
    }

    public int getTotalColores() {
        return listaColores.length;
    }
}
