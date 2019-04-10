package hehehe.destroyer.lab3mobileapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import hehehe.destroyer.lab3mobileapps.R;

public class CompassActivity extends Activity implements SensorEventListener {

    private Context context = this;
    private static final String TAG = "Compass: ";
    // define the display assembly compass picture
    private ImageView image;
    // record the compass picture angle turned
    private float currentDegree = 0f;
    private float lastDegree = 0f;
    // device sensor manager
    private SensorManager mSensorManager;
    TextView tvHeading;

    //Part 5 BRIGHTNESS
    private int brightness;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private boolean InformationObtained;

    //Part 6 SOS
    public static Camera cam = null;

    //Part 1 and 2 Individual
    private float xValuee;
    private float yValuee;
    private float zValuee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass_activity);

        // our compass image
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.tvHeading);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        /*
        try{
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

            brightness = System.g(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        }
        catch(Settings.SettingNotFoundException e){
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }*/
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        xValuee = 0;
        yValuee = 0;
        zValuee = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }
    public void OpenMainActivity(){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        if(Math.abs(lastDegree - degree) >= 1){

            Log.e(TAG, " " +degree);
            tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");
            // create a rotation animation (reverse turn degree degrees)
            RotateAnimation ra = new RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);
            // how long the animation will take place
            ra.setDuration(210);
            // set the animation after the end of the reservation status
            ra.setFillAfter(true);
            // Start the animation
            image.startAnimation(ra);
            currentDegree = -degree;
            //Individual Part 5 Brightness
            //xValuee = event.values[0];
            yValuee = event.values[1];
            //zValuee = event.values[2];
            Log.e(TAG, " " +degree +"Y: " +yValuee);
            //If phone is pointing north, turn on the camera activity
            if(degree == 0) {
                OpenMainActivity();
            }
            //Brightness changes at 30 degress, not 0 Because 0 is North and it turns on the main activity
            if(degree == 30){
                //If phone's orientation is around 90 degrees acording to the Y axis
                if(yValuee < -45f){
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.screenBrightness = 1f;
                    getWindow().setAttributes(lp);
                }else{
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.screenBrightness = 0.2f;
                    getWindow().setAttributes(lp);
                }
            }
            //Individual work Part 6 SOS flashes
            if(degree == 180){
                if(yValuee < -45f){
                    String sos = "...---...";
                    char[] array = sos.toCharArray();
                    cam = Camera.open();
                    Camera.Parameters p = cam.getParameters();
                    for(int i = 0; i < array.length; i++) {
                        //Signalu tipai
                        if (array[i] == '.') {
                            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            cam.setParameters(p);
                            cam.startPreview();
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if(array[i] == '-'){
                            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            cam.setParameters(p);
                            cam.startPreview();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else if(array[i] == ' '){
                            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            cam.setParameters(p);
                            cam.startPreview();
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else if(array[i] == '/'){
                            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            cam.setParameters(p);
                            cam.startPreview();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        //Tarpai tarp signalu
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        cam.setParameters(p);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //Pabaigus darba isjungiam
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    cam.setParameters(p);
                }
            }
        }
        lastDegree = degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}
