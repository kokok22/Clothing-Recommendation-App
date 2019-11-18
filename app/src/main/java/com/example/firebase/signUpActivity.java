package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signUpActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private RadioGroup upper_group;
    private  RadioGroup down_group;
    private  RadioGroup color_group;

    private RadioButton u_rb1, u_rb2, u_rb3, d_rb1, d_rb2, d_rb3, c_rb1, c_rb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        upper_group = findViewById(R.id.rg1);
        down_group = findViewById(R.id.rg2);
        color_group = findViewById(R.id.rg3);

        u_rb1 = findViewById(R.id.rb1);
        u_rb2 = findViewById(R.id.rb2);
        u_rb3 = findViewById(R.id.rb3);
        d_rb1 = findViewById(R.id.rb4);
        d_rb2 = findViewById(R.id.rb5);
        d_rb3 = findViewById(R.id.rb6);
        c_rb1 = findViewById(R.id.rb7);
        c_rb2 = findViewById(R.id.rb8);

        findViewById(R.id.signupButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.signupButton:
                    signUp();
                    setOption();
                    break;
            }
        }
    };

    private void signUp(){
        String email = ((EditText)findViewById(R.id.EmaileditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordeditText)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.passwordcheckeditText)).getText().toString();

        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() >0){
            if(password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startToast("회원가입을 성공했습니다.");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    startLoginActivity();
                                    // 성공 UI
                                } else {
                                    if(task.getException()!=null)
                                        startToast("기존에 가입된 email입니다.");
                                    // 실패 UI
                                }
                            }
                        });
            }

            else{
                startToast("비밀번호가 일치하지 않습니다.");
            }
        }

        else{
            startToast("email 또는 비밀번호를 입력해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private void startLoginActivity(){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    private void setOption(){
        int upper = 0;
        int down = 0;
        int color = 0;

    }

}
