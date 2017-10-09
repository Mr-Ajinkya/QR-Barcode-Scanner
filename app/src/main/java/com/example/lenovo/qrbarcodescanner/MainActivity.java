package com.example.lenovo.qrbarcodescanner;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    public static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,GeneratorActivity.class);
        startActivity(intent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkPermission()){
                Toast.makeText(this, "Permission is granted!", Toast.LENGTH_SHORT).show();
            }
            else {
                requestPermissions();
            }
        }
    }

    private Boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(MainActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permission[], int grantResults[]){
        switch (requestCode){
            case REQUEST_CAMERA:
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Permission deined!", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if (shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("You need to allow access for both permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA},REQUEST_CAMERA);
                                        }
                                    }
                                });

                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    public void onResume(){
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkPermission()){
                if (scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }else {
                requestPermissions();
            }
        }

    }

    public void onDestroy(){
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancle", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {
        final String scanResult = result.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("scan result");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scannerView.resumeCameraPreview(MainActivity.this);
            }
        });
        builder.setNeutralButton("visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));
                startActivity(intent);
            }
        });
        builder.setMessage(scanResult);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
