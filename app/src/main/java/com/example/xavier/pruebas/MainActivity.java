package com.example.xavier.pruebas;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements PhotoFragment.OnFragmentInteractionListener{

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private File photoFile;
    private String mCurrentPhotoPath;
    private ImageView mImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mImageView2 = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "CLIE_" + timeStamp + "_";

        String pathToFiles = getApplicationContext().getFilesDir().getPath();
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    public void tomarFoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {

            photoFile = null;

            try {
                photoFile = setUpPhotoFile();
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            } catch (IOException e) {
                e.printStackTrace();
                photoFile = null;
                mCurrentPhotoPath = null;
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                //Log.e("Fragment Foto/Geo","intent iniciado "+datos.mCurrentPhotoPath);
            }

        }
    }

    public void fragFoto(View view){
        //standar way to attach fragment to an activity
        PhotoFragment fragmento = PhotoFragment.newInstance("","");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragmento);
        transaction.addToBackStack(null);
        transaction.commit();

        }

    public void tomarMuestra(View view){
        Intent intent = new Intent(this, TakeSampleActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE) {

            if (resultCode == Activity.RESULT_OK) {

                if (mCurrentPhotoPath != null) {
                    setPic();
                    galleryAddPic();
                    mCurrentPhotoPath = null;
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
                if (photoFile != null) {
                    photoFile.delete();
                    photoFile = null;
                }
            } else {
                Toast.makeText(this, "Se produjo un error al intentar abrir la aplicacion de fotos", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void setPic() {
        // TODO revisar, da out_of_memory cuando se llama varias veces (tratar varias veces)

        mImageView2.setVisibility(View.INVISIBLE);
		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = mImageView2.getWidth();
        int targetH = mImageView2.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        Log.e("SetPic","mCurrentPhotoPath: "+mCurrentPhotoPath);
        //solicitudCliente.setPathToImage(mCurrentPhotoPath);

        if (bitmap == null) {
            //Toast.makeText(getActivity(), "bitmap NULL", Toast.LENGTH_LONG).show();
        }

		/* Associate the Bitmap to the ImageView */
        mImageView2.setImageBitmap(bitmap);
        mImageView2.setVisibility(View.VISIBLE);

        //Toast.makeText(getActivity(), "setPic", Toast.LENGTH_LONG).show();
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //
    }
}
