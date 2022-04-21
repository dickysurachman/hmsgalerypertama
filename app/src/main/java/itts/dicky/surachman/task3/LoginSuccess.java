package itts.dicky.surachman.task3;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.ui.SafeIntent;
import com.huawei.secure.android.common.util.LogsUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class LoginSuccess extends AppCompatActivity implements View.OnClickListener {

    public static final String Name = "Name";
    public static final String Gambar = "Picture";
    public static final String Email1 = "Email";
    private Context context;
    private static final int GET_BY_CROP = 804;
    private static final int GET_BY_ALBUM1 = 801;
    private static final int GET_BY_CAMERA = 805;
    List<String> mPermissionList = new ArrayList<>();
    String[] permissions = new String[] {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private final int mRequestCode = 100;

    TextView teks;
    Button but1,but2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        BannerView bannerView = findViewById(R.id.hw_banner_view);
        bannerView.setAdId("testw6vs28auh3");
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        // Set the refresh interval to 60 seconds.
        bannerView.setBannerRefresh(15);
        // Create an ad request to load an ad.
        AdParam adParam = new AdParam.Builder().build();
        bannerView.loadAd(adParam);

        teks = findViewById(R.id.textView);
        String name1 = getIntent().getStringExtra(Name);
        String gambar1 = getIntent().getStringExtra(Gambar);
        String emails = getIntent().getStringExtra(Email1);
        teks.setText("Welcome "+  name1 +"\n"+"Your Email "+  emails );
        but1 = findViewById(R.id.maps);
        but1.setOnClickListener(this);
        but2 = findViewById(R.id.imagekit2);
        but2.setOnClickListener(this);

        //Gbr = findViewById(R.id.imageView);
        new DownloadImageFromInternet((ImageView) findViewById(R.id.imageView)).execute(gambar1);

        if (Build.VERSION.SDK_INT >= 23) {
            initPermission();
        }
    }
    private void initPermission() {
        // Clear the permissions that fail the verification.
        mPermissionList.clear();
        //Check whether the required permissions are granted.
        for (int i = 0; i < permissions.length; i++) {
            if (PermissionChecker.checkSelfPermission(this, permissions[i])
                    != PermissionChecker.PERMISSION_GRANTED) {
                // Add permissions that have not been granted.
                mPermissionList.add(permissions[i]);
            }
        }
        //Apply for permissions.
        if (mPermissionList.size() > 0) {//The permission has not been granted. Please apply for the permission.
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
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
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.maps:
                getByAlbum(LoginSuccess.this, GET_BY_CROP);
                break;
            case R.id.imagekit2:
                Toast.makeText(LoginSuccess.this, "Please wait for download sample", Toast.LENGTH_LONG)
                        .show();
                Intent intent4 = new Intent(LoginSuccess.this, imagekit.class);
                intent4.putExtra(imagekit.gambarcon,"https://www.intiwhiz.com/images/prioritize/intiwhiz1.jpg");
                intent4.putExtra("uri","https://www.intiwhiz.com/themenew/img/slide/8.jpg");
                startActivity(intent4);
                break;
        }
    }
    public static void getByAlbum(Activity act, int type) {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        act.startActivityForResult(getAlbum, type);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data) {
            super.onActivityResult(requestCode, resultCode, data);
            try {
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri;
                    switch (requestCode) {
                        case GET_BY_CROP:
                            Intent intent = new SafeIntent(data);
                            uri = intent.getData();
                            Intent intent4 = new Intent(LoginSuccess.this,
                                    imagekit.class);
                            intent4.putExtra("uri", uri.toString());
                            startActivity(intent4);
                            break;
                    }
                }
            } catch (Exception e) {
                LogsUtil.i("onActivityResult", "Exception");
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoURI = FileProvider.getUriForFile(LoginSuccess.this,
                            LoginSuccess.this.getApplicationContext().getPackageName()
                                    + ".fileprovider", new File(context.getFilesDir(), "temp.jpg"));
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, GET_BY_CAMERA);

                } else {
                    Toast.makeText(LoginSuccess.this, "No permission.", Toast.LENGTH_LONG)
                            .show();
                }
                return;
            }
        }
    }
}