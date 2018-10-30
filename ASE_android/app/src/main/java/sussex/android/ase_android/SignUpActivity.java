package sussex.android.ase_android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements
        View.OnClickListener {

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mPasswordField2;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        mEmailField = findViewById(R.id.signupEmail);
        mPasswordField = findViewById(R.id.signupPassword);
        mPasswordField2 = findViewById(R.id.signupPassword2);


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signupButton ){
            if(mPasswordField.getText().toString().equals(mPasswordField2.getText().toString())) {
                    createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
                }else{
                    Toast.makeText(SignUpActivity.this, "Passwords do not match.",
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void createAccount(String email, String password) {
        Log.d("", "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            Intent intent = new Intent(SignUpActivity.this, MapsActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
