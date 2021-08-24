package cryptos.cryptocurrency.vsam.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cryptos.cryptocurrency.vsam.R;

public class EnterPhoneNumberActivity extends AppCompatActivity {
    private EditText enternumber;
    private TextView getotp;
    ProgressBar progressBar;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    String url="http://clients.jprportal.com/sarju/api/user_login/?api_key=w0fp55cIdJ6lLuOqVEd251zKw6lnNd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone_number);

        enternumber = findViewById(R.id.editText);
        getotp=findViewById(R.id.getotp);
        progressBar = findViewById(R.id.prograssbars);
        getData();
        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enternumber.getText().toString().trim().isEmpty()) {
                    if (enternumber.getText().toString().trim().length() == 10) {
                        progressBar.setVisibility(View.VISIBLE);
                        enternumber.setVisibility(v.VISIBLE);
                        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+enternumber.getText().toString(), 5
                                , TimeUnit.SECONDS, EnterPhoneNumberActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(View.GONE);
                                        enternumber.setVisibility(v.VISIBLE);

                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {

                                        progressBar.setVisibility(View.GONE);
                                        enternumber.setVisibility(v.VISIBLE);
                                        Toast.makeText(EnterPhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onCodeSent(@NonNull @NotNull String backendotp, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        firebaseDatabase= FirebaseDatabase.getInstance();
                                        reference=firebaseDatabase.getReference();
                                        FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        HashMap<String,Object>map=new HashMap<>();
                                        map.put("phone",enternumber.getText().toString());
                                        map.put("imageurl","");
                                        map.put("id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        reference.child("MobileUsers").child(FirebaseAuth.getInstance().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                if (task.isSuccessful()){
                                                    Toast.makeText(EnterPhoneNumberActivity.this, "Verify  your OTP", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(EnterPhoneNumberActivity.this, OtpVerificationActivity.class);
                                                    intent.putExtra("mobile", enternumber.getText().toString());
                                                    intent.putExtra("backendotp", backendotp);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                });
                    } else {
                        Toast.makeText(EnterPhoneNumberActivity.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EnterPhoneNumberActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getData() {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Toast.makeText(EnterPhoneNumberActivity.this, "status :"+response.getInt("status")
                            +"," +"message :"+response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(EnterPhoneNumberActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void verifyMobile(View view) {
        if (enternumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
        } else {

            hideKeyboard(this);
            Intent intent = new Intent(this, OtpVerificationActivity.class);
//            intent.putExtra("mobile", editPhone.getText().toString());
//            intent.putExtra("countryCode", countryCodePicker.getSelectedCountryCode());
            startActivity(intent);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void finish_phone(View view) {
        onBackPressed();
    }
}


