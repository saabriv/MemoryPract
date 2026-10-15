package com.example.memorypract.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.memorypract.data.modelos.ColorItem;
import com.example.memorypract.utils.GameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColoresViewModel extends ViewModel {

    private final List<ColorItem> todosLosColores = new ArrayList<>();

    private final MutableLiveData<Integer> _tandaActual = new MutableLiveData<>(0);

    private final MutableLiveData<String> _colorToGuess = new MutableLiveData<>();
    public LiveData<String> getColorToGuess() { return _colorToGuess; }

    private final MutableLiveData<List<ColorItem>> _colorItems = new MutableLiveData<>();
    public LiveData<List<ColorItem>> getColorItems() { return _colorItems; }

    public void verificarColor(String colorSeleccionado) {
        if (Boolean.TRUE.equals(_isProcessing.getValue())) {
            return;
        }

        String colorCorrecto = _colorToGuess.getValue();
       boolean esCorrecto = colorSeleccionado.equals(colorCorrecto);

        _isProcessing.setValue(true);

        if (esCorrecto) {
            Integer tanda = _tandaActual.getValue();
            if(tanda == null) tanda = 0;
            int tamanoTanda = 4;
            boolean esUltimaTanda = (tanda + 1) * tamanoTanda >= todosLosColores.size();
            _verificationEvent.setValue(new VerificationResult(colorSeleccionado, true, esUltimaTanda));
        } else {
            _verificationEvent.setValue(new VerificationResult(colorSeleccionado, false, false));
        }
    }

    public void finalizarVerificacion(boolean fueCorrecto) {
        _isProcessing.setValue(false);
        if (fueCorrecto) {
            Integer tanda = _tandaActual.getValue();
            if (tanda == null) tanda = 0;
            int siguienteTanda =tanda + 1;
            int tamanoTanda = 4;

            if (siguienteTanda * tamanoTanda >= todosLosColores.size()){
                siguienteTanda = 0;
            }
            _tandaActual.setValue(siguienteTanda);
            cargarTandaActual();
        }
    }

    public void reiniciar() {
        _tandaActual.setValue(0);
        _isProcessing.setValue(false);
        cargarTandaActual();
    }

    public  class VerificationResult {
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
        todosLosColores.clear();
        todosLosColores.add(new ColorItem("AZUL", GameConstants.COLOR_AZUL));
        todosLosColores.add(new ColorItem("VERDE", GameConstants.COLOR_VERDE));
        todosLosColores.add(new ColorItem("ROSA", GameConstants.COLOR_ROSA));
        todosLosColores.add(new ColorItem("VIOLETA", GameConstants.COLOR_VIOLETA));
        todosLosColores.add(new ColorItem("ROJO", GameConstants.COLOR_ROJO));
        todosLosColores.add(new ColorItem("NARANJA", GameConstants.COLOR_NARANJA));
        todosLosColores.add(new ColorItem("MARRON", GameConstants.COLOR_MARRON));
        todosLosColores.add(new ColorItem("AZUL OSCURO", GameConstants.COLOR_AZUL_OSCURO));

        _tandaActual.setValue(0);
        cargarTandaActual();
    }

    private void cargarTandaActual() {
        Integer tanda = _tandaActual.getValue();
        if (tanda == null) tanda = 0;

        int tamanoTanda = 4;
        int inicio = tanda * tamanoTanda;
        int fin = Math.min(inicio + tamanoTanda, todosLosColores.size());

        if (inicio >= todosLosColores.size()) {
            _tandaActual.setValue(0);
            cargarTandaActual();
            return;
        }

        List<ColorItem> sublistaTanda = new ArrayList<>(todosLosColores.subList(inicio,fin));
        _colorItems.setValue(sublistaTanda);

        Random random = new Random();
        ColorItem elegido = sublistaTanda.get(random.nextInt(sublistaTanda.size()));
        _colorToGuess.setValue(elegido.getNombre());
        }
    }

