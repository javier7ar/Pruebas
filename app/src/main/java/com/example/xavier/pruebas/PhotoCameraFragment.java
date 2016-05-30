package com.example.xavier.pruebas;

import android.content.Context;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
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
public class PhotoCameraFragment extends Fragment implements SurfaceHolder.Callback{

    private static final String ARG_PARAM_TEXTO = "param_texto";
    private static final String ARG_PARAM_IMAGEN = "param_imagen";

    private String mParamTexto;
    private String mParamImagen;

    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private Camera.PictureCallback jpegCallback;

    private OnPhotoCameraFragmentInteractionListener mListener;

    public PhotoCameraFragment() {
        // Required empty public constructor
    }

    /** A safe way to get an instance of the Camera object. */
    public  Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param paramTexto Texto a mostrar en la Camara.
     * @param paramImagen Imagen para superponer en la Camara.
     * @return A new instance of fragment PhotoCameraFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        jpegCallback = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream fos = null;
                String filename = "prueba.jpg";
                try {

                    fos = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }

                Toast.makeText(getActivity().getApplicationContext(), "Picture Saved with name" + filename, Toast.LENGTH_LONG).show();
                refreshCamera();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_photo_camera, container, false);

        surfaceView = (SurfaceView) root_view.findViewById(R.id.surfaceView2);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Button b_takePicture = (Button) root_view.findViewById(R.id.b_take_picture);
        b_takePicture.setOnClickListener(new SavePicture());

        TextView textView = (TextView) root_view.findViewById(R.id.lb_instructions);
        textView.setText(mParamTexto);

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
        // TODO: Update argument type and name
        void onPhotoCameraFragmentInteraction(Uri uri);
    }

    public void captureImage2() throws IOException {
        camera.takePicture(null, null, jpegCallback);
    }

    private class SavePicture implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                captureImage2();
                mListener.onPhotoCameraFragmentInteraction(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
