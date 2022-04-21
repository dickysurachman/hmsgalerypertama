package itts.dicky.surachman.task3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.image.vision.crop.CropLayoutView;
import com.huawei.secure.android.common.intent.SafeIntent;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class imagekit extends AppCompatActivity implements View.OnClickListener  {
    private static final String TAG = "CropImageActivity";
    private String picPath;
    private Bitmap inputBm;
    private Button cropImage;
    private Button flipH;
    private Button flipV;
    private Button rotate;
    private BitmapFactory.Options options;
    private CropLayoutView cropLayoutView;
    private RadioGroup rgCrop;
    private RadioButton rbCircular;
    private RadioButton rbRectangle;
    private Spinner spinner;
    private Context context;
    public static final String gambarcon = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagekit);
        cropLayoutView = findViewById(R.id.cropImageView);
        cropImage = findViewById(R.id.btn_crop_image);
        rotate = findViewById(R.id.btn_rotate);
        flipH = findViewById(R.id.btn_flip_horizontally);
        flipV = findViewById(R.id.btn_flip_vertically);
        cropLayoutView.setAutoZoomEnabled(true);
        cropLayoutView.setCropShape(CropLayoutView.CropShape.RECTANGLE);
        cropImage.setOnClickListener(this);
        rotate.setOnClickListener(this);
        flipH.setOnClickListener(this);
        flipV.setOnClickListener(this);
        rbCircular = findViewById(R.id.rb_circular);
        rgCrop = findViewById(R.id.rb_crop);
        rgCrop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                if (radioButton.equals(rbCircular)) {
                    cropLayoutView.setCropShape(CropLayoutView.CropShape.OVAL);
                } else {
                    cropLayoutView.setCropShape(CropLayoutView.CropShape.RECTANGLE);
                }
            }
        });
        spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String[] ratios = getResources().getStringArray(R.array.ratios);
                try {
                    int ratioX = Integer.parseInt(ratios[pos].split(":")[0]);
                    int ratioY = Integer.parseInt(ratios[pos].split(":")[1]);
                    cropLayoutView.setAspectRatio(ratioX, ratioY);
                } catch (Exception e) {
                    cropLayoutView.setFixedAspectRatio(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        rbRectangle = findViewById(R.id.rb_rectangle);
        String gambar1 = getIntent().getStringExtra(gambarcon);
        if(gambar1=="") {
            Intent intent = new SafeIntent(getIntent());
            inputBm = Utility.getBitmapFromUriStr(intent, this);
            cropLayoutView.setImageBitmap(inputBm);
        } else {
            Toast.makeText(imagekit.this, "Please wait, it may take a few minute...", Toast.LENGTH_LONG)
                    .show();
            inputBm = getBitmapFromURL(gambar1);
            cropLayoutView.setImageBitmap(inputBm);
        }

    }
    public static Bitmap getBitmapFromURL(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            //Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_crop_image:
                Bitmap croppedImage = cropLayoutView.getCroppedImage();
                cropLayoutView.setImageBitmap(croppedImage);
                break;
            case R.id.btn_rotate:
                cropLayoutView.rotateClockwise();
                break;
            case R.id.btn_flip_horizontally:
                cropLayoutView.flipImageHorizontally();
                break;
            case R.id.btn_flip_vertically:
                cropLayoutView.flipImageVertically();
                break;
        }
    }
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    private Bitmap LoadImage(String URL, BitmapFactory.Options options)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
        }
        return bitmap;
    }
    private InputStream OpenHttpConnection(String strURL) throws IOException{
        InputStream inputStream = null;
        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();
        try{
            HttpURLConnection httpConn = (HttpURLConnection)conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
        }
        return inputStream;
    }
}