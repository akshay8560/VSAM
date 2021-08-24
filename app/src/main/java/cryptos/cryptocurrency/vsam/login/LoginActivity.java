package cryptos.cryptocurrency.vsam.login;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

import cryptos.cryptocurrency.vsam.R;
import cryptos.cryptocurrency.vsam.UserInformation;
import cryptos.cryptocurrency.vsam.webView.WebViewActivity;

public class LoginActivity extends AppCompatActivity {

    private VideoView videoBG;
    MediaPlayer mediaPlayer;
    int mCurrentVideoPosition;
    Dialog dialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    private CallbackManager mCallbackManager;
    private LoginButton fbLoginButton;
    private FirebaseAuth mAuth;
    private AccessTokenTracker accessTokenTracker;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String EMAIL = "email";
    private static final String TAG = "FacebookAuthentication";

    private String privacyUrl = "https://mobile.vsam.me/privacy";
    private String termsUrl = "https://mobile.vsam.me/terms";

    private boolean isFacebook = false, isGoogle = false;

    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new Dialog(this);

        videoBG = (VideoView) findViewById(R.id.videoView);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vid);

        videoBG.setVideoURI(uri);
        videoBG.start();

        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;
                mediaPlayer.setLooping(true);
                if(mCurrentVideoPosition != 0)
                {
                    mediaPlayer.seekTo(mCurrentVideoPosition);
                    mediaPlayer.start();
                }
            }
        });



        mAuth = FirebaseAuth.getInstance();

        //Fb Login
        mCallbackManager = CallbackManager.Factory.create();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    updateUI(user);
                } else {
                    updateUI(null);
                }
            }
        };

    }

    @Override
    protected void onPause() {
        super.onPause();

        mCurrentVideoPosition = mediaPlayer.getCurrentPosition();
        videoBG.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void startPhoneLogin(View view) {
        startActivity(new Intent(this, EnterPhoneNumberActivity.class));
    }

    //Facebook

    public void startFacebookLogin(View view) {

        isGoogle = false;
        isFacebook = true;

        fbLoginButton = new LoginButton(this);
        fbLoginButton.setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
        setFacebookCallback();
        fbLoginButton.performClick();
    }

    private void setFacebookCallback() {
        fbLoginButton.setPermissions(Arrays.asList(EMAIL));
        mCallbackManager = CallbackManager.Factory.create();
        fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Facebook cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Facebook Exception: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookToken(AccessToken token) {
        Log.d(TAG, "handleFacebookToken" + token);
        AuthCredential authCredential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Sign in with Credential:Successful");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);

                } else {
                    Log.d(TAG, "Sign in with Credential:Failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication Successfull", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
            }
        });
    }

    //Google

    public void startGoogleLogin(View view) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        isGoogle = true;
        isFacebook = false;

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityIfNeeded(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isFacebook) {
            isFacebook = false;
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (isGoogle) {
            isGoogle = false;
            if(requestCode == RC_SIGN_IN){
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{

            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this,"Continue",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(LoginActivity.this, UserInformation.class);
            startActivity(intent);
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(LoginActivity.this,"Sign In Failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {

        if (acct != null) {
            firebaseDatabase= FirebaseDatabase.getInstance();
            reference=firebaseDatabase.getReference();
            mAuth.getInstance().getCurrentUser().getUid();
            HashMap<String,Object> map=new HashMap<>();
            map.put("Email", EMAIL);
            map.put("imageurl","https://firebasestorage.googleapis.com/v0/b/vsaminternet.appspot.com/o/images%2FAkshay%2F64572.png?alt=media&token=bbde0b9e-781d-4190-b266-8f71fbf01e74");
            map.put("id",mAuth.getInstance().getCurrentUser().getUid());
            reference.child("GoogleUsers").child(mAuth.getInstance().getCurrentUser().getUid()).setValue(map);
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this, "acc failed", Toast.LENGTH_SHORT).show();
        }
    }

    protected void updateUI(FirebaseUser user) {
        if (user.isEmailVerified()){
            Intent intent = new Intent(LoginActivity.this, UserInformation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public void openPrivacy(View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", privacyUrl);
        intent.putExtra("isAbout", false);
        startActivity(intent);
    }

    public void openTerms(View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", termsUrl);
        startActivity(intent);
    }

    public void openMoreDialog(View view) {
        dialog.getWindow().setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

}
