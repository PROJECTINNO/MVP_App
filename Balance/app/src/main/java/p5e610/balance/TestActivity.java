package p5e610.balance;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.Manifest;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import org.achartengine.GraphicalView;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.opencsv.CSVWriter;

import p5e610.graphview.GraphView;
import p5e610.graphview.series.DataPoint;
import p5e610.graphview.series.LineGraphSeries;
import p5e610.user.AccountHandler;
import p5e610.user.Upload;

import static java.util.Collections.max;

public class TestActivity extends Activity implements SensorEventListener, OnClickListener {
    NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private SensorManager sensorManager;
    private Button btnStart, btnAcceleration, btnUpload, btnStop, btnData, btnReturn;
    private TextView mTextView;
    private EditText mTextField;
    private EditText mPrepField;
    private TextView dataField;
    private ProgressBar mProgressbar;
    private boolean started = false;
    private RelativeLayout layout;
    private AudioManager audioManager;
    float[] mGravf = null;
    float[] mMagnetf = null;
    private GraphicalView mChart;
    private AccelerationData sensorData;
    private ArrayList<Double> accx;
    private ArrayList<Double> accy;
    private ArrayList<Double> accz;
    private ArrayList<Double> vel_x;
    private ArrayList<Double> vel_y;
    private ArrayList<Double> vel_z;
    private ArrayList<Double> treated_data_x;
    private ArrayList<Double> treated_data_y;
    private ArrayList<Double> treated_data_z;
    private Canvas canvas = new Canvas();
    private Paint paint = new Paint();
    private GraphView graph;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    int duration = 21000;

//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test);
        notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        layout = (RelativeLayout) findViewById(R.id.chart_container);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorData = new AccelerationData();
        verifyStoragePermissions(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
        graph = (GraphView) findViewById(R.id.graph);
        mPrepField = (EditText) findViewById((R.id.prepText));
        mTextField = (EditText) findViewById(R.id.editText);
        dataField = (TextView) findViewById(R.id.dataText);
        mTextField.setEnabled(false);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnAcceleration = (Button) findViewById(R.id.btnAcceleration);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnData = (Button) findViewById(R.id.btnData);
        btnReturn.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnAcceleration.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnStart.setEnabled(true);
        btnData.setOnClickListener(this);
        btnAcceleration.setEnabled(false);
        btnData.setEnabled(false);

        mProgressbar = (ProgressBar) findViewById(R.id.progressbar);
        mProgressbar.setProgress(0);
        mProgressbar.setMax(duration/1000);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (sensorData == null || sensorData.size() == 0) {
            btnUpload.setEnabled(false);
        }
        layout.removeAllViews();
        layout.addView(btnReturn);

    }

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


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (started == true) {
            sensorManager.unregisterListener(this);
        }

        mTextField.playSoundEffect(0);
    }

    public void onBackPressed() {
        mTextField.playSoundEffect(0);

        MediaPlayer mp = new MediaPlayer();
        mp.stop();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }


    @Override
    public void onSensorChanged(SensorEvent event) {

        // Gets the value of the sensor that has been changed
        if (started) {
            if ((mGravf != null) && (mMagnetf != null) && (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)) {
//            if (event.sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION){
                long timestamp = System.currentTimeMillis();
                float[] deviceRelativeAcceleration = new float[4];
                deviceRelativeAcceleration[0] = event.values[0];
                deviceRelativeAcceleration[1] = event.values[1];
                deviceRelativeAcceleration[2] = event.values[2];
                deviceRelativeAcceleration[3] = 0;


                // ----- If we want relative acceleration -------- //
//                sensorData.addX(deviceRelativeAcceleration[0]);
//                sensorData.addY(deviceRelativeAcceleration[1]);
//                sensorData.addZ(deviceRelativeAcceleration[2]);
//                sensorData.addTimestamp(timestamp);
                // ----- If we want relative acceleration -------- //

                // ----- If we want absolute acceleration -------- //
                float[] R = new float[16], I = new float[16], earthAcc = new float[16];
                SensorManager.getRotationMatrix(R, I, mGravf, mMagnetf);
                float[] inv = new float[16];
                android.opengl.Matrix.invertM(inv, 0, R, 0);
                android.opengl.Matrix.multiplyMV(earthAcc, 0, inv, 0, deviceRelativeAcceleration, 0);

                sensorData.add(
                        (double)earthAcc[0],
                        (double)earthAcc[1],
                        (double)earthAcc[2],
                        timestamp);

                // ----- If we want absolute acceleration -------- //
            }
            else if (event.sensor.getType() == Sensor.TYPE_GRAVITY){
                mGravf = event.values;
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                mMagnetf = event.values;
            }
        }
    }

    private TextView CreateNewTextView(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText(text);
        return textView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                layout.removeAllViews();
                layout.addView(btnReturn);
                layout.addView(mTextField);
                layout.addView(btnStop);
                layout.addView(mProgressbar);
                btnStart.setEnabled(false);
                btnAcceleration.setEnabled(true);
                btnUpload.setEnabled(true);
                btnStop.setVisibility(View.INVISIBLE);
                btnStop.setEnabled(false);
                btnData.setEnabled(true);
                btnReturn.setEnabled(true);
                sensorData = new AccelerationData();
                accx = new ArrayList<Double>();
                accy = new ArrayList<Double>();
                accz = new ArrayList<Double>();

                graph.removeAllSeries();
                Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                sensorManager.registerListener(this, accel, 10000);

                Sensor grav = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
                sensorManager.registerListener(this, grav, 10000);

                Sensor mag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                sensorManager.registerListener(this, mag, 10000);

                // ----------- FUTURE WORK -------------- //
//                layout.addView(mTextView);
//                mTextField.setVisibility(View.VISIBLE);
                // ----------- FUTURE WORK -------------- //



                new CountDownTimer(duration, 1000) {

                    public void onTick(long millisUntilFinished) {
                        started = true;
                        btnAcceleration.setEnabled(false);
                        btnUpload.setEnabled(false);
                        btnData.setEnabled(false);
                        mTextField.playSoundEffect(SoundEffectConstants.CLICK);
                        mTextField.setVisibility(View.VISIBLE);
                        mProgressbar.setVisibility(View.VISIBLE);
                        mTextField.setText(""+millisUntilFinished / 1000);
                        mTextField.setTextSize(30);

                        mProgressbar.setProgress((int) millisUntilFinished / 1000 );
                    }

                    public void onFinish() {
                        try {
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            r.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mTextField.setText("DONE!");
                        btnStop.setVisibility(View.VISIBLE);
                        btnStop.setEnabled(true);
                         btnUpload.setEnabled(true);
                        btnAcceleration.setEnabled(true);
                        btnData.setEnabled(true);
                        mProgressbar.setProgress(0);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                // this code will be executed after 0.5 seconds
                                started = false;
                            }
                        }, 500);
                    }
                }.start();
                break;

            case R.id.btnStop:
                sensorManager.unregisterListener(this);
                layout.removeAllViews();
                layout.addView(btnReturn);
                btnAcceleration.setEnabled(false);
                btnData.setEnabled(true);
                btnReturn.setEnabled(true);
                btnStart.setEnabled(true);
                seeGraph();

                break;
            case R.id.btnAcceleration:
                btnStart.setEnabled(true);
                btnAcceleration.setEnabled(false);
                btnData.setEnabled(true);
                btnUpload.setEnabled(true);
                btnReturn.setEnabled(true);
                started = false;
                sensorManager.unregisterListener(this);
                layout.removeAllViews();
                layout.addView(btnReturn);
                seeGraph();

                break;
            case R.id.btnUpload:
                btnStart.setEnabled(true);
                btnAcceleration.setEnabled(true);
                btnData.setEnabled(true);
                btnUpload.setEnabled(false);
                btnReturn.setEnabled(true);
                started = false;
                sensorManager.unregisterListener(this);
                try {
                    saveDataToCSV();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnData:
                layout.removeAllViews();
                layout.addView(btnReturn);
                graph.removeAllSeries();
                btnStart.setEnabled(true);
                btnAcceleration.setEnabled(true);
                btnReturn.setEnabled(true);
                btnData.setEnabled(false);
                btnUpload.setEnabled(false);
                started = false;
                sensorManager.unregisterListener(this);
                dataField.setText(computeData());
                dataField.setVisibility(View.VISIBLE);
                layout.addView(dataField);

                break;

            case R.id.btnReturn:
                AccountHandler.setReturnUserActivityFromTestActivity(true);
                Intent registerIntent = new Intent(TestActivity.this, UserActivity.class);
                TestActivity.this.startActivity(registerIntent);
                finish();
            default:
                break;
        }
    }

    private void startTest(){

    }


    /**
     * This function enables to visualize the graph with graphview
     */
    private void seeGraph(){
        graph.removeAllSeries();
        accx = sensorData.getAccX();
        accy = sensorData.getAccY();
        DataPoint[] dpList = new DataPoint[accx.size()];
        double[] dpList_test_x = new double[accx.size()];
        double[] dpList_test_y = new double[accx.size()];
        for (int i = 0; i < accx.size(); i++){
            dpList[i] = new DataPoint(accx.get(i), accy.get(i));
            dpList_test_x[i] = accx.get(i);
            dpList_test_y[i] = accy.get(i);
        }

        // Tests ---
        Arrays.sort(dpList_test_x);
        Arrays.sort(dpList_test_y);

        double minx = dpList_test_x[0];
        double miny = dpList_test_y[0];
        double maxx = dpList_test_x[dpList_test_x.length-1];
        double maxy = dpList_test_y[dpList_test_y.length-1];


        System.out.println("Min x: " + minx);
        System.out.println("Max x: " + maxx);
        System.out.println("Min y: " + miny);
        System.out.println("Max y: " + maxy);
        // ---- Tests


        // ---- Ellipse
        //

        ArrayList<Double> t = new ArrayList<Double>(); //parametric angle
        ArrayList<Double> x = new ArrayList<Double>(); // absisse
        ArrayList<Double> y = new ArrayList<Double>(); //ordonnée

        double x0 = AccelerationData.mean(accx);
        double y0 = AccelerationData.mean(accy);
        double theta = AccelerationData.angle(accx,accy);
        double lambda0 = AccelerationData.eigenvalues(accx,accy)[0];
        double lambda1 = AccelerationData.eigenvalues(accx,accy)[1];
        double a = Math.sqrt(5.991)*Math.sqrt(lambda0/(accx.size()-1));
        double b = Math.sqrt(5.991)*Math.sqrt(lambda1/(accx.size()-1));
        System.out.println("axis : " + a + "  " + b);
        for (int i = 0; i<=100;i++){
            t.add(2.0*Math.PI*i/100.0);
            x.add(x0 + a*Math.cos(t.get(i))*Math.cos(theta) - b*Math.sin(t.get(i))*Math.sin(theta));
            y.add(y0 + a*Math.cos(t.get(i))*Math.sin(theta) + b*Math.sin(t.get(i))*Math.cos(theta));
        }
        System.out.println("points : " + max(y));

        DataPoint[] ellipseList = new DataPoint[t.size()];
        for (int i = 0; i < t.size(); i++){
            ellipseList[i] = new DataPoint(x.get(i), y.get(i));
        }

        LineGraphSeries<DataPoint> ellipseSeries = new LineGraphSeries<>(ellipseList);
        ellipseSeries.setColor(Color.RED);
        ellipseSeries.setThickness(2);

        //Ellipse -----

        // ---- Major axis of ellipse
        //
        ArrayList<Double> t2 = new ArrayList<Double>(); //parametric angle
        ArrayList<Double> x2 = new ArrayList<Double>(); // absisse
        ArrayList<Double> y2 = new ArrayList<Double>(); //ordonnée

        double e0 = AccelerationData.mainDirection(accx,accy)[0];
        double e1 = AccelerationData.mainDirection(accx,accy)[1];

        for (int i = 0; i<=100;i++){
            t2.add(-10.0 + 1.0*i/5.0);
            x2.add(x0 + e0*t2.get(i));
            y2.add(y0 + e1*t2.get(i));
            System.out.println("t2 = " + t2.get(i));
        }

        DataPoint[] axisList = new DataPoint[t2.size()];
        for (int i = 0; i < t2.size(); i++){
            axisList[i] = new DataPoint(x2.get(i), y2.get(i));
        }

        LineGraphSeries<DataPoint> axisSeries = new LineGraphSeries<>(axisList);
        axisSeries.setColor(Color.RED);
        axisSeries.setThickness(5);

        //Major Axis -----


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dpList);
        series.setThickness(2);

        graph.addSeries(ellipseSeries);
        graph.addSeries(series);
        graph.addSeries(axisSeries);

        //Setting the scale ---------------
        //Should be changed regarding to the mean data : what is the appropriate scale
        // for a good person and what is it for an unbalanced one ?
        if(maxx < 2.0 && minx > -2.0) {
            graph.getViewport().setMinX(-2.0);
            graph.getViewport().setMaxX(2.0);}
        else {
            graph.getViewport().setMinX(-4.0);
            graph.getViewport().setMaxX(4.0);}

        if(maxy < 3.5 && miny > -3.5) {
            graph.getViewport().setMinY(-3.5);
            graph.getViewport().setMaxY(3.5);}
        else {
            graph.getViewport().setMinY(-6.0);
            graph.getViewport().setMaxY(6.0);}

        //---------------Setting the scale




        graph.getViewport().setScalable(false);
        graph.getViewport().setScalableY(false);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.setTitle("Graph of x vs y acceleration (ms^-2)");
        layout.addView(graph);
    }

    private String computeData(){
        accx = sensorData.getAccX();
        accy = sensorData.getAccY();

        double x0 = AccelerationData.mean(accx);
        double y0 = AccelerationData.mean(accy);
        double theta = AccelerationData.angle(accx,accy);
        double lambda0 = AccelerationData.eigenvalues(accx,accy)[0];
        double lambda1 = AccelerationData.eigenvalues(accx,accy)[1];
        double a = Math.sqrt(5.991)*Math.sqrt(lambda0/(accx.size()-1));
        double b = Math.sqrt(5.991)*Math.sqrt(lambda1/(accx.size()-1));
        double rangeX = AccelerationData.range(accx);
        double rangeY = AccelerationData.range(accy);
        Double[] zeros = {AccelerationData.zeros(accx),AccelerationData.zeros(accy)};
        double modulus = AccelerationData.maximumModulus(accx,accy);
        double per = AccelerationData.percentage(accx,accy,a,b,theta,1.0);

        return "mean x  = " + x0 +"\n" +
                "mean y  = " + y0 +"\n" +
                "angle  = " + Math.toDegrees(theta) +"\n" +
                "major half-axis  = " + a +"\n" +
                "minor half-axis  = " + b +"\n" +
                "range x  = " + rangeX +"\n" +
                "range y  = " + rangeY +"\n" +
                "number of zeroes x  = " + zeros[0] +"\n" +
                "number of zeroes y  = " + zeros[1] +"\n" +
                "maximum modulus  = " + modulus +"\n" +
                "percentage = " + per;

    }

    private void clearCache(){
        String baseDir = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String folderPath = baseDir + File.pathSeparator + "BalanceApp";
        File dir = new File(folderPath);
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDataToCSV() throws IOException {
        // --------- Reinitialize smoothing ---------------------//
        accx = sensorData.getAccX();
        accy = sensorData.getAccY();
        accz = sensorData.getAccZ();
        ArrayList<Long> timestamp = sensorData.getTimestamp();
        // --------- End reinitialize ---------------------------//



        // --------- Setup Writing ------------------------------//
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy-HH:mm:ss");
        String fileName = "AccelData-" + dateFormat.format(date) + ".csv";
        File f = new File(this.getFilesDir(), fileName);
        CSVWriter writer = new CSVWriter(new FileWriter(f));



//        String baseDir = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//        Date date = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy-HH:mm:ss");
//        String fileName = "AccelData-" + dateFormat.format(date) + ".csv";
//
////        String folder_name = "Balance Data";
////        File folder = new File(baseDir, folder_name);
//
//        String filePath = baseDir + File.pathSeparator + "BalanceApp" + File.separator + fileName;
//        File f = new File(filePath);
//        CSVWriter writer;
//
//        if (f.exists() && !f.isDirectory()){
//            f.delete();
//            f = new File(filePath);
//            writer = new CSVWriter(new FileWriter(filePath));
//        } else {
//            writer = new CSVWriter(new FileWriter(filePath));
//        }

        String[] data = "X,Y,Z,TIME".split(",");
        writer.writeNext(data);

//        String[] data = "X,Y,Z, SMOOTHX, SMOOTHY, SMOOTHZ, t".split(",");
//        writer.writeNext(data);

        for (int i = 0; i < accx.size(); i++){
            String[] entry = {
                    Double.toString(accx.get(i)),
                    Double.toString(accy.get(i)),
                    Double.toString(accz.get(i)),
                    Double.toString(timestamp.get(i) - timestamp.get(0))};
            writer.writeNext(entry);
        }

        writer.close();
        // --------- Setup Writing ------------------------------//

        String uid = user.getUid();
        Uri file = Uri.fromFile(f);
        StorageReference riversRef = mStorageRef.child(uid +File.separator+ fileName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        Upload upload = new Upload(downloadUrl.toString());
                        String uploadId = mDatabase.child("users").child(user.getUid()).child("results").push().getKey();
                        mDatabase.child("results").child("users").child(user.getUid()).child(uploadId).setValue(upload);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

        System.out.println("COMES HERE BUT WHERE DOES IT GO AFTER?");
    }
}
