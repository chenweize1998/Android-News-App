package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.newstoday.R;
import com.example.newstoday.User;
import com.example.newstoday.UserManager;
import com.example.newstoday.UserManagerOnServer;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {
    private UserManagerOnServer userManagerOnServer;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userManagerOnServer = UserManagerOnServer.getUserManagerOnServer(getApplicationContext());
        userManager = UserManager.getUserManager(getApplicationContext());

        Button materialRippleLayoutSignUp = findViewById(R.id.sign_btn_signup);

        Button materialRippleLayoutSignIn = findViewById(R.id.sign_btn_signin);


        materialRippleLayoutSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText inputEmail = findViewById(R.id.sign_email);
                TextInputEditText inputPasswd = findViewById(R.id.sign_passwd);
                TextInputEditText inputName= findViewById(R.id.sign_name);
                String email = inputEmail.getText().toString();
                String passwd = inputPasswd.getText().toString();
                String name = inputName.getText().toString();
                if(userManagerOnServer.userSignUp(email, name, passwd)){
                    Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
//                    User user = new User(email, name, passwd, null, "");
//                    user.setOriAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.header));
//                    user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.header));
//                    userManager.addInUser(user);
                    Intent intent = getIntent();
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "注册失败，邮箱已存在", Toast.LENGTH_SHORT).show();
                }
            }
        });

        materialRippleLayoutSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText inputEmail = findViewById(R.id.sign_email);
                TextInputEditText inputPasswd = findViewById(R.id.sign_passwd);
                TextInputEditText inputName= findViewById(R.id.sign_name);
                String email = inputEmail.getText().toString();
                String passwd = inputPasswd.getText().toString();
                String name = inputName.getText().toString();
                if(userManagerOnServer.userSignIn(email, name, passwd)){
//                    Toast.makeText(getApplicationContext(), "登陆成功！", Toast.LENGTH_SHORT);
                    Intent intent = getIntent();
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
