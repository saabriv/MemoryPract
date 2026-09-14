package com.example.memorypract.Controladores;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.memorypract.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PantallaobjetosActivity extends AppCompatActivity {

    private String targetWord = "ARBOL";
    private List<Character> availableLetters;
    private TextView[] slotTextViews;
    private Button[] keyButtons;
    private List<Integer> selectedKeyIndices;

    private LinearLayout layoutWordSlots;
    private GridLayout gridKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pantallaobjetos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botón volver
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(PantallaobjetosActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        //nav inferior
        findViewById(R.id.btnAnimales).setOnClickListener(v -> {
            Intent intent = new Intent(PantallaobjetosActivity.this, AnimalesActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btnColores).setOnClickListener(v -> {
            Intent intent = new Intent(PantallaobjetosActivity.this, ColoresActivity.class);
            startActivity(intent);
        });

        layoutWordSlots = findViewById(R.id.layoutWordSlots);
        gridKeyboard = findViewById(R.id.gridKeyboard);
        selectedKeyIndices = new ArrayList<>();

        // Si se pasa una palabra desde otra pantalla o nivel, se recibe por Intent
        if (getIntent() != null && getIntent().hasExtra("WORD")) {
            String wordFromIntent = getIntent().getStringExtra("WORD");
            if (wordFromIntent != null && !wordFromIntent.trim().isEmpty()) {
                targetWord = wordFromIntent.trim().toUpperCase();
            }
        }

        // Cargar la palabra y generar las casillas y letras correspondientes
        loadWord(targetWord);


        Button btnDelete = findViewById(R.id.btnDelete);
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> deleteLastLetter());
        }

        Button btnCheck = findViewById(R.id.btnCheck);
        if (btnCheck != null) {
            btnCheck.setOnClickListener(v -> checkWord());
        }
    }

    /**
     * Carga cualquier palabra y genera dinámicamente:
     * 1. La cantidad exacta de cuadritos (slots) que tiene la palabra.
     * 2. El teclado de letras (las letras de la palabra + distractores).
     */
    public void loadWord(String word) {
        if (word == null || word.isEmpty()) return;

        this.targetWord = word.toUpperCase();
        selectedKeyIndices.clear();

        generateAvailableLetters();
        setupWordSlots();
        setupKeyboard();
    }

    private void generateAvailableLetters() {
        availableLetters = new ArrayList<>();

        // 1. Agregar todas las letras de la palabra objetivo
        for (char c : targetWord.toCharArray()) {
            availableLetters.add(c);
        }

        // 2. Definir cantidad total de teclas (12 o 14 según la longitud)
        int totalTargetKeys = targetWord.length() > 6 ? 14 : 12;

        // 3. Rellenar con letras aleatorias hasta alcanzar el total
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        while (availableLetters.size() < totalTargetKeys) {
            char randomChar = alphabet.charAt(random.nextInt(alphabet.length()));
            availableLetters.add(randomChar);
        }

        // 4. Mezclar las letras aleatoriamente
        Collections.shuffle(availableLetters);
    }

    private void setupWordSlots() {
        layoutWordSlots.removeAllViews();
        int length = targetWord.length(); // Genera exactamente tantas casillas como letras tenga la palabra
        slotTextViews = new TextView[length];

        // Ajustar tamaño según la cantidad de letras para que siempre quepan bien en pantalla
        int sizeInDp = length > 7 ? 34 : (length > 5 ? 38 : 44);
        int density = (int) getResources().getDisplayMetrics().density;
        int sizePx = sizeInDp * density;
        int marginPx = (length > 7 ? 2 : 3) * density;

        for (int i = 0; i < length; i++) {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sizePx, sizePx);
            params.setMargins(marginPx, marginPx, marginPx, marginPx);
            textView.setLayoutParams(params);
            textView.setBackgroundResource(R.drawable.bg_letter_slot);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(length > 7 ? 16 : 18);
            textView.setTextColor(ContextCompat.getColor(this, R.color.title_color));
            textView.setTypeface(null, Typeface.BOLD);
            textView.setText("");

            slotTextViews[i] = textView;
            layoutWordSlots.addView(textView);
        }
    }

    private void setupKeyboard() {
        gridKeyboard.removeAllViews();
        int totalKeys = availableLetters.size();
        int columns = totalKeys >= 14 ? 7 : 6;
        gridKeyboard.setColumnCount(columns);

        keyButtons = new Button[totalKeys];

        int sizeInDp = columns >= 7 ? 38 : 42;
        int density = (int) getResources().getDisplayMetrics().density;
        int sizePx = sizeInDp * density;
        int marginPx = 3 * density;

        for (int i = 0; i < totalKeys; i++) {
            final int index = i;
            Button button = new Button(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = sizePx;
            params.height = sizePx;
            params.setMargins(marginPx, marginPx, marginPx, marginPx);
            button.setLayoutParams(params);
            button.setBackgroundResource(R.drawable.bg_key_button);
            button.setText(String.valueOf(availableLetters.get(i)));
            button.setTextSize(16);
            button.setTextColor(ContextCompat.getColor(this, R.color.btn_key_text));
            button.setTypeface(null, Typeface.BOLD);
            button.setPadding(0, 0, 0, 0);

            button.setOnClickListener(v -> onKeyClick(index));

            keyButtons[i] = button;
            gridKeyboard.addView(button);
        }
    }

    private void onKeyClick(int keyIndex) {
        // Buscar la primera casilla libre
        for (TextView slot : slotTextViews) {
            if (slot.getText().toString().isEmpty()) {
                slot.setText(String.valueOf(availableLetters.get(keyIndex)));
                keyButtons[keyIndex].setVisibility(View.INVISIBLE);
                selectedKeyIndices.add(keyIndex);
                break;
            }
        }
    }

    private void deleteLastLetter() {
        if (!selectedKeyIndices.isEmpty()) {
            int lastKeyIndex = selectedKeyIndices.remove(selectedKeyIndices.size() - 1);
            keyButtons[lastKeyIndex].setVisibility(View.VISIBLE);

            // Borrar la última casilla que tenga letra
            for (int i = slotTextViews.length - 1; i >= 0; i--) {
                if (!slotTextViews[i].getText().toString().isEmpty()) {
                    slotTextViews[i].setText("");
                    break;
                }
            }
        }
    }

    private void checkWord() {
        StringBuilder currentGuess = new StringBuilder();
        for (TextView tv : slotTextViews) {
            currentGuess.append(tv.getText().toString());
        }

        if (currentGuess.toString().equalsIgnoreCase(targetWord)) {
            Toast.makeText(this, R.string.correct_word, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.incorrect_word, Toast.LENGTH_SHORT).show();
        }
    }
}