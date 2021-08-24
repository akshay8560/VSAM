package cryptos.cryptocurrency.vsam.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cryptos.cryptocurrency.vsam.R;
import cryptos.cryptocurrency.vsam.UserInformation;


public class OtpVerificationActivity extends AppCompatActivity {

    private EditText Ed1, Ed2, Ed3, Ed4, Ed5, Ed6;
    TextView textView, resendotp;
    private TextView submit;
    String getotpbackend;
    ProgressBar progressBar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    private TextView timer;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        Ed1 = findViewById(R.id.inputotp1);
        Ed2 = findViewById(R.id.inputotp2);
        Ed3 = findViewById(R.id.inputotp3);
        Ed4 = findViewById(R.id.inputotp4);
        Ed5 = findViewById(R.id.inputotp5);
        Ed6 = findViewById(R.id.inputotp6);
        resendotp = findViewById(R.id.textresendotp);
        submit = findViewById(R.id.submitotp);
        textView = findViewById(R.id.mobilenumbershow);
        timer=findViewById(R.id.timer);
        textView.setText(String.format("+91-%s",getIntent().getStringExtra("mobile")));
        getotpbackend = getIntent().getStringExtra("backendotp");
        progressBar = findViewById(R.id.prograssbarv);
        mCountDownTimer=new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                String sDuration= String.format(Locale.ENGLISH,"%02d",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
                timer.setText(sDuration);
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onFinish() {
                resendotp.setTextColor(R.color.textColor);
                resendotp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PhoneAuthProvider.getInstance(firebaseAuth).verifyPhoneNumber(getIntent().getStringExtra("mobile"), 60
                                , TimeUnit.SECONDS, OtpVerificationActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull @org.jetbrains.annotations.NotNull com.google.firebase.auth.PhoneAuthCredential phoneAuthCredential) {

                                        String code=phoneAuthCredential.getSmsCode();
                                        if (code!=null){
                                        }
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull @org.jetbrains.annotations.NotNull FirebaseException e) {


                                        Toast.makeText(OtpVerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull @NotNull String newbackendotp, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                        getotpbackend = newbackendotp;
                                        Toast.makeText(OtpVerificationActivity.this, "OTP Resend on your Phone", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });


            }
        }.start();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Ed1.getText().toString().trim().isEmpty() && !Ed2.getText().toString().trim().isEmpty() &&
                        !Ed3.getText().toString().trim().isEmpty() && !Ed4.getText().toString().trim().isEmpty()
                        && !Ed5.getText().toString().trim().isEmpty() && !Ed6.getText().toString().trim().isEmpty()) {

                    String entercodeotp = Ed1.getText().toString() + Ed2.getText().toString() + Ed3.getText().toString() +
                            Ed4.getText().toString() + Ed5.getText().toString() + Ed6.getText().toString();


                    if (getotpbackend != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.INVISIBLE);
                        com.google.firebase.auth.PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(getotpbackend, entercodeotp);
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(OtpVerificationActivity.this, "OTP Verify  & your register Successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(OtpVerificationActivity.this, UserInformation.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(OtpVerificationActivity.this, "Enter Correct OTP", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    } else {
                        Toast.makeText(OtpVerificationActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(OtpVerificationActivity.this, "Please enter all 6 numbers", Toast.LENGTH_SHORT).show();
                }


            }
        });

        numberotpmove();

    }



    private void numberotpmove() {
        Ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Ed1.getText().toString().length() == 1) {
                    Ed2.requestFocus();
                }
            }
        });
        Ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Ed2.getText().toString().length() == 0) {
                    Ed1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Ed2.getText().toString().length() == 1) {
                    Ed3.requestFocus();
                }
            }
        });
        Ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Ed3.getText().toString().length() == 0) {
                    Ed2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Ed3.getText().toString().length() == 1) {
                    Ed4.requestFocus();
                }
            }
        });
        Ed4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Ed4.getText().toString().length() == 0) {
                    Ed3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Ed4.getText().toString().length() == 1) {
                    Ed5.requestFocus();
                }
            }
        });
        Ed5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Ed5.getText().toString().length() == 0) {
                    Ed4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Ed5.getText().toString().length() == 1) {
                    Ed6.requestFocus();
                }
            }
        });
        Ed6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Ed6.getText().toString().length() == 0) {
                    Ed5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Ed6.getText().toString().length() == 1) {
                    Ed6.requestFocus();
                }
            }
        });

    }

    public void finish_verify(View view) {
        onBackPressed();
    }

    public void enterInformation(View view) {
        startActivity(new Intent(this, UserInformation.class));
    }
}