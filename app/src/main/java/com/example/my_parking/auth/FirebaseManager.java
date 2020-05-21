package com.example.my_parking.auth;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.my_parking.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseManager {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user;
    private static FirebaseManager instance = new FirebaseManager();

    public static FirebaseManager getInstance() {
        return instance;
    }


    public void signIn(String email, String password, final SignInActivity activity) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = firebaseAuth.getCurrentUser();
                            activity.startMainActivity();
                            System.out.println("Sign in success! " + task.getResult().getUser().getEmail());
                        } else {
                            user = null;
                            System.out.println("Sign in failed! " + task.getException());
                        }
                    }
                });
    }

    public void signUp(String email, String password, final Context context){

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Sign up success! " + task.getResult().getUser().getEmail());
                        } else {
                            System.out.println("Sign up failed! " + task.getException());
                            Toast.makeText(context, "Sign up failed! The email is already in use",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signOut(){
        user = null;
        firebaseAuth.signOut();
    }
}
