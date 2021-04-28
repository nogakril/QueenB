package queenb.app.y2021.queenb;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import queenb.app.y2021.queenb.MainScreenActivity;
import queenb.app.y2021.queenb.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    EditText usernameEditText, passwordEditText;
    Button loginButton;
    ProgressBar loadingProgressBar;
    FirebaseAuth fAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
            finish();
        }

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password =  passwordEditText.getText().toString();

            if (!validateEmailAndPassword(username, password)) return; // validate input (format)
            loadingProgressBar.setVisibility(View.VISIBLE);
            fAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException e) {
                        registerUser(username, password);
                    } catch (Exception e) {
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Error" + e, Toast.LENGTH_LONG).show();
                    }
                }
            });
        });

    }

    private Boolean validateEmailAndPassword(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("Email is required");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return false;
        }
        if (!username.contains("@") || (username.contains("@") && !Patterns.EMAIL_ADDRESS.matcher(username).matches())) {
            usernameEditText.setError("Must be a valid email address");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    private void registerUser(String username, String password) {
        fAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Please verify your email", Toast.LENGTH_LONG).show();
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                    } else {
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Error" + task2.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                loadingProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Wrong Password" , Toast.LENGTH_LONG).show();
            }
        });
    }



}