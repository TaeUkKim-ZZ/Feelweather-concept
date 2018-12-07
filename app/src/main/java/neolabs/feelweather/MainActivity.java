package neolabs.feelweather;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String[] numberuid = new String[99999];
    int cnt = 0;

    UserDTO userDTO = new UserDTO();
    UidDTO uidDTO = new UidDTO();

    FloatingActionButton changeweather, addfriends;

    RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        changeweather = findViewById(R.id.floatingActionButton2);
        addfriends = findViewById(R.id.floatingActionButton3);

        recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        //feelArrayList.add(new WeatherItem("김네오", R.drawable.sun));
        recyclerview.setAdapter(new MyAdapter());

        changeweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangeWeatherActivity.class);
                startActivity(intent);
            }
        });

        addfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindFriendsActivity.class);
                startActivity(intent);
            }
        });

        recyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerview, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getApplicationContext(),position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                        intent.putExtra("useruid", numberuid[position]);
                        Log.d("유저 고유 UID", numberuid[position]);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<WeatherItem> feelArrayList = new ArrayList<>();

        public MyAdapter() {
            db.collection("Users").document(firebaseAuth.getUid()).collection("Friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        uidDTO = document.toObject(UidDTO.class);
                        db.collection("Users").document(uidDTO.useruid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                numberuid[cnt] = document.getId();
                                userDTO = task.getResult().toObject(UserDTO.class);
                                Log.d("유저들", userDTO.username + " : " + userDTO.feelstatus);
                                if(userDTO.feelstatus.equals("맑아요")){
                                    feelArrayList.add(new WeatherItem(userDTO.username, R.drawable.sun, userDTO.whyfeel));
                                    notifyDataSetChanged();
                                }
                                else if(userDTO.feelstatus.equals("비와요")) {
                                    feelArrayList.add(new WeatherItem(userDTO.username, R.drawable.rain, userDTO.whyfeel));
                                    notifyDataSetChanged();
                                }
                                else if(userDTO.feelstatus.equals("눈와요")) {
                                    feelArrayList.add(new WeatherItem(userDTO.username, R.drawable.snow, userDTO.whyfeel));
                                    notifyDataSetChanged();
                                }
                                else if(userDTO.feelstatus.equals("안개가 껴요")) {
                                    feelArrayList.add(new WeatherItem(userDTO.username, R.drawable.fog, userDTO.whyfeel));
                                    notifyDataSetChanged();
                                }
                                else if(userDTO.feelstatus.equals("폭풍우가 쳐요")) {
                                    feelArrayList.add(new WeatherItem(userDTO.username, R.drawable.storm, userDTO.whyfeel));
                                    notifyDataSetChanged();
                                }
                                else if(userDTO.feelstatus.equals("번개가 쳐요")) {
                                    feelArrayList.add(new WeatherItem(userDTO.username, R.drawable.thndr, userDTO.whyfeel));
                                    notifyDataSetChanged();
                                }
                                cnt++;
                            }
                        });
                    }
                }
            });
            /*db.collection("Users").document(firebaseAuth.getUid()).collection("Following").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (final QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("태그", document.getId() + " => " + document.getData());
                                    //if(document.getId().equals(FirebaseAuth.getInstance().getUid())) continue;
                                    db.collection("Users").document(document.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            numberuid[cnt] = document.getId();
                                            userDTO = task.getResult().toObject(UserDTO.class);
                                            Log.d("유저들", userDTO.username + " : " + userDTO.feelstatus);
                                            if(userDTO.feelstatus.equals("맑아요")){
                                                feelArrayList.add(new WeatherItem(userDTO.username, R.drawable.sun, userDTO.whyfeel));
                                                notifyDataSetChanged();
                                            }
                                            else if(userDTO.feelstatus.equals("비와요")) {
                                                feelArrayList.add(new WeatherItem(userDTO.username, R.drawable.rain, userDTO.whyfeel));
                                                notifyDataSetChanged();
                                            }
                                            else if(userDTO.feelstatus.equals("눈와요")) {
                                                feelArrayList.add(new WeatherItem(userDTO.username, R.drawable.snow, userDTO.whyfeel));
                                                notifyDataSetChanged();
                                            }
                                            else if(userDTO.feelstatus.equals("안개가 껴요")) {
                                                feelArrayList.add(new WeatherItem(userDTO.username, R.drawable.fog, userDTO.whyfeel));
                                                notifyDataSetChanged();
                                            }
                                            else if(userDTO.feelstatus.equals("폭풍우가 쳐요")) {
                                                feelArrayList.add(new WeatherItem(userDTO.username, R.drawable.storm, userDTO.whyfeel));
                                                notifyDataSetChanged();
                                            }
                                            else if(userDTO.feelstatus.equals("번개가 쳐요")) {
                                                feelArrayList.add(new WeatherItem(userDTO.username, R.drawable.thndr, userDTO.whyfeel));
                                                notifyDataSetChanged();
                                            }
                                            cnt++;
                                        }
                                    });
                                }
                            } else {
                                Log.d("태그", "Error getting documents: ", task.getException());
                            }
                        }
                    });*/
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weatherimage, parent, false);

            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MyViewHolder myViewHolder = (MyViewHolder) holder;

            myViewHolder.feelimage.setImageResource(feelArrayList.get(position).weatherimage);
            myViewHolder.username.setText(feelArrayList.get(position).name);
            myViewHolder.introduce.setText(feelArrayList.get(position).introduce);
        }

        @Override
        public int getItemCount() {
            return feelArrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView feelimage;
        TextView introduce;
        TextView username;

        MyViewHolder(View view) {
            super(view);
            feelimage = view.findViewById(R.id.feelimage);
            username = view.findViewById(R.id.username);
            introduce = view.findViewById(R.id.introduce);
        }
    }
}
