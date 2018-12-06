package neolabs.feelweather;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirstActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    EditText loginid, editpassword;
    Button login, signup;
    String email, password;
    UserDTO userDTO = new UserDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        firebaseAuth = FirebaseAuth.getInstance();

        loginid = (EditText) findViewById(R.id.email_edittext);
        editpassword = (EditText) findViewById(R.id.password_edittext);
        login = (Button) findViewById(R.id.email_login_button);
        signup = (Button) findViewById(R.id.email_signup_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = loginid.getText().toString();
                password = editpassword.getText().toString();

                if(email.equals("") || password.equals("")) {
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(FirstActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 로그인 성공
                                    Toast.makeText(FirstActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(FirstActivity.this, MainActivity.class);
                                    startActivity(intent);

                                    finish();
                                } else {
                                    // 로그인 실패
                                    Toast.makeText(FirstActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }
}