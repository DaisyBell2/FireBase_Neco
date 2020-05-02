package com.daisybell.firebaseneco;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText edLogin, edPassword;
    private FirebaseAuth mAuth;
    private Button mBtStart, mBtSingUp, mBtSingIn, mBtSingOut;
    private TextView mTvUserEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override // Проверяет авторизирован ли пользователь и если да, сразу заходит
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            showSigned();
//            String userName = "Вы вошли как: " + currentUser.getEmail();
//            mTvUserEmail.setText(userName);

            Toast.makeText(this, "User not null " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        } else {
            notSigned();

            Toast.makeText(this, "User null", Toast.LENGTH_SHORT).show();
        }
    }
        // Инициализация необходимых переменных
    private void init() {
        edLogin = findViewById(R.id.edLogin);
        edPassword = findViewById(R.id.edPassword);
        mAuth = FirebaseAuth.getInstance();
        mTvUserEmail = findViewById(R.id.tvUserEmail);
        mBtStart = findViewById(R.id.btStart);
        mBtSingUp = findViewById(R.id.btSingUp);
        mBtSingIn = findViewById(R.id.btSingIn);
        mBtSingOut = findViewById(R.id.btSignOut);
    }

    // Обработчик кнопки "Зарегистрироваться"
    public void onClickSignUp(View view) {
        String login = edLogin.getText().toString();
        String password = edPassword.getText().toString();

        if (!TextUtils.isEmpty(login) && !TextUtils.isEmpty(password)) { // Проверяет пусты ли поля
            mAuth.createUserWithEmailAndPassword(login, password) // создает аккаунт
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) { // Проверяет, все ли успешно
                                showSigned();
                                sendEmailVer();
                                Toast.makeText(getApplicationContext(), "User SingUp Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                notSigned();
                                Toast.makeText(getApplicationContext(), "User SingUp failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Please enter Email and Password", Toast.LENGTH_SHORT).show();
        }
    }

    // Обработчик кнопки "Войти"
    public void onClickSignIn(View view) {
        String login = edLogin.getText().toString();
        String password = edPassword.getText().toString();

        if (!TextUtils.isEmpty(login) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(login, password) // Заходит в акаунт(если он есть)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) { // Проверяет, все ли успешно
                                showSigned();
                                Toast.makeText(getApplicationContext(), "User SingIn Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                notSigned();
                                Toast.makeText(getApplicationContext(), "User SingIn failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Please enter Email and Password", Toast.LENGTH_SHORT).show();
        }
    }
    // Обработчик кнопки начать
    public void onClickStart(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
    // Обработчик кнопки "Выйти"
    public void onClickSignOut(View view) {
        FirebaseAuth.getInstance().signOut(); // Выхот из аккаунта
        notSigned();
    }
    // Метод который отображается если если пользователь вошел и все хорошо
    private void showSigned() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()) {

            String userName = "Вы вошли как: " + user.getEmail();
            mTvUserEmail.setText(userName);

            mBtStart.setVisibility(View.VISIBLE);
            mTvUserEmail.setVisibility(View.VISIBLE);
            mBtSingOut.setVisibility(View.VISIBLE);
            edLogin.setVisibility(View.GONE);
            edPassword.setVisibility(View.GONE);
            mBtSingUp.setVisibility(View.GONE);
            mBtSingIn.setVisibility(View.GONE);
        } else {
            Toast.makeText(getApplicationContext(), "Проверьте вашу почту для подтверждения Email адреса", Toast.LENGTH_SHORT).show();
        }
    }
    // Метод, когда пользователь не вошел
    private void notSigned() {
        mBtStart.setVisibility(View.GONE);
        mTvUserEmail.setVisibility(View.GONE);
        mBtSingOut.setVisibility(View.GONE);
        edLogin.setVisibility(View.VISIBLE);
        edPassword.setVisibility(View.VISIBLE);
        mBtSingUp.setVisibility(View.VISIBLE);
        mBtSingIn.setVisibility(View.VISIBLE);
    }
    // Метод для отправки письма на указаную почту(Верефикация почты)
    private void sendEmailVer() {
        FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Проверьте вашу почту для подтверждения Email адреса", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Send email failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
