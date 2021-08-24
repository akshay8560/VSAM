package cryptos.cryptocurrency.vsam;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import cryptos.cryptocurrency.vsam.activity.HomeActivity;
import cryptos.cryptocurrency.vsam.models.User;

public class UserInformation<prefixString> extends Activity {
    private DatePickerDialog datePickerDialog;
    private Calendar myCalendar;
    private  EditText name,dob,createPassword,conformPassword;
    private AutoCompleteTextView username;
    private int year, month, day;

    private ImageView userbackbtn;
    private final int CROP_IMAGE_ACTIVITY_REQUEST_CODE=203;
    private ImageView userprofilephoto;
    TextView addphoto;
    ProgressDialog pd;
    int length ;
    DatabaseReference reference;

    private FirebaseUser firebaseUser;
    private StorageTask uploadTask;
    private StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    FirebaseAuth auth;
    User user;
    private Button btn;
    String[] days = new String[]{"akshay_kumar", "vivek", "amir@123", "amir_khan", "akshay-0010", "vivak-001", "sunnday"};
    private Object CropImage;
    private Object CropImageView;
    private Uri mImageUri;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        name=findViewById(R.id.name);
        username=findViewById(R.id.username);
        dob=findViewById(R.id.dob);
        createPassword=findViewById(R.id.createPassword);
        createPassword.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
        createPassword.setText(GetPassword());
        conformPassword=findViewById(R.id.confirmPassword);
        conformPassword.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
        conformPassword.setText(GetPassword());
        final TextInputLayout nameWrapper = (TextInputLayout) findViewById(R.id.nameWrapper);
        final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        final TextInputLayout dobWrapper = (TextInputLayout) findViewById(R.id.dobWrapper);
        btn=findViewById(R.id.btn);
        userbackbtn=findViewById(R.id.userBackBtn);
        userprofilephoto=findViewById(R.id.userprofilephoto);
        addphoto=findViewById(R.id.change_photo);
        username.setAdapter(new ArrayAdapter<>(UserInformation.this, android.R.layout.simple_list_item_1, days));


        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference().child("Uploads");
        FirebaseDatabase.getInstance().getReference().child("GoogleUsers").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);

                Picasso.get().load(user.getImageurl()).into(userprofilephoto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        userprofilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.theartofdev.edmodo.cropper.CropImage.activity().setCropShape(com.theartofdev.edmodo.cropper.CropImageView.CropShape.OVAL).start(UserInformation.this);

            }
        });
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.theartofdev.edmodo.cropper.CropImage.activity().setCropShape(com.theartofdev.edmodo.cropper.CropImageView.CropShape.OVAL).start(UserInformation.this);

            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pd = new ProgressDialog(UserInformation.this);
                pd.setMessage("Please Wait.....");
                String str_name = name.getText().toString();
                String str_username = username.getText().toString();
                String str_dob = dob.getText().toString();
                String str_createPassword = createPassword.getText().toString();
                String str_conformpassword = conformPassword.getText().toString();


                if (TextUtils.isEmpty(str_name) || TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_dob) || TextUtils.isEmpty(str_createPassword) || TextUtils.isEmpty(str_conformpassword)) {

                    Toast.makeText(UserInformation.this, "All Fields are Required ", Toast.LENGTH_SHORT).show();
                } else if (username.length() < 4) {
                    Toast.makeText(UserInformation.this, "username must have 4 character", Toast.LENGTH_SHORT).show();
                } else if (str_createPassword.length() < 6) {
                    Toast.makeText(UserInformation.this, "Password must have 6 character", Toast.LENGTH_SHORT).show();
                } else if (str_conformpassword.length() < 6) {
                    Toast.makeText(UserInformation.this, "Password must have 6 character", Toast.LENGTH_SHORT).show();

                } else if (!str_createPassword.equals(str_conformpassword)){
                    Toast.makeText(UserInformation.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                }

                else {

                    register(str_name, str_username, str_dob, str_createPassword, str_conformpassword);
                    pd.dismiss();
                    Toast.makeText(UserInformation.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(UserInformation.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }

        });
        nameWrapper.setHint("Name");
        usernameWrapper.setHint("Username");
        dobWrapper.setHint("Date of Birth");

        myCalendar = Calendar.getInstance();

        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);

        this.setTheme(R.style.DatePickerDialog);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (year>2010){
                    Toast.makeText(UserInformation.this, "your age is under 11", Toast.LENGTH_SHORT).show();
                }else
                {
                    updateLabel();
                }

            }
        };

        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UserInformation.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }


    private String GetPassword() {
        char[] chars;
        chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*!+~_".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();

        Random rand = new Random();


        for(int i = 0; i < length; i++){
            char c = chars[rand.nextInt(chars.length)];
            stringBuilder.append(c);

        }

        return stringBuilder.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            uploadImage();
        }




        else {
            Toast.makeText(this, "Something gone wrong !", Toast.LENGTH_SHORT).show();
        }

    }
    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (mImageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri>task) {
                    if (task.isSuccessful()){



                        /*Uri downloadUri=task.getResult();
                        String miUrlOk = downloadUri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GoogleUsers").child(firebaseUser.getUid());
                        HashMap<String, Object> map1 = new HashMap<>();
                        map1.put("imageurl", ""+miUrlOk);
                        reference.updateChildren(map1);
                        pd.dismiss();*/

                    }else {
                        Toast.makeText(UserInformation.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserInformation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }

    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void register(String str_name, String str_username, String str_dob, String str_createPassword, String str_conformpassword) {





    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(myCalendar.getTime()));
    }



    public void finish_user(View view) {
        onBackPressed();
    }

//    public void enterHomeActivity(View view) {
//        startActivity(new Intent(UserInformation.this , HomeActivity.class));
//    }
}
