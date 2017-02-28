package p5e610.balance;

import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.opencsv.CSVWriter;

import p5e610.graphview.GraphView;
import p5e610.graphview.LegendRenderer;
import p5e610.graphview.series.DataPoint;
import p5e610.graphview.series.LineGraphSeries;
import p5e610.graphview.series.Series;
import p5e610.balance.AccelerationData.Coordinate;

public class TestActivity extends Activity implements SensorEventListener, OnClickListener {
    NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private SensorManager sensorManager;
    private Button btnStart, btnAcceleration, btnUpload, btnStop;
    private TextView mTextView;
    private EditText mTextField;
    private EditText mPrepField;
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

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
        graph = (GraphView) findViewById(R.id.graph);
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
        if (sensorData == null || sensorData.size() == 0) {
            btnUpload.setEnabled(false);
        }
        layout.removeAllViews();
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
                layout.addView(mTextField);
                layout.addView(btnStop);
                btnStart.setEnabled(false);
                btnAcceleration.setEnabled(true);
                btnUpload.setEnabled(true);
                btnStop.setVisibility(View.INVISIBLE);
                btnStop.setEnabled(false);
                sensorData = new AccelerationData();
                accx = new ArrayList<Double>();
                accy = new ArrayList<Double>();
                accz = new ArrayList<Double>();

                graph.removeAllSeries();
                Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);

                Sensor grav = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
                sensorManager.registerListener(this, grav, SensorManager.SENSOR_DELAY_GAME);

                Sensor mag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                sensorManager.registerListener(this, mag, SensorManager.SENSOR_DELAY_GAME);

                // ----------- FUTURE WORK -------------- //
//                layout.addView(mTextView);
//                mTextField.setVisibility(View.VISIBLE);
                // ----------- FUTURE WORK -------------- //


                new CountDownTimer(20000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        started = true;
                        btnAcceleration.setEnabled(false);
                        btnUpload.setEnabled(false);
                        mTextField.playSoundEffect(SoundEffectConstants.CLICK);
                        mTextField.setVisibility(View.VISIBLE);
                        mTextField.setText("TIME REMAINING: " + millisUntilFinished / 1000 + "s");
                        mTextField.setTextSize(24);
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
                        btnStart.setEnabled(true);
                        btnUpload.setEnabled(true);
                        btnAcceleration.setEnabled(true);
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
                btnAcceleration.setEnabled(false);
                seeGraph();

                break;
            case R.id.btnAcceleration:
                btnStart.setEnabled(true);
                btnAcceleration.setEnabled(false);
                btnUpload.setEnabled(true);
                started = false;
                sensorManager.unregisterListener(this);
                layout.removeAllViews();
                seeGraph();

                break;
            case R.id.btnUpload:
                btnStart.setEnabled(true);
                btnAcceleration.setEnabled(true);
                btnUpload.setEnabled(false);
                started = false;
                sensorManager.unregisterListener(this);
                try {
                    saveDataToCSV();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
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

        double x0 = EllipseConstruction.mean(accx);
        double y0 = EllipseConstruction.mean(accy);
        double theta = EllipseConstruction.angle(accx,accy);
        double a = EllipseConstruction.eigenvalues(accx,accy)[0];
        double b = EllipseConstruction.eigenvalues(accx,accy)[1];
        for (int i = 0; i<100;i++){
            t.add(2.0*Math.PI*i/100.0);
            x.add(x0 + a*Math.cos(t.get(i))*Math.cos(theta) - b*Math.sin(t.get(i))*Math.sin(theta));
            y.add(y0 + a*Math.cos(t.get(i))*Math.sin(theta) - b*Math.sin(t.get(i))*Math.cos(theta));
        }

        DataPoint[] ellipseList = new DataPoint[t.size()];
        for (int i = 0; i < t.size(); i++){
            ellipseList[i] = new DataPoint(x.get(i), y.get(i));
        }

        LineGraphSeries<DataPoint> ellipseSeries = new LineGraphSeries<>(ellipseList);
        ellipseSeries.setColor(Color.red(1));
        graph.addSeries(ellipseSeries);
        //Ellipse -----


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dpList);
        graph.addSeries(series);
        graph.getViewport().setMinX(minx);
        graph.getViewport().setMaxX(maxx);
        graph.getViewport().setMinY(miny);
        graph.getViewport().setMaxY(maxy);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.setTitle("Graph of x vs y acceleration (ms^-2)");
        layout.addView(graph);
    }
    private void saveDataToCSV() throws IOException {
        // --------- Reinitialize smoothing ---------------------//
        accx = sensorData.getAccX();
        accy = sensorData.getAccY();
        accz = sensorData.getAccZ();
        // --------- End reinitialize ---------------------------//

        // --------- Setup Writing ------------------------------//
        String baseDir = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy-HH:mm:ss");
        String fileName = "AccelData-" + dateFormat.format(date) + ".csv";

//        String folder_name = "Balance Data";
//        File folder = new File(baseDir, folder_name);

        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath);
        CSVWriter writer;

        if (f.exists() && !f.isDirectory()){
            f.delete();
            f = new File(filePath);
            writer = new CSVWriter(new FileWriter(filePath));
        } else {
            writer = new CSVWriter(new FileWriter(filePath));
        }

        String[] data = "X,Y,Z, SMOOTHX, SMOOTHY, SMOOTHZ, t".split(",");
        writer.writeNext(data);

        for (int i = 0; i < sensorData.size(); i++){
            String[] entry = {
                    Double.toString(sensorData.get(i).getX()),
                    Double.toString(sensorData.get(i).getY()),
                    Double.toString(sensorData.get(i).getZ()),
                    Double.toString(accx.get(i)),
                    Double.toString(accy.get(i)),
                    Double.toString(accz.get(i)),
                    Double.toString(sensorData.get(i).getTimestamp() - sensorData.get(0).getTimestamp())};
            writer.writeNext(entry);
        }

        writer.close();
        // --------- Setup Writing ------------------------------//
    }
}
