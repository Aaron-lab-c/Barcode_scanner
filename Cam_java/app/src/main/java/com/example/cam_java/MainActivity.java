package com.example.cam_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.*;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //找到介面
        zXingScannerView = findViewById(R.id.ZXingScannerView_QRCode);
        zXingScannerView.getWidth();

        //取得相機權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(this
                        , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    100);
        }else{
            //若先前已取得權限，則直接開啟
            openQRCamera();

        }
    }
    /**開啟QRCode相機*/
    private void openQRCamera(){
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }
    /**取得權限回傳*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults[0] ==0){
            openQRCamera();
        }else{
            Toast.makeText(this, "權限勒？", Toast.LENGTH_SHORT).show();
        }
    }
    /**關閉QRCode相機*/
    @Override
    protected void onStop() {
        zXingScannerView.stopCamera();
        super.onStop();
    }
    /**取得QRCode掃描到的物件回傳*/
    @Override
    public void handleResult(Result rawResult) {
        TextView tvResult = findViewById(R.id.textView_Result);
        tvResult.setText(rawResult.getText());
        String strmes=rawResult.getText();
        //ZXing相機預設掃描到物件後就會停止，以此這邊再次呼叫開啟，使相機可以為連續掃描之狀態
        openQRCamera();
        if(strmes.contains("http")){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(strmes));
            startActivity(intent);
        }else if(strmes.contains("1922")){
            Intent smsIntent=new Intent (Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("smsto:"));
            smsIntent.setType("vnd.android-dir/mms-sms");
            System.out.println("sssssssssssssssssssssssssssssssssssssssssssssssss");

            System.out.println(rawResult.getText());
            //address是紀錄要傳送簡訊的對方電話號碼
            smsIntent.putExtra("address"  , new String ("1922"));
            //sms_body則是記錄簡訊內容, 記得name欄位不可以改名稱
            smsIntent.putExtra("sms_body"  , rawResult.getText().split("1922:")[1]);
            //啟動傳簡訊
            startActivity(smsIntent);
        }else{

        }




    }

    public void refresh(View view) {
        openQRCamera();

    }
}