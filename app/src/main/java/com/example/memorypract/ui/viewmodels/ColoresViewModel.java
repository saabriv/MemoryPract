package com.example.memorypract.ui.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.memorypract.data.modelos.ColorItem;
import com.example.memorypract.utils.GameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColoresViewModel extends ViewModel {

    private static final String TAG = "ColoresViewModel";

    private final List<ColorItem> todosLosColores = new ArrayList<>();

    private final MutableLiveData<Integer> _tandaActual = new MutableLiveData<>(0);

    private final MutableLiveData<String> _colorToGuess = new MutableLiveData<>();
    public LiveData<String> getColorToGuess() { return _colorToGuess; }

    private final MutableLiveData<List<ColorItem>> _colorItems = new MutableLiveData<>();
    public LiveData<List<ColorItem>> getColorItems() { return _colorItems; }

    public void verificarColor(String colorSeleccionado) {
        if (Boolean.TRUE.equals(_isProcessing.getValue())) {
            Log.d(TAG, "Ignorando selección de color: ya se encuentra procesando una verificación");
            return;
        }

        String colorCorrecto = _colorToGuess.getValue();
        boolean esCorrecto = colorSeleccionado != null && colorSeleccionado.equals(colorCorrecto);

        _isProcessing.setValue(true);

        if (esCorrecto) {
            Log.i(TAG, "Verificación exitosa: Color seleccionado=" + colorSeleccionado + " coincide con el objetivo=" + colorCorrecto);
            Integer tanda = _tandaActual.getValue();
            if(tanda == null) tanda = 0;
            int tamanoTanda = 4;
            boolean esUltimaTanda = (tanda + 1) * tamanoTanda >= todosLosColores.size();
            _verificationEvent.setValue(new VerificationResult(colorSeleccionado, true, esUltimaTanda));
        } else {
            Log.w(TAG, "Verificación errónea: Color seleccionado=" + colorSeleccionado + ", se esperaba=" + colorCorrecto);
            _verificationEvent.setValue(new VerificationResult(colorSeleccionado, false, false));
        }
    }

    public void finalizarVerificacion(boolean fueCorrecto) {
        Log.d(TAG, "Finalizando verificación (fueCorrecto=" + fueCorrecto + ")");
        _isProcessing.setValue(false);
        if (fueCorrecto) {
            Integer tanda = _tandaActual.getValue();
            if (tanda == null) tanda = 0;
            int siguienteTanda = tanda + 1;
            int tamanoTanda = 4;

            if (siguienteTanda * tamanoTanda >= todosLosColores.size()){
                Log.i(TAG, "Se completaron todas las tandas de colores. Reiniciando a tanda 0.");
                siguienteTanda = 0;
            }
            _tandaActual.setValue(siguienteTanda);
            cargarTandaActual();
        }
    }

    public void reiniciar() {
        Log.i(TAG, "Reiniciando ColoresViewModel");
        _tandaActual.setValue(0);
        _isProcessing.setValue(false);
        cargarTandaActual();
    }

    public static class VerificationResult {
        private final String colorSeleccionado;
        private final boolean isCorrect;
        private final boolean isCompleted;

        public VerificationResult(String colorSeleccionado, boolean isCorrect, boolean isCompleted) {
            this.colorSeleccionado = colorSeleccionado;
            this.isCorrect = isCorrect;
            this.isCompleted = isCompleted;
        }

        public String getColorSeleccionado() {
            return colorSeleccionado;
        }

        public boolean isCorrect() {
            return isCorrect;
        }

        public boolean isCompleted() {
            return isCompleted;
        }
    }

    private final MutableLiveData<VerificationResult> _verificationEvent = new MutableLiveData<>();
    public LiveData<VerificationResult> getVerificationEvent() { return _verificationEvent; }

    private final MutableLiveData<Boolean> _isProcessing = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsProcessing() { return _isProcessing; }

    public ColoresViewModel() {
        Log.d(TAG, "Creando ColoresViewModel");
        initGame();
    }

    public void initGame() {
        Log.d(TAG, "Inicializando lista de colores del juego");
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
            Log.w(TAG, "Índice de tanda inválido (" + inicio + "). Reiniciando tanda a 0.");
            _tandaActual.setValue(0);
            cargarTandaActual();
            return;
        }

        List<ColorItem> sublistaTanda = new ArrayList<>(todosLosColores.subList(inicio, fin));
        _colorItems.setValue(sublistaTanda);

        Random random = new Random();
        ColorItem elegido = sublistaTanda.get(random.nextInt(sublistaTanda.size()));
        _colorToGuess.setValue(elegido.getNombre());
        Log.i(TAG, "Cargada tanda " + tanda + " (" + sublistaTanda.size() + " colores). Color a adivinar: " + elegido.getNombre());
    }
}
