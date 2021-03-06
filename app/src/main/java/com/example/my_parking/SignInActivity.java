package com.example.my_parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_parking.auth.FirebaseManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignInActivity extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;
    private TextView textViewInfo;
    private FirebaseManager firebaseManager = new FirebaseManager();
    private Verify verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        textViewInfo = findViewById(R.id.textViewInfo);


        // Get key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.my_parking",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {
        }

        firebaseManager = new FirebaseManager();

        verify = new Verify();
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void signIn(View view) {

        if (verify.isOK()) {
            firebaseManager.signIn(verify.email, verify.password, this);
        } else {
            System.out.println("==> Error here");
        }
    }

    public void signInUnsuccessful() {
        // Should only happen if sign in was unsuccessful
        passwordText.setText("");
        textViewInfo.setVisibility(View.VISIBLE);
    }

    public void signUp(View view) {
        if (verify.isOK()) {
            firebaseManager.signUp(verify.email, verify.password, this);
        } else {
            Toast.makeText(this, "You did not enter a valid email or password",
                    Toast.LENGTH_LONG).show();
        }
    }

    private class Verify {
        String email;
        String password;

        boolean isOK() {
            email = emailText.getText().toString();
            password = passwordText.getText().toString();
            return email.length() > 0 && password.length() > 0;
        }
    }
}