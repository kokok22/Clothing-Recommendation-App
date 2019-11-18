package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.Loginbutton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoSignUpbutton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoPasswordbutton).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.Loginbutton:
                    login();
                    break;
                case R.id.gotoSignUpbutton:
                    startmyActivity(signUpActivity.class);
                    break;
                case R.id.gotoPasswordbutton:
                    startmyActivity(PasswordResetActivity.class);
                    break;
            }
        }
    };

    private void login(){
        String email = ((EditText)findViewById(R.id.EmaileditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordeditText)).getText().toString();

        if(email.length() > 0 && password.length() > 0){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                startToast("로그인에 성공했습니다.");
                                startmyActivity(MainActivity.class);
                            } else {
                                if(task.getException()!=null)
                                    startToast("email 또는 비밀번호를 확인해주세요.");
                            }

                            // ...
                        }
                    });

        }
        else{
            startToast("email 또는 비밀번호를 입력해주세요.");
        }
    }

    public void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private void startmyActivity(Class c){
        Intent intent=new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
