package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 현재 로그인 되어있는지 확인 로그인 되어있지 않으면 로그인 화면으로 넘어가도록
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startMyActivity(LoginActivity.class);
        }

        findViewById(R.id.Logoutbutton).setOnClickListener(onClickListener);
        findViewById(R.id.Optionbutton).setOnClickListener(onClickListener);
        findViewById(R.id.Resetbutton).setOnClickListener(onClickListener);


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.Logoutbutton:
                    FirebaseAuth.getInstance().signOut();
                    startMyActivity(LoginActivity.class);
                    break;
                case R.id.Optionbutton:
                    startMyActivity(OptionActivity.class);
                    break;
                case R.id.Resetbutton:
                    setImage();
                    break;
            }
        }
    };

    private void setImage(){
        // 사진 초기화

        ArrayList<StorageReference> RefList = new ArrayList<StorageReference>();

        RefList.add(storageRef.child("10012.png"));
        RefList.add(storageRef.child("10001.bmp"));
        RefList.add(storageRef.child("10007.bmp"));
        RefList.add(storageRef.child("10006.bmp"));
        RefList.add(storageRef.child("KakaoTalk_20191107_110103496.jpg"));

        final long ONE_MEGABYTE = 1024 * 1024;

        RefList.get(1).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // 이미지 다운로드 성공
                ImageButton image1 = findViewById(R.id.firstImage);
                image1.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                startToast("추천 스타일 입니다.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // 다운로드 실패
                startToast("fail...uu");
            }
        });

    }

    private void startMyActivity(Class c){
        Intent intent=new Intent(this,c);
        startActivity(intent);
    }

    public void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

}
