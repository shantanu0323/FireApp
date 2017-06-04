package shantanu.fireapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class ViewDatabase extends AppCompatActivity {

    private Button bView;
    private ListView listView;
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayAdapter<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database);

        init();

        bView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);
                bView.setVisibility(View.GONE);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReferenceFromUrl("https://fireapp-1dfa9.firebaseio.com/Users/user1");

        if (databaseReference == null) {
            Log.e("TAG", "NULL");
        } else {
            Log.e("TAG", "NOT NULL");
        }
        FirebaseListAdapter<String> adapter = new FirebaseListAdapter<String>(this, String.class,
                android.R.layout.simple_list_item_1, databaseReference) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) findViewById(android.R.id.text1);
                textView.setText(model);
                Log.e("TAG", "inside function");
                Log.e("TAG", "hello " + model);
            }
        };

        listView.setAdapter(adapter);

        /*
        Firebase users = new Firebase("https://fireapp-1dfa9.firebaseio.com/Users");
        users.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                String name = map.get("Name");
                String age = String.valueOf(map.get("Age"));
                String stream = map.get("Stream");
                String info = name + " is " + age + " yrs & is studying " + stream;
                usernames.add(info);
                data.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        */

    }

    private void init() {
        bView = (Button) findViewById(R.id.bView);
        listView = (ListView) findViewById(R.id.listView);
        data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usernames);
        listView.setAdapter(data);
    }
}
