package com.globales.socialmotion;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.globales.socialmotion.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignupActivity";
    private static final int PASSWORD_LENGTH = 6;

    EditText emailField;
    EditText passwordField;
    EditText confirmPasswordField;
    EditText firstNameField;
    EditText lastNameField;
    Button cancelSignupButton;
    Button createUserButton;

    protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initElements();
        setupAuth();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindElements();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    protected void setupAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d(TAG, "onAuthStateChanged");
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    saveNewUserDetails(user);
                    finish();
                }
            }
        };
    }

    private void saveNewUserDetails(FirebaseUser firebaseUser) {
        setupUser().saveUser(firebaseUser.getUid());
        String msg = "Your account has been created";
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void initElements() {
        cancelSignupButton = (Button) findViewById(R.id.cancel_signup_button);
        createUserButton = (Button) findViewById(R.id.create_user_button);
        emailField = (EditText) findViewById(R.id.email_field);
        passwordField = (EditText) findViewById(R.id.password_field);
        confirmPasswordField = (EditText) findViewById(R.id.confirm_password_field);
        firstNameField = (EditText) findViewById(R.id.first_name_field);
        lastNameField = (EditText) findViewById(R.id.last_name_field);
    }

    private void bindElements() {
        cancelSignupButton.setOnClickListener(this);
        createUserButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.cancel_signup_button:
                finish();
                break;
            case R.id.create_user_button:
                createUser();
                break;
        }
    }

    private void createUser() {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private User setupUser() {
        User user = new User();
        user.setFirstName(firstNameField.getText().toString());
        user.setLastName(lastNameField.getText().toString());
        user.setEmail(emailField.getText().toString());
        user.setPassword(passwordField.getText().toString());

        return user;
    }

    private boolean validateForm() {
        boolean valid = true;

        EditText[] requiredFields = { emailField, passwordField, confirmPasswordField, firstNameField, lastNameField };
        for (EditText requiredField : requiredFields) {
            valid = validateRequiredField(requiredField);
        }

        if (!valid) {
            return false;
        }

        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if (!password.equals(confirmPassword)) {
            valid = false;
            confirmPasswordField.setError("Passwords must match");
            confirmPasswordField.requestFocus();
        }

        if (password.length() < PASSWORD_LENGTH) {
            valid = false;
            passwordField.setError("Passwords must be at least " + PASSWORD_LENGTH + " characters");
            passwordField.requestFocus();
        }

        return valid;
    }

    private boolean validateRequiredField(EditText field) {
        boolean valid = true;

        String text = field.getText().toString();
        if (TextUtils.isEmpty(text)) {
            field.setError("Required.");
            valid = false;
        } else {
            field.setError(null);
        }

        return valid;
    }
}
