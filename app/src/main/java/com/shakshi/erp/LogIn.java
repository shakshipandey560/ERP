package com.shakshi.erp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class LogIn extends AppCompatActivity {

    private static final int RC_SIGN_IN = 101;

    TextView register;
    EditText edtemail,edtpswd;
    Button butLogIn;

    private FirebaseAuth mAuth;
    private FirebaseAuth auth;
    private ProgressDialog mLoadingBar;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private LoginButton loginButton;

    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        register = findViewById(R.id.lRegisterhere);
        edtemail = findViewById(R.id.lUsername);
        edtpswd = findViewById(R.id.lPassword);
        butLogIn = findViewById(R.id.lLogin);


        mAuth=FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(LogIn.this);

        //LOGIN BUTTON
        butLogIn.setOnClickListener(view -> checkInfo());

        //REGISTER HERE
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, SignIn.class);
                startActivity(intent);
            }
        });

        //Google
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //FACEBOOK

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.facebook);
        loginButton.setPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LogIn.this, "onSuccess:" + loginResult, Toast.LENGTH_SHORT).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LogIn.this, "CANCLE", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LogIn.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                Toast.makeText(this, "USER EMAIL~"+ personEmail, Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(LogIn.this,MainActivity.class);
            startActivity(intent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(LogIn.this,MainActivity.class);
        startActivity(intent);
    }

    private void checkInfo() {
        String email = edtemail.getText().toString();
        String password = edtpswd.getText().toString();

        if(email.isEmpty() || !email.contains("@gmail.com") ){
            showError(edtemail,"Email is not valid");
        }
        else if(password.isEmpty() || password.length()<7){
            showError(edtpswd,"Password should have 7 character");
        }
        else{

            mLoadingBar.setTitle("LogIn");
            mLoadingBar.setMessage("Please Wait!!!");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {   if(mAuth.getCurrentUser().isEmailVerified()) {
                        mLoadingBar.dismiss();
                        Intent intent = new Intent(LogIn.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(LogIn.this, "Please Verify Your Email Address!!!", Toast.LENGTH_SHORT).show();
                    }
                    }
                    else{
                        mLoadingBar.dismiss();
                        Toast.makeText(LogIn.this, "Error"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
    //facebook------
    private void handleFacebookAccessToken(AccessToken token) {

        Toast.makeText(this, "handleFacebookAccessToken:" + token, Toast.LENGTH_SHORT).show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LogIn.this, "SignIn Successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LogIn.this, "Authentication failed."+task.getException(),Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

}