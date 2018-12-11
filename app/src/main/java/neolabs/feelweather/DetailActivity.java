package neolabs.feelweather;

import android.content.Intent;
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
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.Query.Direction;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    ImageView feelstatus;
    TextView username, userintroduce;
    String detailuseruid;

    CommentDTO commentDTO = new CommentDTO();
    UserDTO userDTO = new UserDTO();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView recyclerview;

    EditText commenttext;
    Button send;

    CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        detailuseruid = intent.getExtras().getString("useruid");

        recyclerview = findViewById(R.id.comment);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        recyclerview.setAdapter(new CommentAdapter());

        feelstatus = (ImageView) findViewById(R.id.feelimage);
        username = (TextView) findViewById(R.id.username2);
        userintroduce = (TextView) findViewById(R.id.introduce2);

        commenttext = (EditText) findViewById(R.id.messageActivity_editText);
        send = (Button) findViewById(R.id.sendbutton);

        db.collection("Users").document(detailuseruid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                userDTO = task.getResult().toObject(UserDTO.class);
                Log.d("유저들", userDTO.username + " : " + userDTO.feelstatus);
                if(userDTO.feelstatus.equals("맑아요")){
                    feelstatus.setImageResource(R.drawable.sun);
                    username.setText(userDTO.username);
                    userintroduce.setText(userDTO.whyfeel);
                }
                else if(userDTO.feelstatus.equals("비와요")) {
                    feelstatus.setImageResource(R.drawable.rain);
                    username.setText(userDTO.username);
                    userintroduce.setText(userDTO.whyfeel);
                }
                else if(userDTO.feelstatus.equals("눈와요")) {
                    feelstatus.setImageResource(R.drawable.snow);
                    username.setText(userDTO.username);
                    userintroduce.setText(userDTO.whyfeel);
                }
                else if(userDTO.feelstatus.equals("안개가 껴요")) {
                    feelstatus.setImageResource(R.drawable.fog);
                    username.setText(userDTO.username);
                    userintroduce.setText(userDTO.whyfeel);
                }
                else if(userDTO.feelstatus.equals("폭풍우가 쳐요")) {
                    feelstatus.setImageResource(R.drawable.storm);
                    username.setText(userDTO.username);
                    userintroduce.setText(userDTO.whyfeel);

                }
                else if(userDTO.feelstatus.equals("번개가 쳐요")) {
                    feelstatus.setImageResource(R.drawable.thndr);
                    username.setText(userDTO.username);
                    userintroduce.setText(userDTO.whyfeel);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                commentDTO.time = sdf.format(date);
                commentDTO.useruid = FirebaseAuth.getInstance().getUid();
                commentDTO.comment = commenttext.getText().toString();

                if(commenttext.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"빈 댓글을 입력할수 없습니다.",Toast.LENGTH_SHORT).show();
                }
                else {
                    commenttext.setText("");
                    db.collection("Users").document(detailuseruid).collection("Comments").document().set(commentDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"댓글이 등록되었습니다.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<CommentItem> commentArrayList = new ArrayList<>();
        UserDTO userDTO2 = new UserDTO();

        public CommentAdapter() {
            //댓글 좌표를 가져온다.
            /*db.collection("Users").document(detailuseruid).collection("Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("deletedebug", document.getId());
                        final CommentDTO tempCommentDTO = document.toObject(CommentDTO.class);
                        db.collection("Users").document(tempCommentDTO.useruid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                userDTO2 = task.getResult().toObject(UserDTO.class);
                                if (userDTO2.feelstatus.equals("맑아요")) {
                                    commentArrayList.add(new CommentItem(userDTO2.username, R.drawable.sun, tempCommentDTO.comment, tempCommentDTO.useruid, document.getId()));
                                    notifyDataSetChanged();
                                } else if (userDTO2.feelstatus.equals("비와요")) {
                                    commentArrayList.add(new CommentItem(userDTO2.username, R.drawable.rain, tempCommentDTO.comment, tempCommentDTO.useruid, document.getId()));
                                    notifyDataSetChanged();
                                } else if (userDTO2.feelstatus.equals("눈와요")) {
                                    commentArrayList.add(new CommentItem(userDTO2.username, R.drawable.snow, tempCommentDTO.comment, tempCommentDTO.useruid, document.getId()));
                                    notifyDataSetChanged();
                                } else if (userDTO2.feelstatus.equals("안개가 껴요")) {
                                    commentArrayList.add(new CommentItem(userDTO2.username, R.drawable.fog, tempCommentDTO.comment, tempCommentDTO.useruid,  document.getId()));
                                    notifyDataSetChanged();
                                } else if (userDTO2.feelstatus.equals("폭풍우가 쳐요")) {
                                    commentArrayList.add(new CommentItem(userDTO2.username, R.drawable.storm, tempCommentDTO.comment, tempCommentDTO.useruid,  document.getId()));
                                    notifyDataSetChanged();
                                } else if (userDTO2.feelstatus.equals("번개가 쳐요")) {
                                    commentArrayList.add(new CommentItem(userDTO2.username, R.drawable.thndr, tempCommentDTO.comment, tempCommentDTO.useruid,  document.getId()));
                                    notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            });*/

            db.collection("Users").document(detailuseruid).collection("Comments").orderBy("time", Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for (final DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                //Log.d(TAG, "New city: " + dc.getDocument().getData());
                                final CommentDTO commentDTO = dc.getDocument().toObject(CommentDTO.class);
                                db.collection("Users").document(commentDTO.useruid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        userDTO2 = task.getResult().toObject(UserDTO.class);
                                        if (userDTO2.feelstatus.equals("맑아요")) {
                                            commentArrayList.add(new CommentItem(userDTO2.username, R.drawable.sun, commentDTO.comment, commentDTO.useruid, dc.getDocument().getId()));
                                            notifyDataSetChanged();
                                        } else if (userDTO2.feelstatus.equals("비와요")) {
                                            commentArrayList.add(new CommentItem(userDTO2.username, R.drawable.rain, commentDTO.comment, commentDTO.useruid, dc.getDocument().getId()));
                                            notifyDataSetChanged();
                                        } else if (userDTO2.feelstatus.equals("눈와요")) {
                                            commentArrayList.add(new CommentItem(userDTO2.username, R.drawable.snow, commentDTO.comment, commentDTO.useruid, dc.getDocument().getId()));
                                            notifyDataSetChanged();
                                        } else if (userDTO2.feelstatus.equals("안개가 껴요")) {
                                            commentArrayList.add(new CommentItem(userDTO2.username, R.drawable.fog, commentDTO.comment, commentDTO.useruid,  dc.getDocument().getId()));
                                            notifyDataSetChanged();
                                        } else if (userDTO2.feelstatus.equals("폭풍우가 쳐요")) {
                                            commentArrayList.add(new CommentItem(userDTO2.username, R.drawable.storm, commentDTO.comment, commentDTO.useruid,  dc.getDocument().getId()));
                                            notifyDataSetChanged();
                                        } else if (userDTO2.feelstatus.equals("번개가 쳐요")) {
                                            commentArrayList.add(new CommentItem(userDTO2.username, R.drawable.thndr, commentDTO.comment, commentDTO.useruid,  dc.getDocument().getId()));
                                            notifyDataSetChanged();
                                        }
                                    }
                                });
                                break;
                            case MODIFIED:
                                //Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                break;
                            case REMOVED:
                                //Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                break;
                        }
                    }
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);

            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            MyViewHolder myViewHolder = (MyViewHolder) holder;

            myViewHolder.feelimage.setImageResource(commentArrayList.get(position).weatherimage);
            myViewHolder.username.setText(commentArrayList.get(position).username);
            myViewHolder.comment.setText(commentArrayList.get(position).comment);
            myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //삭제기능 구현
                    db.collection("Users").document(detailuseruid).collection("Comments").document(commentArrayList.get(position).deletestring).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("deletedebug", commentArrayList.get(position).deletestring);
                            Toast.makeText(getApplicationContext(),"댓글이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                            commentArrayList.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                }
            });

            //내 댓글이 아닌데 삭제가 되면 안되므로.....
            if (!commentArrayList.get(position).useruid.equals(FirebaseAuth.getInstance().getUid())) {
                myViewHolder.delete.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return commentArrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView feelimage;
        TextView comment;
        TextView username;
        Button delete;

        MyViewHolder(View view) {
            super(view);
            feelimage = view.findViewById(R.id.feelimage2);
            username = view.findViewById(R.id.introduce3);
            comment = view.findViewById(R.id.username3);
            delete = view.findViewById(R.id.deletebutton);
        }
    }
}
