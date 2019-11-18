package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class User {

    public String username;
    public String email;

    public User() {

    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}

public class signUpActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private FirebaseDatabase  fdb=FirebaseDatabase.getInstance();
    private DatabaseReference daRef=fdb.getReference();

//    RadioGroup upRg=(RadioGroup) findViewById(R.id.rg1);//상체라디오그룹
//    RadioGroup downRg=(RadioGroup) findViewById(R.id.rg2);//하체라디오그룹


//    RadioButton uprb1= upRg.findViewById(upRg.getCheckedRadioButtonId());
//    RadioButton uprb2= findViewById(R.id.rb2);
//    RadioButton uprb3= findViewById(R.id.rb3);
//    RadioButton downrb1= findViewById(R.id.rb4);
//    RadioButton downrb2= findViewById(R.id.rb5);
//    RadioButton downrb3= findViewById(R.id.rb6);
//






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signupButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){

            switch(v.getId()){

                case R.id.signupButton:
                    writeNewUser("1234","NEW","newnewenw@naver.com");
                    signUp();
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

                                // ...
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
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        daRef.child("users").child(userId).setValue(user);
    }

}
