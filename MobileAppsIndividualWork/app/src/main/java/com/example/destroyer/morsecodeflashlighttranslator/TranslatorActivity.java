package com.example.destroyer.morsecodeflashlighttranslator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.hardware.Camera;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class TranslatorActivity extends Activity {

    Context context = this;
    public static Camera cam = null;
    TextView translatorTitle;
    String finalMorse;

    TextView inputField;
    EditText inputText;
    Button translateButton;

    TextView translatedTextField;
    TextView translatedText;
    Button flashButton;

    //Part 3 - Camera
    private static final String TAG = "AndroidCameraApi";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static{
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private String cameraId;
    protected CameraDevice cameraDevice;
    private static final int REQUEST_CAMERA_PERMISION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translatorlayout);

        finalMorse = "";

        translatorTitle = (TextView) findViewById(R.id.translator);
        inputField = (TextView) findViewById(R.id.inputField);
        inputText = (EditText) findViewById(R.id.inputText);
        translateButton = (Button) findViewById(R.id.translateButton);
        translatedTextField = (TextView) findViewById(R.id.translatedTextField);
        translatedText = (TextView) findViewById(R.id.translatedText);
        flashButton = (Button) findViewById(R.id.flashButton);
        //OnClick Listeners
        translateButton.setOnClickListener(readText);
        flashButton.setOnClickListener(flashMorseCode);

    }

    public String translateText(String text){
        Translator translator = new Translator(text);
        translator.translate();
        String morse = translator.getMorse();
        return morse;
    }
    View.OnClickListener readText = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            String text = inputText.getText().toString();
            Log.e(TAG, "got string from box: " +text);
            finalMorse = translateText(text);
            Log.e(TAG, "translated string from box: " +finalMorse);
            //System.out.println(morse);
            translatedText.setText(finalMorse);
        }
    };

    public void flashMorse(View v){
        final View w = v;
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            openCamera();
            return;
        }
        if(finalMorse == ""){
            Toast.makeText(getBaseContext(), "Enter the text and click \"Translate\" Button", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e(TAG, " " +finalMorse);
        char[] array = finalMorse.toCharArray();
        cam = Camera.open();
        Camera.Parameters p = cam.getParameters();
        for(int i = 0; i < array.length; i++) {
            if (array[i] == '.') {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if(array[i] == '-'){
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
                try {
                    Thread.sleep(1500);
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
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                cam.setParameters(p);
                cam.startPreview();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            cam.setParameters(p);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        cam.setParameters(p);
        return;
    }

    View.OnClickListener flashMorseCode = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            flashMorse(v);
        }
    };
    public void flashLightOn(View view) {
        try {
            if (getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH)) {
                cam = Camera.open();
                Camera.Parameters p = cam.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception flashLightOn()",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void flashLightOff(View view) {
        try {
            if (getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH)) {
                cam.stopPreview();
                cam.release();
                cam = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception flashLightOff",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Stuff from labaratory work
    private void openCamera(){
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");

        try{
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(TranslatorActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
        Log.e(TAG, "open camera X");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == REQUEST_CAMERA_PERMISION){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(TranslatorActivity.this, "You cant use this app without granting permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };
/*
    public void flashMorse(View v){
        final View w = v;
        openCamera();
        if(finalMorse == ""){
            Toast.makeText(getBaseContext(), "Enter the text and click \"Translate\" Button", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e(TAG, " " +finalMorse);
        char[] array = finalMorse.toCharArray();

        for(int i = 0; i < array.length; i++){
            Log.e(TAG, " " +i +" raide: " +array[i]);
            Timer timer = new Timer();
            if(array[i] == '.'){
                flashLightOn(w);
                TimerTask timer_task = new TimerTask() {
                    public void run() {
                        flashLightOff(w);
                    }
                };
                timer.schedule(timer_task, 5000);
                timer_task.cancel();
            }else if(array[i] == '-'){
                flashLightOn(w);
                TimerTask timer_task = new TimerTask() {
                    public void run() {
                        flashLightOff(w);
                    }
                };
                timer.schedule(timer_task, 8000);
                timer_task.cancel();
            }else if(array[i] == ' '){
                TimerTask timer_task = new TimerTask() {
                    public void run() {
                        //flashLightOff(w);
                    }
                };
                timer.schedule(timer_task, 8000);
                timer_task.cancel();
            }else if(array[i] == '/'){
                TimerTask timer_task = new TimerTask() {
                    public void run() {
                        //flashLightOff(w);
                        int sass = 0;
                    }
                };
                timer.schedule(timer_task, 2000);
                timer_task.cancel();
            }
            timer.cancel();
        }
        flashLightOff(w);
    }
*/
}
