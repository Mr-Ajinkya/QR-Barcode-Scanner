package com.example.lenovo.qrbarcodescanner;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GeneratorActivity extends AppCompatActivity {

    EditText text;
    Button btnGen;
    ImageView imageView;
    String text2Qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);
        text = findViewById(R.id.text);
        btnGen = findViewById(R.id.btnGen);
        imageView = findViewById(R.id.imageView);
        btnGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text2Qr = text.getText().toString().trim();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imageView.setImageBitmap(bitmap);
                }catch (WriterException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
