package com.example.xavier.pruebas.pruebas;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xavier.pruebas.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoFragment.OnPhotoFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static final int REQUEST_TAKE_PHOTO = 1;

    private ImageView image_view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnPhotoFragmentInteractionListener mListener;

    public PhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoFragment newInstance(String param1, String param2) {
        PhotoFragment fragment = new PhotoFragment();
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
        View root_view = inflater.inflate(R.layout.fragment_photo, container, false);

        Button b_tomarFoto = (Button) root_view.findViewById(R.id.tomar_foto);
        b_tomarFoto.setOnClickListener(new TakePictureClickListener());

        Button bt_siguiente = (Button) root_view.findViewById(R.id.bt_next);
        bt_siguiente.setOnClickListener(new NextClickListener());

        image_view = (ImageView) root_view.findViewById(R.id.photo_preview);

        return root_view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPhotoTaked(uri);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnPhotoFragmentInteractionListener) {
            mListener = (OnPhotoFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPhotoFragmentInteractionListener");
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
    public interface OnPhotoFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPhotoTaked(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getActivity(), "Entra fragment", Toast.LENGTH_LONG).show();
        if (requestCode == REQUEST_TAKE_PHOTO) {

            if (resultCode == Activity.RESULT_OK) {
                if (data == null){
                    Toast.makeText(getActivity(), "data null", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(), "data not null", Toast.LENGTH_LONG).show();
                    image_view.setImageURI(data.getData());

                }
            } else {
                Toast.makeText(getActivity(), "Se produjo un error al intentar abrir la aplicacion de fotos", Toast.LENGTH_LONG).show();
            }

        }
    }

    /*camera*/
   /* private boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            Camera mCamera;
            mCamera = Camera.open(id);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }*/

   /* private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }*/


    /*inner classes or helpers*/
    private class TakePictureClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //To-Do sacar foto violenta
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private class NextClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            mListener.onPhotoTaked(null);
        }
    }
}
