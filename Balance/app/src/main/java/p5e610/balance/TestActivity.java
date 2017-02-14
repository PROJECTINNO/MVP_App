package p5e610.balance;


import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.media.AudioManager;

import java.util.ArrayList;

/**
 * Created by Cecile on 08/02/2017.
 */

public class TestActivity extends AppCompatActivity {

    private double duration;
    private ArrayList<Double> accx;
    private ArrayList<Double> accy;
    private ArrayList<Double> accz;
    private SensorManager sensorManager;
    private boolean started = false;
    private AccelerationData sensorData;
    private AudioManager audioManager;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        layout = (RelativeLayout) findViewById(R.id.chart_container);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorData = new AccelerationData();
        verifyStoragePermissions(this);
        accx = new ArrayList<Double>();
        accy = new ArrayList<Double>();
        accz = new ArrayList<Double>();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);

        mPrepField = (EditText) findViewById((R.id.prepText));
        mTextField = (EditText) findViewById(R.id.editText);
        mTextField.setEnabled(false);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnAcceleration = (Button) findViewById(R.id.btnAcceleration);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnStart.setOnClickListener(this);
        btnAcceleration.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnStart.setEnabled(true);
        btnAcceleration.setEnabled(false);
        if (sensorData == null || sensorData.getX().size() == 0) {
            btnUpload.setEnabled(false);
        }
    }

    //Display notification

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


}


}
