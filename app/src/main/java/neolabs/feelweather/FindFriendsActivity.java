package neolabs.feelweather;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindFriendsActivity extends AppCompatActivity {

    EditText inputuseremail;
    Button sendit;
    RecyclerView recyclerView;
    //MyAdapter myAdapter;

    UserDTO userDTO = new UserDTO();

    Boolean isfriend = false;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<ResultItem> resultArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        firebaseAuth = FirebaseAuth.getInstance();

        inputuseremail = (EditText) findViewById(R.id.messageActivity_editText3);
        sendit = (Button) findViewById(R.id.sendbutton3);

        //recyclerView = findViewById(R.id.findresultrecyclerView);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //feelArrayList.add(new WeatherItem("김네오", R.drawable.sun));
        //myAdapter = new MyAdapter();
        //recyclerView.setAdapter(myAdapter);

        sendit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users").whereEqualTo("useremail", inputuseremail.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            isfriend = true;
                            Log.d("resulttag", document.getId() + " => " + document.getData());
                            userDTO = document.toObject(UserDTO.class);

                            Map<String, Object> friend = new HashMap<>();
                            friend.put("useruid", userDTO.useruid);

                            Map<String, Object> oppofriend = new HashMap<>();
                            oppofriend.put("useruid", firebaseAuth.getUid());

                            db.collection("Users").document(firebaseAuth.getUid()).collection("Friends").document().set(friend).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });

                            db.collection("Users").document(userDTO.useruid).collection("Friends").document().set(oppofriend).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                        }

                        if(isfriend) {
                            Toast.makeText(getApplicationContext(), "친구추가 완료", Toast.LENGTH_SHORT).show();
                            isfriend = false;
                        }
                    }
                });
            }
        });
    }

    /*public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public MyAdapter() {

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.findlist, parent, false);

            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MyViewHolder myViewHolder = (MyViewHolder) holder;

            myViewHolder.imageView.setImageResource(resultArrayList.get(position).weatherimage);
            myViewHolder.textView.setText(resultArrayList.get(position).username);
            myViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return resultArrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        Button button;

        MyViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.userfeelimage);
            textView = view.findViewById(R.id.usernames);
            button = view.findViewById(R.id.addfriendsbutton);
        }
    }*/
}
