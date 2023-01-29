package com.example.newsapp.Activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.Room.UserDao;
import com.example.newsapp.Room.UserDatabase;
import com.example.newsapp.Room.UserEntity;

public class Login extends AppCompatActivity {

    EditText userId, password;
    Button login;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userId = findViewById(R.id.userId);
        password = findViewById(R.id.password);
        register=findViewById(R.id.register_btn);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userIdText = userId.getText().toString();
                String passwordText = password.getText().toString();
                if(userIdText.isEmpty()|| passwordText.isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.input), Toast.LENGTH_SHORT).show();
                }else{
                    //Perform QUERY
                    UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                    UserDao userDao = userDatabase.userDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserEntity userEntity = userDao.login(userIdText,passwordText);
                            if(userEntity==null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),getString(R.string.credentials),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                //TUTAJ WYWOLAJ WIDOK KTORY MA SIE OTWORZYC PO ZALOGOWANIU
                                String name = userEntity.getName();
                                startActivity(new Intent(Login.this, MainActivity.class)
                                        .putExtra("name",name));
                            }
                        }
                    }).start();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}