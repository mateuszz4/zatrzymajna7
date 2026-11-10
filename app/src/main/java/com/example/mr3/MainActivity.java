package com.example.mr3;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvCounter, tvHits, tvMessage;
    private Button btnStart, btnStop;

    private int currentDigit = 0;
    private int hitsCount = 0;
    private boolean isRunning = false;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    private int currentIntervalMs = 500;
    private static final int MIN_INTERVAL_MS = 200;
    private static final int INTERVAL_STEP_MS = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvCounter = findViewById(R.id.tvCounter);
        tvHits = findViewById(R.id.tvHits);
        tvMessage = findViewById(R.id.tvMessage);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);

        runnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    currentDigit = (currentDigit + 1) % 10;
                    tvCounter.setText(String.valueOf(currentDigit));
                    handler.postDelayed(this, currentIntervalMs);
                }
            }
        };

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGame();
            }
        });
    }

    private void startGame() {
        isRunning = true;
        currentDigit = 0;
        tvCounter.setText(String.valueOf(currentDigit));
        tvMessage.setText("");

        btnStart.setEnabled(false);
        btnStop.setEnabled(true);

        handler.postDelayed(runnable, currentIntervalMs);
    }

    private void stopGame() {
        isRunning = false;

        handler.removeCallbacks(runnable);

        btnStart.setEnabled(true);
        btnStop.setEnabled(false);

        if (currentDigit == 7) {
            hitsCount++;
            tvHits.setText("Trafienia: " + hitsCount);
            tvMessage.setText("Trafienie!");

            if (currentIntervalMs > MIN_INTERVAL_MS) {
                currentIntervalMs = Math.max(MIN_INTERVAL_MS, currentIntervalMs - INTERVAL_STEP_MS);
            }
        } else {
            tvMessage.setText("Pudło! Zatrzymano na " + currentDigit);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isRunning) {
            stopGame();
        }
    }
}