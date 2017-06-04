package shantanu.fireapp;

import android.app.ProgressDialog;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class AudioRecorder extends AppCompatActivity {

    private ImageButton bRecord;
    private TextView recordLabel;
    private MediaRecorder mRecorder;
    private String mFileName;
    private String LOG_TAG = "RECORDER";
    private StorageReference storageReference = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        storageReference = FirebaseStorage.getInstance().getReference();
        bRecord = (ImageButton) findViewById(R.id.bRecord);
        recordLabel = (TextView) findViewById(R.id.tvRecordLabel);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_audio.3gp";

        bRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();
                    recordLabel.setText("Recording...");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording();
                    recordLabel.setText("Tap and hold to\nRecord");
                }

                return false;
            }
        });
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        uploadAudio();
    }

    private void uploadAudio() {
        progressDialog.show();
        StorageReference filePath = storageReference.child("Audios").child("newAudio.3gp");
        Uri uri = Uri.fromFile(new File(mFileName));
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getBaseContext(), "Audio uploaded Successfully...", Toast
                        .LENGTH_SHORT).show();
                progressDialog.dismiss();
                recordLabel.setText("Uploading Done Successfully!!!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Failed...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
