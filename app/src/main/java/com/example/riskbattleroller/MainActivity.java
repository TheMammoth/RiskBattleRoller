package com.example.riskbattleroller;

import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView attackerDice1, attackerDice2, attackerDice3;
    private ImageView defenderDice1, defenderDice2;
    private Button attacker1Btn, attacker2Btn, attacker3Btn;
    private Button defender1Btn, defender2Btn;
    private Button battleButton, resetButton, historyButton, closeHistoryButton;
    private TextView resultText, casualtiesText, historyText;
    private LinearLayout attackerSection, defenderSection, historyOverlay;

    private Random random;
    private Handler handler;
    private boolean isBattling = false;

    private SoundPool soundPool;
    private int diceRollSoundId;
    private boolean soundLoaded = false;

    private int attackerDiceCount = 1;
    private int defenderDiceCount = 1;
    private int attackerCasualties = 0;
    private int defenderCasualties = 0;
    private ArrayList<String> battleHistory;
    private int battleNumber = 0;

    private final int ATTACKER_COLOR = Color.parseColor("#C62828");
    private final int ATTACKER_FLASH = Color.parseColor("#EF5350");
    private final int DEFENDER_COLOR = Color.parseColor("#1565C0");
    private final int DEFENDER_FLASH = Color.parseColor("#42A5F5");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        attackerDice1 = findViewById(R.id.attackerDice1);
        attackerDice2 = findViewById(R.id.attackerDice2);
        attackerDice3 = findViewById(R.id.attackerDice3);
        defenderDice1 = findViewById(R.id.defenderDice1);
        defenderDice2 = findViewById(R.id.defenderDice2);

        attacker1Btn = findViewById(R.id.attacker1Btn);
        attacker2Btn = findViewById(R.id.attacker2Btn);
        attacker3Btn = findViewById(R.id.attacker3Btn);
        defender1Btn = findViewById(R.id.defender1Btn);
        defender2Btn = findViewById(R.id.defender2Btn);
        battleButton = findViewById(R.id.battleButton);
        resetButton = findViewById(R.id.resetButton);
        historyButton = findViewById(R.id.historyButton);
        closeHistoryButton = findViewById(R.id.closeHistoryButton);

        resultText = findViewById(R.id.resultText);
        casualtiesText = findViewById(R.id.casualtiesText);
        historyText = findViewById(R.id.historyText);

        attackerSection = findViewById(R.id.attackerSection);
        defenderSection = findViewById(R.id.defenderSection);
        historyOverlay = findViewById(R.id.historyOverlay);

        random = new Random();
        handler = new Handler();
        battleHistory = new ArrayList<>();

        // Initialize SoundPool with modern AudioAttributes API
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();

        // Load the dice roll sound
        diceRollSoundId = soundPool.load(this, R.raw.dice_roll, 1);

        // Set listener to know when sound is loaded
        soundPool.setOnLoadCompleteListener((sp, sampleId, status) -> {
            if (status == 0) {
                soundLoaded = true;
            }
        });

        // Set volume control to media stream
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        attacker1Btn.setOnClickListener(v -> selectAttackerDice(1));
        attacker2Btn.setOnClickListener(v -> selectAttackerDice(2));
        attacker3Btn.setOnClickListener(v -> selectAttackerDice(3));
        defender1Btn.setOnClickListener(v -> selectDefenderDice(1));
        defender2Btn.setOnClickListener(v -> selectDefenderDice(2));

        battleButton.setOnClickListener(v -> {
            if (!isBattling) {
                startBattle();
            }
        });

        resetButton.setOnClickListener(v -> resetCasualties());
        historyButton.setOnClickListener(v -> showHistory());
        closeHistoryButton.setOnClickListener(v -> hideHistory());

        selectAttackerDice(1);
        selectDefenderDice(1);
    }

    private void selectAttackerDice(int count) {
        attackerDiceCount = count;
        updateButtonStates();
    }

    private void selectDefenderDice(int count) {
        defenderDiceCount = count;
        updateButtonStates();
    }

    private void updateButtonStates() {
        updateAttackerButtonState(attacker1Btn, attackerDiceCount == 1);
        updateAttackerButtonState(attacker2Btn, attackerDiceCount == 2);
        updateAttackerButtonState(attacker3Btn, attackerDiceCount == 3);
        updateDefenderButtonState(defender1Btn, defenderDiceCount == 1);
        updateDefenderButtonState(defender2Btn, defenderDiceCount == 2);
    }

    private void updateAttackerButtonState(Button btn, boolean selected) {
        if (selected) {
            btn.setTextSize(28);
            btn.setTextColor(Color.parseColor("#FFFFFF"));
            btn.setAlpha(1.0f);
            btn.setPaintFlags(btn.getPaintFlags() | android.graphics.Paint.FAKE_BOLD_TEXT_FLAG);
        } else {
            btn.setTextSize(16);
            btn.setTextColor(Color.parseColor("#8B0000"));
            btn.setAlpha(0.6f);
            btn.setPaintFlags(btn.getPaintFlags() & ~android.graphics.Paint.FAKE_BOLD_TEXT_FLAG);
        }
    }

    private void updateDefenderButtonState(Button btn, boolean selected) {
        if (selected) {
            btn.setTextSize(28);
            btn.setTextColor(Color.parseColor("#FFFFFF"));
            btn.setAlpha(1.0f);
            btn.setPaintFlags(btn.getPaintFlags() | android.graphics.Paint.FAKE_BOLD_TEXT_FLAG);
        } else {
            btn.setTextSize(16);
            btn.setTextColor(Color.parseColor("#002171"));
            btn.setAlpha(0.6f);
            btn.setPaintFlags(btn.getPaintFlags() & ~android.graphics.Paint.FAKE_BOLD_TEXT_FLAG);
        }
    }

    private void startBattle() {
        isBattling = true;
        battleButton.setEnabled(false);
        resultText.setText("");

        // Play dice roll sound
        playDiceRollSound();

        final int[] finalAttackerValues = new int[attackerDiceCount];
        final int[] finalDefenderValues = new int[defenderDiceCount];

        for (int i = 0; i < attackerDiceCount; i++) {
            finalAttackerValues[i] = random.nextInt(6) + 1;
        }
        for (int i = 0; i < defenderDiceCount; i++) {
            finalDefenderValues[i] = random.nextInt(6) + 1;
        }

        final int animationDuration = 750;
        final int frameDelay = 100;
        final long startTime = System.currentTimeMillis();

        final Runnable animationRunnable = new Runnable() {
            private boolean flashState = false;

            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - startTime;

                if (elapsed < animationDuration) {
                    updateAttackerDice(getRandomDiceValues(attackerDiceCount));
                    updateDefenderDice(getRandomDiceValues(defenderDiceCount));

                    flashState = !flashState;
                    attackerSection.setBackgroundColor(flashState ? ATTACKER_FLASH : ATTACKER_COLOR);
                    defenderSection.setBackgroundColor(flashState ? DEFENDER_FLASH : DEFENDER_COLOR);

                    handler.postDelayed(this, frameDelay);
                } else {
                    updateAttackerDice(finalAttackerValues);
                    updateDefenderDice(finalDefenderValues);
                    attackerSection.setBackgroundColor(ATTACKER_COLOR);
                    defenderSection.setBackgroundColor(DEFENDER_COLOR);
                    calculateBattleResult(finalAttackerValues, finalDefenderValues);
                    isBattling = false;
                    battleButton.setEnabled(true);
                }
            }
        };

        handler.post(animationRunnable);
    }

    private int[] getRandomDiceValues(int count) {
        int[] values = new int[count];
        for (int i = 0; i < count; i++) {
            values[i] = random.nextInt(6) + 1;
        }
        return values;
    }

    private void updateAttackerDice(int[] values) {
        attackerDice1.setVisibility(values.length > 0 ? View.VISIBLE : View.GONE);
        attackerDice2.setVisibility(values.length > 1 ? View.VISIBLE : View.GONE);
        attackerDice3.setVisibility(values.length > 2 ? View.VISIBLE : View.GONE);

        if (values.length > 0) attackerDice1.setImageResource(getDiceImage(values[0]));
        if (values.length > 1) attackerDice2.setImageResource(getDiceImage(values[1]));
        if (values.length > 2) attackerDice3.setImageResource(getDiceImage(values[2]));
    }

    private void updateDefenderDice(int[] values) {
        defenderDice1.setVisibility(values.length > 0 ? View.VISIBLE : View.GONE);
        defenderDice2.setVisibility(values.length > 1 ? View.VISIBLE : View.GONE);

        if (values.length > 0) defenderDice1.setImageResource(getDiceImage(values[0]));
        if (values.length > 1) defenderDice2.setImageResource(getDiceImage(values[1]));
    }

    private void calculateBattleResult(int[] attackerValues, int[] defenderValues) {
        Arrays.sort(attackerValues);
        Arrays.sort(defenderValues);

        int[] attackerSorted = new int[attackerValues.length];
        int[] defenderSorted = new int[defenderValues.length];

        for (int i = 0; i < attackerValues.length; i++) {
            attackerSorted[i] = attackerValues[attackerValues.length - 1 - i];
        }
        for (int i = 0; i < defenderValues.length; i++) {
            defenderSorted[i] = defenderValues[defenderValues.length - 1 - i];
        }

        int attackerLosses = 0;
        int defenderLosses = 0;

        int comparisons = Math.min(attackerSorted.length, defenderSorted.length);

        for (int i = 0; i < comparisons; i++) {
            if (attackerSorted[i] > defenderSorted[i]) {
                defenderLosses++;
            } else {
                attackerLosses++;
            }
        }

        attackerCasualties += attackerLosses;
        defenderCasualties += defenderLosses;

        String result = "";
        if (attackerLosses > defenderLosses) {
            result = "Attacker loses " + attackerLosses + " army!";
        } else if (defenderLosses > attackerLosses) {
            result = "Defender loses " + defenderLosses + " army!";
        } else {
            result = "Both lose " + attackerLosses + " army!";
        }

        resultText.setText(result);
        casualtiesText.setText("Total - Attacker: " + attackerCasualties + " | Defender: " + defenderCasualties);

        battleNumber++;
        String historyEntry = "Battle #" + battleNumber + ": " +
                              "ATK[" + arrayToString(attackerSorted) + "] vs DEF[" + arrayToString(defenderSorted) + "] → " + result;
        battleHistory.add(historyEntry);
    }

    private String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(",");
        }
        return sb.toString();
    }

    private void resetCasualties() {
        attackerCasualties = 0;
        defenderCasualties = 0;
        battleNumber = 0;
        battleHistory.clear();
        casualtiesText.setText("Total - Attacker: 0 | Defender: 0");
        resultText.setText("");
    }

    private void showHistory() {
        if (battleHistory.isEmpty()) {
            historyText.setText("No battles yet. Start rolling!");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = battleHistory.size() - 1; i >= 0; i--) {
                sb.append(battleHistory.get(i)).append("\n\n");
            }
            historyText.setText(sb.toString());
        }
        historyOverlay.setVisibility(View.VISIBLE);
    }

    private void hideHistory() {
        historyOverlay.setVisibility(View.GONE);
    }

    private int getDiceImage(int value) {
        switch (value) {
            case 1: return R.drawable.dice1;
            case 2: return R.drawable.dice2;
            case 3: return R.drawable.dice3;
            case 4: return R.drawable.dice4;
            case 5: return R.drawable.dice5;
            case 6: return R.drawable.dice6;
            default: return R.drawable.dice1;
        }
    }

    private void playDiceRollSound() {
        if (soundPool != null && soundLoaded) {
            // Play sound at full volume: soundID, leftVol, rightVol, priority, loop, rate
            soundPool.play(diceRollSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

        // Release SoundPool resources
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
