package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

/*
* 시도해 볼 방법 => 이메일을 전역변수 option 페이지로 받아온다
* option 페이지에서 직접 업데이트
*
* */

class UserOption{
    public int UpBody;
    public int downBody;
    public int skinColor;

    public UserOption(int upBody,int downBody,int skinColor) {
        this.UpBody = upBody;
        this.downBody = downBody;
        this.skinColor= skinColor;
    }
}

public class OptionActivity extends BaseActivity{
    static int state=0;
    private FirebaseAuth mAuth;
    private RadioGroup bodyG;
    private RadioGroup legG;
    private RadioGroup skinCG;
    private RadioGroup cloth1;
    private RadioGroup cloth2;

    private RadioButton body1,body2,body3,leg1,leg2,leg3,sc1,sc2;
    private RadioButton black,blue,brown,gray;
    private RadioButton pink,Green,Maroon,Mustard;
    signUpActivity SUA=new signUpActivity();

    int bodyNum=10,legNum=10, skinC=10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        bodyG=findViewById(R.id.org1);
        legG=findViewById(R.id.org2);
        skinCG=findViewById(R.id.org3);
        cloth1=findViewById(R.id.org4);
        cloth2=findViewById(R.id.org5);
        Button applyB=(Button) findViewById(R.id.applyButton);

        body1=findViewById(R.id.orb1);
        body2=findViewById(R.id.orb2);
        body3=findViewById(R.id.orb3);

        leg1=findViewById(R.id.leg1);
        leg2=findViewById(R.id.leg2);
        leg3=findViewById(R.id.leg3);

        sc1=findViewById(R.id.skinC1);
        sc2=findViewById(R.id.skinC2);

        black=findViewById(R.id.black);
        blue=findViewById(R.id.blue);
        brown=findViewById(R.id.brown);
        gray=findViewById(R.id.gray);
        pink=findViewById(R.id.pink);
        Green=findViewById(R.id.green);
        Maroon=findViewById(R.id.maroon);
        Mustard=findViewById(R.id.mustard);



                bodyG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        Map<String, Object> userUpdates = new HashMap<>();
                        if(body1.isChecked()){
                            bodyNum=1;
                           // userUpdates.put("gfrt12193@naver", new UserOption(4,5,5));
                        }else if(body2.isChecked()){
                            bodyNum=2;
//                            userUpdates.put("gfrt12193@naver", new UserOption(4,5,5));
                        }else if(body3.isChecked()){
                            bodyNum=3;
//                            userUpdates.put("gfrt12193@naver", new UserOption(4,5,5));

                        }
                        //   SUA.daRef.updateChildren(userUpdates);
                       // Toast.makeText(getApplicationContext(),"테스트",Toast.LENGTH_LONG);
                    }
                });
                legG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(leg1.isChecked()){
                            legNum=1;
                        }else if(leg2.isChecked()){
                            legNum=2;
                        }else if(leg3.isChecked()){
                            legNum=3;

                        }
                    }
                });

                skinCG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(sc1.isChecked()){
                            skinC=1;
                        }else if(sc2.isChecked()){
                            skinC=2;
                        }
                    }
                });

                cloth1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(black.isChecked()){
                            MainActivity ma=new MainActivity();
                            //ma.setColorBBB(1);
                            System.out.println(ma.getColorBBB());
                        }else if(blue.isChecked()){
                            MainActivity ma=new MainActivity();
                            ma.setColorBBB(2);
                        }else if(brown.isChecked()){
                            MainActivity ma=new MainActivity();
                            ma.setColorBBB(3);
                        }else if(gray.isChecked()){
                            MainActivity ma=new MainActivity();
                            ma.setColorBBB(4);
                        }
                    }
                });
        cloth2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(pink.isChecked()){
                    MainActivity ma=new MainActivity();
                    ma.setColorBBB(5);
                    //System.out.println(ma.getColorBBB());
                }else if(Green.isChecked()){
                    MainActivity ma=new MainActivity();
                    ma.setColorBBB(6);
                }else if(Maroon.isChecked()){
                    MainActivity ma=new MainActivity();
                    ma.setColorBBB(7);
                }else if(Mustard.isChecked()){
                    MainActivity ma=new MainActivity();
                    ma.setColorBBB(8);
                }
            }
        });


         //   SUA.signUp();

        applyB.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     System.out.println("test");
                Map<String, Object> userUpdates = new HashMap<>();
       //         Toast.makeText(getApplicationContext(),"테스트",Toast.LENGTH_LONG).show();
                userUpdates.put("gfrt12193@naver", new UserOption(bodyNum,legNum,skinC));
                SUA.daRef.updateChildren(userUpdates);
            }
        });


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
