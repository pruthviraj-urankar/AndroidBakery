package com.example.androidbakery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidbakery.Model.User;
import com.example.androidbakery.common.common;
import com.google.android.gms.signin.SignIn;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SIgnIn extends AppCompatActivity {

    MaterialEditText edtPhone,edtPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone=(MaterialEditText)findViewById(R.id.edtPhone);
        edtPassword=(MaterialEditText)findViewById(R.id.edtPassword);

        btnSignIn=(Button)findViewById(R.id.btnSignIn);


        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("user");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SIgnIn.this);
                mDialog.setMessage("Please wait!!!");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(edtPhone.getText().toString()).exists()){
                        mDialog.dismiss();
                        User user=dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                        user.setPhone(edtPhone.getText().toString());
                        if(user.getPassword().equals(edtPassword.getText().toString()))
                        {
                            //Toast.makeText(SIgnIn.this,"Sign In Successful",Toast.LENGTH_SHORT).show();
                            Intent homeIntent=new Intent(SIgnIn.this,Home.class);
                            common.currentUser=user;
                            startActivity(homeIntent);
                            finish();
                        }
                        else
                            Toast.makeText(SIgnIn.this, "Sign In Failes", Toast.LENGTH_SHORT).show();

                        }

                        else {
                            mDialog.dismiss();
                            Toast.makeText(SIgnIn.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
