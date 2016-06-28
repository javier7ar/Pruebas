package com.example.xavier.pruebas.ui.take_sample;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.xavier.pruebas.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoCameraFragment.OnPhotoCameraFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoCameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoCameraFragment extends Fragment implements SurfaceHolder.Callback, Camera.PictureCallback{

    private static final String ARG_PARAM_TEXTO = "param_texto";
    private static final String ARG_PARAM_IMAGEN = "param_imagen";

    private String mParamTexto;
    private String mParamImagen;

    private Camera camera;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ViewGroup photo_layout;
    private ViewGroup preview_layout;
    private ImageView photo_preview;

    private Uri imageURI;

    private OnPhotoCameraFragmentInteractionListener mListener;

    public PhotoCameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param paramTexto Texto a mostrar en la Camara.
     * @param paramImagen Imagen para superponer en la Camara.
     * @return A new instance of fragment PhotoCameraFragment.
     */

    public static PhotoCameraFragment newInstance(String paramTexto, String paramImagen) {
        PhotoCameraFragment fragment = new PhotoCameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TEXTO, paramTexto);
        args.putString(ARG_PARAM_IMAGEN, paramImagen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamTexto = getArguments().getString(ARG_PARAM_TEXTO);
            mParamImagen = getArguments().getString(ARG_PARAM_IMAGEN);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_photo_camera, container, false);

        photo_layout = (ViewGroup) root_view.findViewById(R.id.photo_layout);
        preview_layout = (ViewGroup) root_view.findViewById(R.id.preview_layout);

        surfaceView = (SurfaceView) root_view.findViewById(R.id.surfaceView2);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Button b_takePicture = (Button) root_view.findViewById(R.id.b_take_picture);
        b_takePicture.setOnClickListener(new TakePictureClick());

        TextView textView = (TextView) root_view.findViewById(R.id.lb_instructions);
        textView.setText(mParamTexto);

        photo_preview = (ImageView) root_view.findViewById(R.id.photo_preview);

        Button b_next = (Button) root_view.findViewById(R.id.bt_next);
        b_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPhotoCameraFragmentInteraction(imageURI
                );
            }
        });

        return root_view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhotoCameraFragmentInteractionListener) {
            mListener = (OnPhotoCameraFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPhotoCameraFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
        }

        catch (RuntimeException e) {
            System.err.println(e);
            return;
        }

        try {
            setCameraDisplayOrientation(0, camera);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }

        catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private void setCameraDisplayOrientation(int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        }

        catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (Exception e) {
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPhotoCameraFragmentInteractionListener {

        void onPhotoCameraFragmentInteraction(Uri uri);
    }



    private class TakePictureClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                captureImage();
                //mListener.onPhotoCameraFragmentInteraction(imageURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void captureImage() throws IOException {
        // Toma la foto y llama a onPictureTaken()
        camera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        try {
            ContextWrapper cw = new ContextWrapper(getContext());

            // Directorio privado de la aplicacion
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // El nombre del archivo armado con la fecha/hora
            String filename = String.format("%d.jpg", System.currentTimeMillis());
            // Creao el archivo
            File file = new File(directory,filename);
            // Creo el output stream para escribir sobre el archivo
            FileOutputStream fos =  new FileOutputStream(file);


            Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);
            ExifInterface exif=new ExifInterface(file.toString());

            if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){
                realImage= rotate(realImage, 90);
            } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
                realImage= rotate(realImage, 270);
            } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
                realImage= rotate(realImage, 180);
            } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")){
                realImage= rotate(realImage, 90);
            }

            boolean bo = realImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            // Guardo y cierro
            //fos.write(data);
            fos.close();

            imageURI = Uri.fromFile(file);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Error:" + e.toString(), Toast.LENGTH_LONG).show();
        }

        //refreshCamera();
        showPreviewLayout();
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private void showPreviewLayout () {
        // Oculto el layout de la camara
        photo_layout.setVisibility(View.INVISIBLE);
        // FIXME cerrar camara aca y volver a abrir cuando vuelve a tomar la foto

        // Muestro el layout del preview
        preview_layout.setVisibility(View.VISIBLE);

        // Cargo la imagen en el control que la muestra
        photo_preview.setImageURI(imageURI);
        photo_preview.refreshDrawableState();
        Log.e("Image URI",imageURI.toString());
    }




}
