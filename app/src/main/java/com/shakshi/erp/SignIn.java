package com.shakshi.erp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
//import com.shakshi.erp.userInfo.Connect;

public class SignIn extends AppCompatActivity {

    TextView logIn;
    private EditText inputUserName,inputPassword,inputEmail,inputNumber,inputConfirmPassword;
    Button btnReg;

    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;



    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        logIn = findViewById(R.id.alreadyRegister);
        inputUserName = findViewById(R.id.sName);
        inputPassword = findViewById(R.id.sPassword);
        inputEmail = findViewById(R.id.sEmail);
        inputNumber =findViewById(R.id.sPhone);
        inputConfirmPassword = findViewById(R.id.sConPassword);

        mAuth=FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(SignIn.this);

        


        btnReg = findViewById(R.id.reGister);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCrededentials();


            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, LogIn.class);
                startActivity(intent);
            }
        });
    }

    private void checkCrededentials() {

        String fullname = inputUserName.getText().toString();
        String email = inputEmail.getText().toString();
        String mobile_no = inputNumber.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();


        if(fullname.isEmpty() || fullname.length()<7){
            showError(inputUserName,"Your Username is not Valid");
        }
        else if(email.isEmpty() || !email.contains("@gmail.com") ){
            showError(inputEmail,"Email is not valid");
        }
        else if(mobile_no.isEmpty() || mobile_no.length()<10){
            showError(inputNumber,"Enter  Valid Phone Number");
        }
        else if(password.isEmpty() || password.length()<7){
            showError(inputPassword,"Password should have 7 character");
        }
        else if(confirmPassword.isEmpty() || !confirmPassword.equals(password)){
            showError(inputConfirmPassword,"Password is not matched");
        }
        else{



            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("Please Wait!!!");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();


            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //Send a User a Verification Email
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    FirebaseUser rUser = mAuth.getCurrentUser();
                                    String userId = rUser.getUid();
                                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                                    HashMap<String,String> hashMap = new HashMap<>();
                                    hashMap.put("userId",userId);
                                    hashMap.put("Name",fullname);
                                    hashMap.put("Email",email);
                                    hashMap.put("Mobile_No",mobile_no);
                                    hashMap.put("Password",password);

                                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                           if(task.isSuccessful()){
                                               Toast.makeText(SignIn.this, "Successful Register. Please check your email for verification!!!", Toast.LENGTH_SHORT).show();
                                               mLoadingBar.dismiss();

                                               Intent intent = new Intent(SignIn.this, LogIn.class);
                                               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                               startActivity(intent);
                                           }
                                           else{
                                               Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                           }
                                        }
                                    });

                                }
                                else{
                                    Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                    else{

                        Toast.makeText(SignIn.this, "Error"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        mLoadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();

    }
}