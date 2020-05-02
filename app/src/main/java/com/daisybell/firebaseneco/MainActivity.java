package com.daisybell.firebaseneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText edName, edSurname, edEmail;
    private DatabaseReference mDataBase;
    private String USER_KEY = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }
    private void init() {
        edName = findViewById(R.id.edName);
        edSurname = findViewById(R.id.edSurname);
        edEmail = findViewById(R.id.edEmail);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }
    // Обработчик кнопки "Сохранить" (Сохраняет введеные пользователем данные на почту)
    public void onClickSave(View view) {
        String id = mDataBase.getKey(); // Берем ключ из firebase
        String name = edName.getText().toString();
        String surname = edSurname.getText().toString();
        String email = edEmail.getText().toString();
        User newUser = new User(id, name, surname, email); // Вызываем конструктор с класса User и сохраняем в нем данные
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !TextUtils.isEmpty(email)) {
            mDataBase.push().setValue(newUser); // отправка веденых пользователем данных в firebase
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Пустое поле", Toast.LENGTH_SHORT).show();
        }

    }
    //Обработчик кнопки "Read". Переход на другое активити
    public void onClickRead(View view) {
        Intent intent = new Intent(MainActivity.this, ReadActivity.class);
        startActivity(intent);
    }
}
