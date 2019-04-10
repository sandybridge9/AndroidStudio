package hehehe.destroyer.lab3mobileapps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener{
    //Part 1 - Accelerometer
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Button startAndStop;
    private TextView xValue;
    private TextView yValue;
    private TextView zValue;
    private boolean InformationObtained;

    //Part 1 and 2 Individual
    private TextView xPos;
    private TextView yPos;
    private TextView zPos;
    private float xValuee;
    private float yValuee;
    private float zValuee;

    //Compass
    private Context context = this;
    private Button compassButton;

    //Part 2 - GPS
    private TextView coordinates;
    private TextView coordinatesNetwork;
    private LocationManager locationManager;
    private Location gpsLocation;
    private Location networkLocation;

    //Part 3 - Camera
    private static final String TAG = "AndroidCameraApi";
    private Button takePictureButton;
    private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static{
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //Part 1 and 2
        InformationObtained = false;
        startAndStop = (Button) findViewById(R.id.start_and_stop);
        startAndStop.setOnClickListener(StartAndStopButtonListener);
        xValue = (TextView) findViewById(R.id.x_value);
        yValue = (TextView) findViewById(R.id.y_value);
        zValue = (TextView) findViewById(R.id.z_value);
        xPos = (TextView) findViewById(R.id.x_pos);
        yPos = (TextView) findViewById(R.id.y_pos);
        zPos = (TextView) findViewById(R.id.z_pos);

        coordinates = (TextView) findViewById(R.id.coordinates);
        coordinatesNetwork = (TextView) findViewById(R.id.coordinates_network);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLastLocation();

        xValuee = 0;
        yValuee = 0;
        zValuee = 0;

        //Compass button
        compassButton = (Button) findViewById(R.id.compass);
        compassButton.setOnClickListener(compassListener);

        //Part 3
        textureView = (TextureView) findViewById(R.id.textureView);
        assert  textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        takePictureButton = (Button) findViewById(R.id.take_photo);
        assert takePictureButton != null;
        takePictureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                takePicture();
            }
        });
    }
    View.OnClickListener StartAndStopButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){

            if(senAccelerometer == null){
                Toast.makeText(MainActivity.this, getString(R.string.no_sensor), Toast.LENGTH_LONG).show();
                return;
            }

            if(InformationObtained){
                startAndStop.setText(getString(R.string.start));
                senSensorManager.unregisterListener(MainActivity.this, senAccelerometer);
                InformationObtained = false;
            } else{
                senSensorManager.registerListener(MainActivity.this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                startAndStop.setText(getString(R.string.stop));
                InformationObtained = true;
            }
        }
    };
    public void runCompass(){
        Intent intent = new Intent(context, CompassActivity.class);
        context.startActivity(intent);
    }
    View.OnClickListener compassListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            runCompass();
        }
    };
    @Override
    public void onSensorChanged(SensorEvent event){

        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //For accuracy (Individual Task 1)
            float diffx = Math.abs(Math.abs(xValuee) - Math.abs(event.values[0]));
            float diffy = Math.abs(Math.abs(yValuee) - Math.abs(event.values[1]));
            float diffz = Math.abs(Math.abs(zValuee) - Math.abs(event.values[2]));
            float difference = (diffx + diffy + diffz) / 3;
            Log.e(TAG, " " +difference);
            if(difference > 0.5f){
                xValuee = event.values[0];
                yValuee = event.values[1];
                zValuee = event.values[2];
                xValue.setText(String.valueOf(xValuee));
                yValue.setText(String.valueOf(yValuee));
                zValue.setText(String.valueOf(zValuee));
                //Individual Task 2
                if(xValuee < 0){
                    xPos.setText("Left side up");
                }else if(xValuee > 0){
                    xPos.setText("Right side up");
                }
                if(yValuee < 0){
                    yPos.setText("Bottom side up");
                }else if(yValuee > 0){
                    yPos.setText("Top side up");
                }
                if(zValuee < 0){
                    zPos.setText("Back side up");
                }else if(zValuee > 0){
                    zPos.setText("Screen side up");
                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }
    @Override
    protected  void onPause(){
        super.onPause();
        if(senAccelerometer != null)
            senSensorManager.unregisterListener(MainActivity.this, senAccelerometer);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        this.locationManager.removeUpdates(this);
        stopBackgroundThread();

    }

    @Override
    protected  void onResume(){
        super.onResume();

        if(senAccelerometer != null && InformationObtained)
            senSensorManager.registerListener(MainActivity.this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
        startBackgroundThread();
        if(textureView.isAvailable()){
            openCamera();
        }else{
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    //Part 2 methods
    public void onLocationChanged(Location location){
        if(location != null){
            if(gpsLocation.getProvider() == location.getProvider()){
                gpsLocation = location;
                coordinates.setText(getString(R.string.latitude_text) +" " +gpsLocation.getLatitude() +" " + getString(R.string.longitude_text) +" " +gpsLocation.getLongitude());
            }else if(networkLocation.getProvider() == location.getProvider()){
                networkLocation = location;
                coordinatesNetwork.setText(getString(R.string.latitude_text) +" " +networkLocation.getLatitude() +" " + getString(R.string.longitude_text) +" " +networkLocation.getLongitude());
            }
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){
    }

    @Override
    public void onProviderEnabled(String provider){
    }

    @Override
    public void onProviderDisabled(String provider){
    }
    private void getLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        coordinates.setText(getString(R.string.latitude_text) +" " +gpsLocation.getLatitude() +" " + getString(R.string.longitude_text) +" " +gpsLocation.getLongitude());
        coordinatesNetwork.setText(getString(R.string.latitude_text) +" " +networkLocation.getLatitude() +" " + getString(R.string.longitude_text) +" " +networkLocation.getLongitude());
    }







    //Part 3 methods CAMERA STUFF BELOW
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
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

    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(MainActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
            createCameraPreview();
        }
    };

    protected void startBackgroundThread(){
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundHandler.getLooper());
    }

    protected void stopBackgroundThread(){
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    protected void takePicture(){
        if(null == cameraDevice){
            Log.e(TAG, "cameraDevice is null");
            return;
        }

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try{
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if(characteristics != null){
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if(jpegSizes != null && 0 < jpegSizes.length){
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            //Overall mode of 3A
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            //Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            //Output file
            final File file = new File(Environment.getExternalStorageDirectory()+"/pic.jpg");

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try{
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch(FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }finally {
                        if(image != null){
                            image.close();
                        }
                    }
                }
                private void save(byte[] bytes) throws IOException{
                    OutputStream output = null;
                    try{
                        //save to file
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    }finally {
                        if(null != output){
                            output.close();
                        }
                    }
                }
            };

            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

            //This callback is ivoked when a request triggers a capture to start, and when the capture is complete.
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(MainActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }
            };

            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(CameraCaptureSession session){
                    try{
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession session){

                }
            }, mBackgroundHandler);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }
    protected void createCameraPreview(){
        try{
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if(null == cameraDevice){
                        return;
                    }

                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(MainActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void openCamera(){
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");

        try{
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
        Log.e(TAG, "open camer X");
    }

    protected void updatePreview(){
        if(null == cameraDevice){
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try{
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        }catch(CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void closeCamera(){
        if(null != cameraDevice){
            cameraDevice.close();
            cameraDevice = null;
        }
        if(null != imageReader){
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == REQUEST_CAMERA_PERMISION){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(MainActivity.this, "You cant use this app without granting permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
