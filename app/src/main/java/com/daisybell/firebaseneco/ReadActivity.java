package com.daisybell.firebaseneco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> mListData; // ListView для передачи одного значения стринг(Имени)
    private List<User> mListTemp; // ListView для передачи всех переменных класса User
    private DatabaseReference mDataBase;
    private String USER_KEY = "User";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        init();
        getDataFromDB();
        setOnClickItem();
    }
    private void init() {
        mListView = findViewById(R.id.listView);
        mListData = new ArrayList<>();
        mListTemp = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mListData);
        mListView.setAdapter(mAdapter);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }
    // Метод для получения данных из firebase и отображения в ListView
    private void getDataFromDB() {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(mListData.size() > 0)mListData.clear(); // Проверяем если размер больше 0, то отчищаем
                if(mListTemp.size() > 0)mListTemp.clear(); // Проверяем если размер больше 0, то отчищаем
                for (DataSnapshot ds : dataSnapshot.getChildren()) { // Перебираем все данные с firebase
                    User user = ds.getValue(User.class);
                    assert user != null;
                    mListData.add(user.name); // Сохраняем полученые данные в ListView (Имени)
                    mListTemp.add(user); // Сохраняем полученые данные в ListView (всех переменных User)
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDataBase.addValueEventListener(vListener);
    }
    // Узнаем по position на какой элемент ListView нажал пользователь и переходи на другое активити
    private void setOnClickItem() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = mListTemp.get(position);
                Intent intent = new Intent(ReadActivity.this, ShowActivity.class);
                intent.putExtra(Constant.USER_NAME, user.name); // сохраняем Имя для отображения в другом активити
                intent.putExtra(Constant.USER_SURNAME, user.surname); // сохраняем Фамилию для отображения в другом активити
                intent.putExtra(Constant.USER_EMAIL, user.email); // сохраняем Почту для отображения в другом активити
                startActivity(intent);
            }
        });
    }
}
