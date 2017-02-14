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
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.opencsv.CSVWriter;


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
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
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
                sensorData.addX(earthAcc[0]);
                sensorData.addY(earthAcc[1]);
                sensorData.addZ(earthAcc[2]);
                sensorData.addTimestamp(timestamp);
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
                sensorData = new AccelerationData();
                accx = new ArrayList<Double>();
                accy = new ArrayList<Double>();
                accz = new ArrayList<Double>();

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


                new CountDownTimer(5000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        started = true;
                        btnAcceleration.setEnabled(false);
                        btnUpload.setEnabled(false);
                        mTextField.playSoundEffect(SoundEffectConstants.CLICK);
                        mTextField.setVisibility(View.VISIBLE);
                        mTextField.setText("TIME REMAINING: " + millisUntilFinished / 1000 + "s");

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
                openAcceleration();

                break;
            case R.id.btnAcceleration:
                btnStart.setEnabled(true);
                btnAcceleration.setEnabled(false);
                btnUpload.setEnabled(true);
                started = false;
                sensorManager.unregisterListener(this);
                layout.removeAllViews();
                openAcceleration();

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

    private void openAcceleration() {
        if (sensorData != null || sensorData.getX().size() > 0) {
            long t = sensorData.getTimestamp().get(0);

            // --------- Setup MultiRenderer ----------------------------------- //
            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
            multiRenderer.setXLabels(0);
            for (int i=1; i<100; i++){
                multiRenderer.addXTextLabel(1000*i, Integer.toString(i));
            }
            multiRenderer.setYLabels(6);
            multiRenderer.setLabelsColor(Color.BLACK);
            multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
            multiRenderer.setChartTitle("Time vs Acceleration (x,y,z)");
            multiRenderer.setXTitle("Sensor Data (s)");
            multiRenderer.setYTitle("Values of Acceleration (m.s^-2)");
            multiRenderer.setShowCustomTextGrid(true);
            multiRenderer.setLegendTextSize(20);
            multiRenderer.setLabelsTextSize(20);
            multiRenderer.setAxisTitleTextSize(20);
            multiRenderer.setChartTitleTextSize(40);
            multiRenderer.setFitLegend(true);
            multiRenderer.setLegendHeight(20);
            multiRenderer.setZoomButtonsVisible(false);
            multiRenderer.setXAxisMax(5000);
            multiRenderer.setYAxisMax(5);
            multiRenderer.setYLabelsColor(0, Color.BLACK);
            multiRenderer.setXLabelsColor(Color.BLACK);
            multiRenderer.setScale(3);
            // --------- End Setup MultiRenderer --------------------------------//


            // --------- Raw Acceleration Data ---------------------------------//
            XYSeries xSeries = new XYSeries("X");
            XYSeries ySeries = new XYSeries("Y");
            XYSeries zSeries = new XYSeries("Z");

            for (int i = 0; i < sensorData.getX().size();i++){
                xSeries.add(sensorData.getTimestamp().get(i) - t, sensorData.getX().get(i));
                ySeries.add(sensorData.getTimestamp().get(i) - t, sensorData.getY().get(i));
                zSeries.add(sensorData.getTimestamp().get(i) - t, sensorData.getZ().get(i));
            }


//            ArrayList<Double> angles = new ArrayList<Double>();
//            for (int i = 0; i < sensorData.getX().size();i++){
//                angles.add(Calculate.calculateAngle(i,sensorData.getX(), sensorData.getY()));
//            }
//            for (int i = 0; i < sensorData.getX().size();i++){
//                System.out.println(angles.get(i));
//            }


            dataset.addSeries(xSeries);
            dataset.addSeries(ySeries);
            dataset.addSeries(zSeries);

            XYSeriesRenderer xRenderer = new XYSeriesRenderer();
            xRenderer.setColor(Color.RED);
            xRenderer.setPointStyle(PointStyle.POINT);
            xRenderer.setFillPoints(true);
            xRenderer.setLineWidth(1);
            xRenderer.setDisplayChartValues(false);

            XYSeriesRenderer yRenderer = new XYSeriesRenderer();
            yRenderer.setColor(Color.GREEN);
            yRenderer.setPointStyle(PointStyle.POINT);
            yRenderer.setFillPoints(true);
            yRenderer.setLineWidth(1);
            yRenderer.setDisplayChartValues(false);

            XYSeriesRenderer zRenderer = new XYSeriesRenderer();
            zRenderer.setColor(Color.BLUE);
            zRenderer.setPointStyle(PointStyle.POINT);
            zRenderer.setFillPoints(true);
            zRenderer.setLineWidth(1);
            zRenderer.setDisplayChartValues(false);

            multiRenderer.addSeriesRenderer(xRenderer);
            multiRenderer.addSeriesRenderer(yRenderer);
            multiRenderer.addSeriesRenderer(zRenderer);
            // --------- Raw Acceleration Data ---------------------------------//


            // --------- Smooth Acceleration Data -----------------------------//
            XYSeries accxSeries = new XYSeries("accxAverage");
            XYSeries accySeries = new XYSeries("accyAverage");
            XYSeries acczSeries = new XYSeries("acczAverage");

            accx = TestAlgorithms.calculateAverage(sensorData.getX());
            accy = TestAlgorithms.calculateAverage(sensorData.getY());
            accz = TestAlgorithms.calculateAverage(sensorData.getZ());


            for (int i = 0;i < sensorData.getX().size();i++){
                accxSeries.add(sensorData.getTimestamp().get(i) -t, accx.get(i));
                accySeries.add(sensorData.getTimestamp().get(i) -t, accy.get(i));
                acczSeries.add(sensorData.getTimestamp().get(i) -t, accz.get(i));
            }


            dataset.addSeries(accxSeries);
            dataset.addSeries(accySeries);
            dataset.addSeries(acczSeries);


            XYSeriesRenderer accxRenderer = new XYSeriesRenderer();
            accxRenderer.setColor(Color.BLACK);
            accxRenderer.setPointStyle(PointStyle.POINT);
            accxRenderer.setFillPoints(true);
            accxRenderer.setLineWidth(1);
            accxRenderer.setDisplayChartValues(false);

            XYSeriesRenderer accyRenderer = new XYSeriesRenderer();
            accyRenderer.setColor(Color.CYAN);
            accyRenderer.setPointStyle(PointStyle.POINT);
            accyRenderer.setFillPoints(true);
            accyRenderer.setLineWidth(1);
            accyRenderer.setDisplayChartValues(false);

            XYSeriesRenderer acczRenderer = new XYSeriesRenderer();
            acczRenderer.setColor(Color.MAGENTA);
            acczRenderer.setPointStyle(PointStyle.POINT);
            acczRenderer.setFillPoints(true);
            acczRenderer.setLineWidth(1);
            acczRenderer.setDisplayChartValues(false);

            multiRenderer.addSeriesRenderer(accxRenderer);
            multiRenderer.addSeriesRenderer(accyRenderer);
            multiRenderer.addSeriesRenderer(acczRenderer);
            // --------- Smooth Acceleration Data -----------------------------//


            // --------- Plotting the graphs ---------------------------------//
            mChart = ChartFactory.getLineChartView(getBaseContext(), dataset,
                    multiRenderer);

            layout.addView(mChart);
            // --------- End Plotting the graphs -----------------------------//
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void openComparison() {
        if (sensorData != null || sensorData.getX().size() > 0) {
            // --------- Setup MultiRenderer ----------------------------------- //
            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
            multiRenderer.setXLabels(6);

            multiRenderer.setYLabels(6);
            multiRenderer.setLabelsColor(Color.BLACK);
            multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
            multiRenderer.setChartTitle("x Acceleration vs y Acceleration");
            multiRenderer.setXTitle("x Acceleration (m.s^-2)");
            multiRenderer.setYTitle("y Acceleration (m.s^-2)");
            multiRenderer.setShowCustomTextGrid(true);
            multiRenderer.setLegendTextSize(20);
            multiRenderer.setLabelsTextSize(20);
            multiRenderer.setAxisTitleTextSize(20);
            multiRenderer.setChartTitleTextSize(40);
            multiRenderer.setFitLegend(true);
            multiRenderer.setLegendHeight(20);
            multiRenderer.setZoomButtonsVisible(false);
            multiRenderer.setXAxisMax(5);
            multiRenderer.setYAxisMax(5);
            multiRenderer.setYLabelsColor(0, Color.BLACK);
            multiRenderer.setXLabelsColor(Color.BLACK);
            multiRenderer.setScale(3);
            multiRenderer.setPointSize(40);

            // --------- End Setup MultiRenderer --------------------------------//

            // --------- Raw Acceleration Data ---------------------------------//
            XYSeries xySeries = new XYSeries("raw x-y");


            for (int i = 0; i < sensorData.getX().size(); i++) {
                xySeries.add(sensorData.getX().get(i), sensorData.getY().get(i));
            }

            dataset.addSeries(xySeries);

            XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
            xyRenderer.setColor(Color.RED);
            xyRenderer.setPointStyle(PointStyle.POINT);
            xyRenderer.setFillPoints(true);
            xyRenderer.setLineWidth(1);
            xyRenderer.setDisplayChartValues(false);


            multiRenderer.addSeriesRenderer(xyRenderer);
            // --------- Raw Acceleration Data ---------------------------------//


            // --------- Smooth Acceleration Data -----------------------------//
            XYSeries accxySeries = new XYSeries("accxAverage");

            accx = new ArrayList<Double>();
            accy = new ArrayList<Double>();
            accx = TestAlgorithms.calculateAverage(sensorData.getX());
            accy = TestAlgorithms.calculateAverage(sensorData.getY());


            for (int i = 0; i < sensorData.getX().size(); i++) {
                accxySeries.add(accx.get(i), accy.get(i));
            }


            dataset.addSeries(accxySeries);

            XYSeriesRenderer accxyRenderer = new XYSeriesRenderer();
            accxyRenderer.setColor(Color.BLACK);
            accxyRenderer.setPointStyle(PointStyle.POINT);
            accxyRenderer.setFillPoints(true);
            accxyRenderer.setLineWidth(1);
            accxyRenderer.setDisplayChartValues(false);


            multiRenderer.addSeriesRenderer(accxyRenderer);
            // --------- Smooth Acceleration Data -----------------------------//

            // --------- Plotting the graphs ---------------------------------//
//            mChart = ChartFactory.getLineChartView(getBaseContext(), dataset,
//                    multiRenderer);
//
//            layout.addView(mChart);
            // --------- End Plotting the graphs -----------------------------//


            // --------- Plotting the drawing ---------------------------------//
            ArrayList<Float> points = new ArrayList<Float>();
            for(int i = 0; i < accx.size()-1; i+=2){
                points.add(Float.parseFloat(Double.toString(accx.get(i))));
                points.add(Float.parseFloat(Double.toString(accy.get(i))));
                points.add(Float.parseFloat(Double.toString(accx.get(i+1))));
                points.add(Float.parseFloat(Double.toString(accy.get(i+1))));
            }

            float res[] = new float[accx.size()*2];
//            for(int i = 0; i<points.size();i++){
//                res[i] = points.get(i);
//                System.out.println(res[i]);
//            }


            // --------- End Plotting the drawing -----------------------------//
        }
    }
    private void saveDataToCSV() throws IOException {
        // --------- Reinitialize smoothing ---------------------//
        accx = TestAlgorithms.calculateAverage(sensorData.getX());
        accy = TestAlgorithms.calculateAverage(sensorData.getY());
        accz = TestAlgorithms.calculateAverage(sensorData.getZ());
        // --------- End reinitialize ---------------------------//

        // --------- Setup Writing ------------------------------//
        String baseDir = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy-HH:mm:ss");
        String fileName = "AccelData-" + dateFormat.format(date) + ".csv";

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

        for (int i = 0; i < sensorData.getX().size(); i++){
            String[] entry = {
                    Double.toString(sensorData.getX().get(i)),
                    Double.toString(sensorData.getY().get(i)),
                    Double.toString(sensorData.getZ().get(i)),
                    Double.toString(accx.get(i)),
                    Double.toString(accy.get(i)),
                    Double.toString(accz.get(i)),
                    Double.toString(sensorData.getTimestamp().get(i)- sensorData.getTimestamp().get(0))};
            writer.writeNext(entry);
        }

        writer.close();
        // --------- Setup Writing ------------------------------//
    }
}
