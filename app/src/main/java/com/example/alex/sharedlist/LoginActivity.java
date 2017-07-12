package com.example.alex.sharedlist;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _phoneNumberText;
    Button _sendCodeButton;
    String e164PhoneNumber;
    String countryCode = "IL";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        _phoneNumberText = (EditText)findViewById(R.id.phoneNumber);
        _sendCodeButton = (Button)findViewById(R.id.sendCodeButton);

        _sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

/*        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });*/

/*        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });*/
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validatePhoneNumber()) {
            onLoginFailed();
            return;
        }

        // TODO: dialog box, asking user if it his number, change/accept

        _sendCodeButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending SMS...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here. sending SMS

        if(e164PhoneNumber != null) {
            sendSMS(e164PhoneNumber);
            Log.d(TAG, "after send sms " + e164PhoneNumber);
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _sendCodeButton.setEnabled(true);
       // finish();
        Intent intent = new Intent(this, AuthenticationActivity.class);
        intent.putExtra("phoneNumber", e164PhoneNumber);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Could not LOGIN, something went wrong", Toast.LENGTH_LONG).show();

        _sendCodeButton.setEnabled(true);
    }

    public boolean validatePhoneNumber() {
        boolean valid = true;

        String phoneNumber = _phoneNumberText.getText().toString();

        String countryCode = "IL";

        e164PhoneNumber = PhoneNumberUtils.formatNumberToE164(phoneNumber, countryCode);

        if (e164PhoneNumber == null){
            _phoneNumberText.setError("Enter a valid phone number.");
            valid = false;
        }

        return valid;
    }

    public void sendSMS(String e164PhoneNumber){

        HashMap<String, Object> params = new HashMap<>();
        params.put("phoneNumber", e164PhoneNumber);

        ParseUser user = ParseUser.getCurrentUser();
        String sessionToken = user.getSessionToken();
        params.put("sessionToken", sessionToken);
        params.put("objectId", user.getObjectId());
        Log.d(TAG, "phone number: " + e164PhoneNumber);
        Log.d(TAG, params.toString());

        ParseCloud.callFunctionInBackground("sendVerificationCode", params, new FunctionCallback<Object>() {
            public void done(Object response, ParseException e) {

                //TODO: change cloud code, to send 6 digits with dash (xyz-efg)
                if (e == null) {
                    Log.i("Response", "no exceptions! " + response.toString());
                    // Code sent successfully you have to wait it or ask the user to enter the code for verification
                    //moving to next page for entering the SecretCode
                } else {
                    Log.i("Response", "Exception: " + e.toString());
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),  "Something wrong.  Please try again." + e, Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}