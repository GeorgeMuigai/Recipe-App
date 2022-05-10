package com.gdev.recipe.nointernet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.gdev.recipe.MainActivity;
import com.gdev.recipe.R;

public class NoInternet extends AppCompatActivity {

    Button btn_retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        btn_retry = findViewById(R.id.btn_retry);

        btn_retry.setOnClickListener(View ->{
            Intent intent = new Intent(NoInternet.this, MainActivity.class);
            startActivity(intent);
        });
    }
}