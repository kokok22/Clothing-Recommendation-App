package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class OptionActivity extends BaseActivity{
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.signupButton:
                    Option();
                    break;
            }
        }
    };

    private void Option(){

    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private void startMyActivity(Class c){
        Intent intent=new Intent(this,c);
        startActivity(intent);
    }
}
