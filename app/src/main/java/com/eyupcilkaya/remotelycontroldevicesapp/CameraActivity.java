package com.eyupcilkaya.remotelycontroldevicesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CameraActivity extends AppCompatActivity {

    ImageView imageView;
    FirebaseStorage firebaseStorage;
    StorageReference storageRef;
    Button button;
    Button button2;
    Runnable runnable;
    Handler handler;
    private FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    String relay_value;
    RelayControl relayControl;
    TextView stateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
        button2.setEnabled(false);
        imageView.setImageResource(R.drawable.cam);
        firebaseAuth = FirebaseAuth.getInstance();
        relayControl = new RelayControl();
        relay_value = "";
        relay_value = relayControl.getRelay();
        stateText = findViewById(R.id.stateText);
        System.out.println(relay_value);

    }

    public void cameraOn(View view) {
        button.setEnabled(false);
        button2.setEnabled(true);
        download();
    }

    public void download() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (button2.isEnabled() == true) {

                    storageRef.child("1.jpg").getBytes(1024 * 1024)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    imageView.setImageBitmap(bitmap);
                                    handler.postDelayed(runnable, 500);
                                }
                            });
                } else {
                    imageView.setImageResource(R.drawable.cam);

                }
            }
        };
        handler.post(runnable);

    }

    public void cameraOff(View view) {

        handler.removeCallbacks(runnable);
        //imageView.setImageResource(R.drawable.cam);
        button2.setEnabled(false);
        button.setEnabled(true);

    }

    public void rolecontrol(View view) {

        relay_value = relayControl.getRelay();
        if (relay_value.equals("1")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
            builder.setTitle("Uyarı");
            builder.setMessage("Cihazı kapatmak mı istiyorsunuz ?");
            builder.setNegativeButton("Hayır", null);
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    relayControl.setRelay("0");
                    Toast.makeText(CameraActivity.this, "Cihaz Kapandı", Toast.LENGTH_SHORT).show();
                    stateText.setText("Cihaz durumu: Kapalı ");
                    relay_value = relayControl.getRelay();

                }
            });
            builder.show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
            builder.setTitle("Uyarı");
            builder.setMessage("Cihazı açmak mı istiyorsunuz ?");
            builder.setNegativeButton("Hayır", null);
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    relayControl.setRelay("1");
                    Toast.makeText(CameraActivity.this, "Cihaz Açıldı", Toast.LENGTH_SHORT).show();
                    stateText.setText("Cihaz durumu: Açık ");
                    relay_value = relayControl.getRelay();

                }
            });
            builder.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        relay_value = relayControl.getRelay();

        if (item.getItemId() == R.id.log_out) {

            firebaseAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.sensor) {

            if (relay_value.equals("0")) {

                Toast.makeText(getApplicationContext(), "Cihaz Kapalı", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(CameraActivity.this, SensorActivity.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
