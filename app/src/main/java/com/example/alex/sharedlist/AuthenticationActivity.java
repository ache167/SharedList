package com.example.alex.sharedlist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;


public class AuthenticationActivity extends AppCompatActivity {
    private static final String TAG = "AuthenticationActivity";

    PinEntryEditText txtPinEntry;
    String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        txtPinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);

        Intent intent = getIntent();
        final String phoneNumber = intent.getStringExtra("phoneNumber");
        Log.d(TAG, phoneNumber + " wtf");

        //PinCode Listener
        txtPinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    final HashMap<String, Object> params = new HashMap<>();
                    params.put("phoneNumber", phoneNumber);
                    params.put("phoneVerificationCode", s.toString());
                    Log.d(TAG, params.toString());

                    //start progressbar
                    final ProgressDialog progressDialog = new ProgressDialog(AuthenticationActivity.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Creating Account...");
                    progressDialog.show();


                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    /*// On complete call either onLoginSuccess or onLoginFailed
                                    onLoginSuccess();
                                    // onLoginFailed();
                                    progressDialog.dismiss();*/

                                    ParseCloud.callFunctionInBackground("verifyPhoneNumber", params, new FunctionCallback<String>() {

                                        @Override
                                        public void done(String response, ParseException e) {
                                            //close progress bar
                                            progressDialog.dismiss();
                                            if(e == null){
                                                token = response;
                                                Log.d("Response1", "no exceptions! " + response);

                                                ParseUser.becomeInBackground(token, new LogInCallback() {
                                                    @Override
                                                    public void done(ParseUser user, ParseException e) {
                                                        if (e == null){
                                                            Log.d("Response2", "no exceptions! ");
                                                            Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                                                            intent.putExtra("logedin","yes");
                                                            startActivity(intent);
                                                            onSignupSuccess();
                                                        } else {
                                                            Log.d("Response3", "Exception: " + e);
                                                            Toast.makeText(getApplicationContext(),"Something wrong.  Please try again." + e, Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Log.d("Response4", "Exception: " + e.toString());
                                                e.printStackTrace();
                                                Toast.makeText(AuthenticationActivity.this, "Incorrect", Toast.LENGTH_LONG).show();
                                                txtPinEntry.setText(null);
                                            }
                                        }
                                    });

                                }
                            }, 3000);



                }
            }
        });


    }



   /* public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        //_signupButton.setEnabled(false);




        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }*/


    public void onSignupSuccess() {
       // _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }
/*

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

       // _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;



*/
/*        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } *//*


        return valid;
    }
*/


}