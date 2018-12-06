package neolabs.feelweather;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Any;

import java.util.HashMap;
import java.util.Map;

public class ChangeWeatherActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    UserDTO userDTO = new UserDTO();
    LinearLayout sun, rain, snow, fog, storm, thunder;
    EditText introduce;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_weather);

        firebaseAuth = FirebaseAuth.getInstance();
        userDTO.useruid = firebaseAuth.getUid();

        sun = (LinearLayout) findViewById(R.id.first);
        rain = (LinearLayout) findViewById(R.id.second);
        snow = (LinearLayout) findViewById(R.id.third);
        fog = (LinearLayout) findViewById(R.id.fourth);
        storm = (LinearLayout) findViewById(R.id.fifth);
        thunder = (LinearLayout) findViewById(R.id.sixth);

        save = (Button) findViewById(R.id.introducebutton);
        introduce = (EditText) findViewById(R.id.introduceedit);

        db.collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    userDTO = task.getResult().toObject(UserDTO.class);
                    introduce.setText(userDTO.whyfeel);
                }
            }
        });

        //final Map<String,Object> updates = new HashMap<>();

        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updates.put("feelstatus", "맑아요");

                db.collection("Users").document(firebaseAuth.getUid()).update("feelstatus", "맑아요").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChangeWeatherActivity.this, "날씨가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updates.put("feelstatus", "비와요");

                db.collection("Users").document(firebaseAuth.getUid()).update("feelstatus", "비와요").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChangeWeatherActivity.this, "날씨가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        snow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updates.put("feelstatus", "눈와요");

                db.collection("Users").document(firebaseAuth.getUid()).update("feelstatus", "눈와요").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChangeWeatherActivity.this, "날씨가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        fog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updates.put("feelstatus", "안개가 껴요");

                db.collection("Users").document(firebaseAuth.getUid()).update("feelstatus", "안개가 껴요").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChangeWeatherActivity.this, "날씨가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        storm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updates.put("feelstatus", "폭풍우가 쳐요");

                db.collection("Users").document(firebaseAuth.getUid()).update("feelstatus", "폭풍우가 쳐요").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChangeWeatherActivity.this, "날씨가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        thunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updates.put("feelstatus", "번개가 쳐요");

                db.collection("Users").document(firebaseAuth.getUid()).update("feelstatus", "번개가 쳐요").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChangeWeatherActivity.this, "날씨가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users").document(firebaseAuth.getUid()).update("whyfeel", introduce.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChangeWeatherActivity.this, "날씨가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ChangeWeatherActivity.this, MainActivity.class);
                        startActivity(intent);

                        finish();
                    }
                });
            }
        });
    }
}
