package shantanu.fireapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UIActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);

        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl
                ("https://fireapp-1dfa9.firebaseio.com/Users/user1");

        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(
                this,
                String.class,
                android.R.layout.simple_list_item_1,
                databaseReference
        ) {
            @Override
            protected void populateView(View view, String model, int position) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText("" + model);
                if (flag) {
//                    Toast.makeText(getBaseContext(), "retrieved...", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "Retrieved...");
                    flag = false;
                }
            }
        };

        listView.setAdapter(firebaseListAdapter);
        listView.setEmptyView(progressBar);
    }
}
