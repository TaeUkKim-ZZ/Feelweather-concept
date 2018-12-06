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
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //출처: https://altongmon.tistory.com/725 [IOS를 Java]

    EditText loginid, editpassword, editusername;
    Button signupbutton;
    String email, password, username;
    UserDTO userDTO = new UserDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        loginid = (EditText) findViewById(R.id.email_edittext2);
        editpassword = (EditText) findViewById(R.id.password_edittext2);
        editusername = (EditText) findViewById(R.id.getname_edittext3);
        signupbutton = (Button) findViewById(R.id.email_signup_button2);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = loginid.getText().toString();
                password = editpassword.getText().toString();
                username = editusername.getText().toString();

                if(email.equals("") || password.equals("") || username.equals("")) {
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 회원가입 성공
                                    Toast.makeText(SignupActivity.this, "가입 성공", Toast.LENGTH_SHORT).show();

                                    userDTO.useruid = firebaseAuth.getUid();
                                    userDTO.username = username;
                                    userDTO.feelstatus = "맑아요";
                                    userDTO.whyfeel = "";

                                    db.collection("Users").document(userDTO.useruid).set(userDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(SignupActivity.this, "기본 날씨 상태는 맑아요 입니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    Intent intent=new Intent(SignupActivity.this, FirstActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignupActivity.this, "가입 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
