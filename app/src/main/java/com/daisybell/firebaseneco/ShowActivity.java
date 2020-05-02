package com.daisybell.firebaseneco;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity {

    private TextView tvName, tvSurname, tvEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        init();
        getIntentMain();
    }
    private void init() {
        tvName = findViewById(R.id.tvName);
        tvSurname = findViewById(R.id.tvSurname);
        tvEmail = findViewById(R.id.tvEmail);
    }
    // Метод получения данных, которые передавали в прошлой активити
    private void getIntentMain() {
        Intent intent = getIntent();
        if (intent != null) { // Если Intent не нулевой, то присваеваем переменным данные
            tvName.setText(intent.getStringExtra(Constant.USER_NAME));
            tvSurname.setText(intent.getStringExtra(Constant.USER_SURNAME));
            tvEmail.setText(intent.getStringExtra(Constant.USER_EMAIL));
        }
    }
}
