package com.example.memorypract.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ObjetosViewModel extends ViewModel {

    private final MutableLiveData<String> _targetWord = new MutableLiveData<>("ARBOL");
    public LiveData<String> getTargetWord() { return _targetWord; }

    private final MutableLiveData<List<Character>> _availableLetters = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<Character>> getAvailableLetters() { return _availableLetters; }

    private final MutableLiveData<List<String>> _slotLetters = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<String>> getSlotLetters() { return _slotLetters; }

    private final MutableLiveData<List<Integer>> _selectedKeyIndices = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<Integer>> getSelectedKeyIndices() { return _selectedKeyIndices; }

    private final MutableLiveData<Boolean> _checkResultEvent = new MutableLiveData<>();
    public LiveData<Boolean> getCheckResultEvent() { return _checkResultEvent; }

    public void loadWord(String word) {
        if (word == null || word.trim().isEmpty()) return;

        String formattedWord = word.trim().toUpperCase();

        // If word is already set and letters generated, do not re-randomize on configuration changes
        if (formattedWord.equals(_targetWord.getValue()) && _availableLetters.getValue() != null && !_availableLetters.getValue().isEmpty()) {
            return;
        }

        _targetWord.setValue(formattedWord);

        List<Integer> selectedIndices = new ArrayList<>();
        _selectedKeyIndices.setValue(selectedIndices);

        List<Character> letters = new ArrayList<>();
        for (char c : formattedWord.toCharArray()) {
            letters.add(c);
        }

        int totalTargetKeys = formattedWord.length() > 6 ? 14 : 12;
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        while (letters.size() < totalTargetKeys) {
            char randomChar = alphabet.charAt(random.nextInt(alphabet.length()));
            letters.add(randomChar);
        }
        Collections.shuffle(letters);
        _availableLetters.setValue(letters);

        List<String> slots = new ArrayList<>();
        for (int i = 0; i < formattedWord.length(); i++) {
            slots.add("");
        }
        _slotLetters.setValue(slots);
    }

    public void onKeyClick(int keyIndex) {
        List<String> slots = _slotLetters.getValue();
        List<Character> letters = _availableLetters.getValue();
        List<Integer> selectedIndices = _selectedKeyIndices.getValue();

        if (slots == null || letters == null || selectedIndices == null) return;
        if (keyIndex < 0 || keyIndex >= letters.size()) return;
        if (selectedIndices.contains(keyIndex)) return;

        List<String> newSlots = new ArrayList<>(slots);
        List<Integer> newSelectedIndices = new ArrayList<>(selectedIndices);

        for (int i = 0; i < newSlots.size(); i++) {
            if (newSlots.get(i).isEmpty()) {
                newSlots.set(i, String.valueOf(letters.get(keyIndex)));
                newSelectedIndices.add(keyIndex);
                break;
            }
        }

        _slotLetters.setValue(newSlots);
        _selectedKeyIndices.setValue(newSelectedIndices);
    }

    public void deleteLastLetter() {
        List<String> slots = _slotLetters.getValue();
        List<Integer> selectedIndices = _selectedKeyIndices.getValue();

        if (slots == null || selectedIndices == null || selectedIndices.isEmpty()) return;

        List<Integer> newSelectedIndices = new ArrayList<>(selectedIndices);
        int lastKeyIndex = newSelectedIndices.remove(newSelectedIndices.size() - 1);

        List<String> newSlots = new ArrayList<>(slots);
        for (int i = newSlots.size() - 1; i >= 0; i--) {
            if (!newSlots.get(i).isEmpty()) {
                newSlots.set(i, "");
                break;
            }
        }

        _selectedKeyIndices.setValue(newSelectedIndices);
        _slotLetters.setValue(newSlots);
    }

    public void checkWord() {
        List<String> slots = _slotLetters.getValue();
        String target = _targetWord.getValue();

        if (slots == null || target == null) return;

        StringBuilder currentGuess = new StringBuilder();
        for (String letter : slots) {
            currentGuess.append(letter);
        }

        boolean isCorrect = currentGuess.toString().equalsIgnoreCase(target);
        _checkResultEvent.setValue(isCorrect);
    }

    public void clearCheckResultEvent() {
        _checkResultEvent.setValue(null);
    }
}
