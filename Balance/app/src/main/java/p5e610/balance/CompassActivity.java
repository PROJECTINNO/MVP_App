package p5e610.balance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {
    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    TextView tvHeading;
    Sensor accelerometer;
    Sensor magnetometer;
    Float azimut;
    Button btnCancel;
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_compass);
        image = (ImageView) findViewById(R.id.imageViewCompass);
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnContinue = (Button) findViewById(R.id.btnContinue);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent UserActivity = new Intent(CompassActivity.this, UserActivity.class);
                CompassActivity.this.startActivity(UserActivity);
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent TestActivity = new Intent(CompassActivity.this, TestActivity.class);
                CompassActivity.this.startActivity(TestActivity);
                finish();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
//        SensorManager.SENSOR_DELAY_UI
        mSensorManager.registerListener(this, accelerometer, 40000);
        mSensorManager.registerListener(this, magnetometer, 40000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {  }

    float[] mGravity;
    float[] mGeomagnetic;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[0]*360/(2*3.14159f); // orientation contains: azimut, pitch and roll
            }
        }

        if (azimut != null) {
            String result = String.format("%.2f", azimut);
            tvHeading.setText("North: " + result + " degrees");
            image.setRotation(currentDegree);
//            RotateAnimation ra = new RotateAnimation(
//                    currentDegree,
//                    -azimut,
//                    Animation.RELATIVE_TO_SELF, 0.5f,
//                    Animation.RELATIVE_TO_SELF,
//                    0.5f);
//
//            // how long the animation will take place
//            ra.setDuration(210);
//
//            // set the animation after the end of the reservation status
//            ra.setFillAfter(true);
//
//            // Start the animation
//            image.startAnimation(ra);
            currentDegree = -azimut;

        }
    }
}
