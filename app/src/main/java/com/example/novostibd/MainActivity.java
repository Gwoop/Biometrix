package com.example.novostibd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {


    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    EditText login,password;
    Button avtoriz,registr;
    DataBaseHelperUsers databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor,new BiometricPrompt.AuthenticationCallback(){
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result){
                super.onAuthenticationSucceeded(result);
                Toast.makeText(MainActivity.this, "Succ",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed(){
                super.onAuthenticationFailed();
                Log.e("FailedAUTH","Fail");
            }

        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Авторизация")
                .setNegativeButtonText("cance")
                .build();


        biometricPrompt.authenticate(promptInfo);



        databaseHelper = new DataBaseHelperUsers(this);
        avtoriz = findViewById(R.id.button);
        registr = findViewById(R.id.button2);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        avtoriz.setOnClickListener(view ->{
                    Cursor res = databaseHelper.getdataUsers();
                    if(res.getCount() == 0)
                    {
                        Toast.makeText(getApplicationContext(),"Нет пользователей",Toast.LENGTH_LONG).show();
                        return;
                    }
                    while (res.moveToNext()){
                        //начинается отсчёт с 0
                        String loginIn = login.getText().toString();
                        String loginOut = res.getString(1);
                        String paswIn = password.getText().toString();
                        String pasOut = res.getString(2);
                        if(loginIn.equals(loginOut))
                        {
                            if(paswIn.equals(pasOut))
                            {
                                String role = res.getString(3).toString();
                                if(role.equals("1"))
                                {
                                    Intent intent = new Intent(this,Admin.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(this,User.class);
                                    startActivity(intent);
                                }
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Не верный пароль",Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Такого пользователя нет",Toast.LENGTH_LONG).show();


                    }

        }
        );

        registr.setOnClickListener(view ->{
            Intent back = new Intent(this,Registration.class);
            startActivity(back);
        }
        );
    }




}