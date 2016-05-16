package com.example.xavier.pruebas;

import android.content.Context;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoCameraFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoCameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoCameraFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int CAMERA_FACING_BACK = 0;
    private static final int CAMERA_FACING_FRONT = 1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PhotoCameraFragment() {
        // Required empty public constructor
    }

    /** A safe way to get an instance of the Camera object. */
    public Camera getCameraInstance(){
        //from 0 to n-1 cameras
        int num_cameras = Camera.getNumberOfCameras();
        Camera c = null;
        //if there is only 1 camera, then
        try {
            if(num_cameras == 0) {
                c = Camera.open(); // attempt to get a Camera instance
            }
            else{
                int backCamera = 0;
                for(int i=0;i<=num_cameras;i++){
                    //c = Camera.open(i);
                    Camera.CameraInfo cam_info = null;
                    Camera.getCameraInfo(0, cam_info);
                    if(cam_info.facing == CAMERA_FACING_BACK){
                        backCamera = i;
                    }
                }
                if(backCamera == 0) {
                    //open default camera
                    c = Camera.open();
                }
                else{
                    c = Camera.open(backCamera);
                }
            }
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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoCameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoCameraFragment newInstance(String param1, String param2) {
        PhotoCameraFragment fragment = new PhotoCameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_camera, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
