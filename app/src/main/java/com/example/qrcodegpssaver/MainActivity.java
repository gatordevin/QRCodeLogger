package com.example.qrcodegpssaver;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amplifyframework.api.ApiOperation;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelSubscription;
import com.amplifyframework.datastore.generated.model.QRRecord;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import android.app.Application;
import android.util.Log;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import android.util.Log;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Priority;
import com.amplifyframework.datastore.generated.model.Todo;
import java.util.*;
import java.util.concurrent.TimeUnit;
import com.amplifyframework.api.aws.AWSApiPlugin;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    // QR code reader
    private MultiFormatReader mMultiFormatReader;

    // Google API client
    private GoogleApiClient mGoogleApiClient;

    // User's current location
    private Location mCurrentLocation;

    // UI elements
    private TextView mQRCodeText;
    private ImageView mQRCodeImage;
    private Button mScanButton;
    public static final int REQUEST_CAMERA_PERMISSION = 1;
    public static final int REQUEST_LOCATION_PERMISSION = 2;
    public static final int REQUEST_STORAGE_PERMISSION = 3;
    public static final int REQUEST_COARSE_LOCATION_PERMISSION = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Link to amazon databse is here. https://us-east-1.console.aws.amazon.com/appsync/home?region=us-east-1#/53gpxaoqlfgyxhvp6bap33ubwm/v1/home

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

        // Request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }

        // Request location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION_PERMISSION);
        }

        // Request storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        mQRCodeText = (TextView) findViewById(R.id.qrcode_text);
        mQRCodeImage = (ImageView) findViewById(R.id.qrcode_image);
        mScanButton = (Button) findViewById(R.id.scan_button);

        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onScanButtonClicked(view);
            }
        });

        // Create QR code reader
        mMultiFormatReader = new MultiFormatReader();

        // Build Google API client
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String date1 = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
                String qrCodeID = data.getStringExtra("SCAN_RESULT");
                double latitude = mCurrentLocation.getLatitude();
                double longitude = mCurrentLocation.getLongitude();
                QRRecord record = QRRecord.builder()
                                .lat(latitude)
                                .lon(longitude)
                                .qrValue(qrCodeID)
                                .timestamp(new Temporal.DateTime(date1))
                               .build();

                Amplify.DataStore.save(record,
                        response -> Log.i("MyAmplifyApp", "Created Succesfully"),
                        error -> Log.e("MyAmplifyApp", "Create failed", error)
                );
            }
            if(resultCode == RESULT_CANCELED){
                System.out.println("scan failed");
            }
        }
        mScanButton.setEnabled(true);
    }

    /**
     * Builds a Google API client.
     */
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Decodes a QR code from a given image.
     *
     * @param bitmap the image to decode the QR code from
     * @return the decoded QR code
     * @throws NotFoundException if the QR code cannot be found
     */
    private Result decodeQRCode(Bitmap bitmap) throws NotFoundException {
        int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        com.google.zxing.LuminanceSource source = new com.google.zxing.RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));

        return mMultiFormatReader.decodeWithState(bitmap1);
    }

    /**
     * Encodes a QR code from a given text.
     *
     * @param text the text to encode in the QR code
     * @return the generated QR code image
     */
    private Bitmap encodeQRCode(String text) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 200, 200);
            return bitmap;

        } catch (Exception e) {
            Log.e(TAG, "Error while encoding QR code", e);
            return null;
        }
    }

    /**
     * Saves a given QR code image to the device.
     *
     * @param bitmap the QR code image to save
     */
    private void saveQRCode(Bitmap bitmap) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "qrcode.png");
            FileOutputStream fOut = new FileOutputStream(file);
            // Save QR code to file
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

        } catch (Exception e) {
            Log.e(TAG, "Error while saving QR code", e);
        }
    }

    /**
     * Called when the scan button is clicked.
     *
     * @param view the view that was clicked
     */
    public void onScanButtonClicked(View view) {
// Disable scan button
        mScanButton.setEnabled(false);

        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

        startActivityForResult(intent, 0);

//        System.out.println("Capturing image");
//
//// Open device's camera
//        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, 0);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            // Get user's current location
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Get the current location
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } else {
                // Location permission not granted, ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            }

        } catch (SecurityException e) {
            Log.e(TAG, "Error while getting user's location", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the current location
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } else {
                // Permission not granted, show a message to the user
                Toast.makeText(this, "Location permission is required to use this app", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}