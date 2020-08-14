package com.example.nonmoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class addLog extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private int confirm=0;
    private EditText name, phonenumber, id, pw, pw2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);

        Button addlog = (Button)findViewById(R.id.addLog);
        name = (EditText)findViewById(R.id.Signup_Name);
        phonenumber = (EditText)findViewById(R.id.Signup_PhoneNumber);
        id = (EditText)findViewById(R.id.Signup_Id);
        pw = (EditText)findViewById(R.id.Signup_Pw);
        pw2 = (EditText)findViewById(R.id.Signup_Pw2);

        addlog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        confirm = 0;
        if(id.getText().toString().equals("")||pw.getText().toString().equals("")||pw2.getText().toString().equals("")||phonenumber.getText().toString().equals("")||name.getText().toString().equals(""))
            confirm = 1;
        if(!pw.getText().toString().equals(pw2.getText().toString()))
            confirm = 2;
        if(pw.getText().toString().length()<=5)
            confirm = 3;
        if(confirm==0) {
            mAuth.createUserWithEmailAndPassword(id.getText().toString()+"@hs.ac.kr", pw.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Map<String, Object> userMap = new HashMap<>();
                                if (user != null) {
                                    userMap.put(FirebaseID.documentId, user.getUid());
                                    userMap.put(FirebaseID.nickname, name.getText().toString());
                                    userMap.put(FirebaseID.email, id.getText().toString());
                                    userMap.put(FirebaseID.password, pw.getText().toString());
                                    userMap.put(FirebaseID.mobileNo, phonenumber.getText().toString());
                                    mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge());
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "동일 ID가 존재합니다.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else if(confirm == 1){
            Toast.makeText(getApplicationContext(), "입력 하지 않은 부분이 존재 합니다.",
                    Toast.LENGTH_SHORT).show();
        }
        else if(confirm == 2){
            Toast.makeText(getApplicationContext(), "패스워드 두개가 다릅니다.",
                    Toast.LENGTH_SHORT).show();
        }
        else if(confirm == 3){
            Toast.makeText(getApplicationContext(), "패스워드를 6자 이상으로 입력해주세요.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
