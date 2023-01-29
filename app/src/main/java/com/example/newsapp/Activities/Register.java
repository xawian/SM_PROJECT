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

public class Register extends AppCompatActivity {

    EditText userId, password,password_confirm, name;
    Button register;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userId=findViewById(R.id.userId);
        password=findViewById(R.id.password);
        password_confirm=findViewById(R.id.password_confirm);
        name=findViewById(R.id.name);
        register=findViewById(R.id.register);
        login=findViewById(R.id.login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating user entity
                UserEntity userEntity = new UserEntity();
                userEntity.setUserId(userId.getText().toString());
                userEntity.setPassword(password.getText().toString());
                userEntity.setPassword_confirm(password_confirm.getText().toString());
                userEntity.setName(name.getText().toString());
                if(validateInput(userEntity)){
                    //do insert operation
                    UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                    final UserDao userDao = userDatabase.userDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //Register user
                            userDao.registerUser(userEntity);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),getString(R.string.registered),Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this, Login.class);
                                    startActivity(intent);

                                }
                            });
                        }
                    }).start();
                }else{
                    Toast.makeText(getApplicationContext(),getString(R.string.input), Toast.LENGTH_SHORT).show();
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( Register.this, Login.class));
            }
        });


    }
    private Boolean validateInput(UserEntity userEntity){
        if(userEntity.getName().isEmpty() ||
                userEntity.getPassword().isEmpty() ||
                userEntity.getPassword_confirm().isEmpty() ||
                userEntity.getName().isEmpty()){
            return false;
        } else if( userEntity.getPassword().equals(userEntity.getPassword_confirm()) )
            return true;
        else{
            return false;
        }

    }
}