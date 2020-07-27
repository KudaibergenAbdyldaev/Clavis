package com.example.clavis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextMobile;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextMobile = findViewById(R.id.edt_register);
        mAuth= FirebaseAuth.getInstance();

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();
                String code = "+996";

                if(mobile.isEmpty() || mobile.length() < 9){
                    editTextMobile.setError("Заполните поля!");
                    editTextMobile.requestFocus();
                    return;
                }
                String phoneNumber = code + mobile;
                Intent intent = new Intent(RegisterActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile", phoneNumber);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
