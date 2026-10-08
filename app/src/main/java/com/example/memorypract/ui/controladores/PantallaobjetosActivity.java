package com.example.memorypract.ui.controladores;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.memorypract.R;
import com.example.memorypract.data.modelos.AnimalItem;
import com.example.memorypract.ui.viewmodels.ObjetosViewModel;

import java.util.ArrayList;
import java.util.List;

public class PantallaobjetosActivity extends AppCompatActivity {

    private ObjetosViewModel viewModel;

    private TextView[] slotTextViews;
    private Button[] keyButtons;

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

        viewModel = new ViewModelProvider(this).get(ObjetosViewModel.class);

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(PantallaobjetosActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

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

        /*List<AnimalItem> lista = new ArrayList<>();
        lista.add(new AnimalItem(getString(R.string.animal_perro), R.drawable.perro));
        lista.add(new AnimalItem(getString(R.string.animal_gato), R.drawable.gato));
        lista.add(new AnimalItem(getString(R.string.animal_conejo), R.drawable.conejo));
        lista.add(new AnimalItem(getString(R.string.animal_elefante), R.drawable.elefante));
        lista.add(new AnimalItem(getString(R.string.animal_leon), R.drawable.leon));
        lista.add(new AnimalItem(getString(R.string.animal_jirafa), R.drawable.jirafa));*/
        String initialWord = "ARBOL";
        if (getIntent() != null && getIntent().hasExtra("WORD")) {
            String wordFromIntent = getIntent().getStringExtra("WORD");
            if (wordFromIntent != null && !wordFromIntent.trim().isEmpty()) {
                initialWord = wordFromIntent.trim().toUpperCase();
            }
        }

        viewModel.loadWord(initialWord);

        viewModel.getAvailableLetters().observe(this, letters -> {
            if (letters != null && !letters.isEmpty()) {
                setupKeyboard(letters);
            }
        });

        viewModel.getSlotLetters().observe(this, slots -> {
            if (slots != null) {
                updateSlotViews(slots);
            }
        });

        viewModel.getSelectedKeyIndices().observe(this, selectedIndices -> {
            if (keyButtons == null || selectedIndices == null) return;
            for (int i = 0; i < keyButtons.length; i++) {
                if (keyButtons[i] != null) {
                    keyButtons[i].setVisibility(selectedIndices.contains(i) ? View.INVISIBLE : View.VISIBLE);
                }
            }
        });

        viewModel.getCheckResultEvent().observe(this, isCorrect -> {
            if (isCorrect == null) return;
            if (isCorrect) {
                Toast.makeText(this, R.string.correct_word, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.incorrect_word, Toast.LENGTH_SHORT).show();
            }
            viewModel.clearCheckResultEvent();
        });

        Button btnDelete = findViewById(R.id.btnDelete);
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> viewModel.deleteLastLetter());
        }

        Button btnCheck = findViewById(R.id.btnCheck);
        if (btnCheck != null) {
            btnCheck.setOnClickListener(v -> viewModel.checkWord());
        }
    }

    private void updateSlotViews(List<String> slots) {
        if (slotTextViews == null || slotTextViews.length != slots.size()) {
            setupWordSlots(slots.size());
        }
        for (int i = 0; i < slots.size(); i++) {
            if (slotTextViews[i] != null) {
                slotTextViews[i].setText(slots.get(i));
            }
        }
    }

    private void setupWordSlots(int length) {
        layoutWordSlots.removeAllViews();
        slotTextViews = new TextView[length];

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

    private void setupKeyboard(List<Character> availableLetters) {
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

            button.setOnClickListener(v -> viewModel.onKeyClick(index));

            keyButtons[i] = button;
            gridKeyboard.addView(button);
        }
    }
}
