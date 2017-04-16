package com.example.loginregister;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import static android.content.Context.SENSOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostFinder.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostFinder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFinder extends Fragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SensorManager sManager;
    private BubbleView bubbleView;
    private Sensor accel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PostFinder() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFinder.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFinder newInstance(String param1, String param2) {
        PostFinder fragment = new PostFinder();
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
        sManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post_finder, container, false);
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.container);
        bubbleView = new BubbleView(getActivity());
        relativeLayout.addView(bubbleView);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        //if (mListener != null) {
          //  mListener.onFragmentInteraction(uri);
        //}
    }

    public boolean checkQuest(int id) {
        return bubbleView.checkQuest(id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //if (context instanceof OnFragmentInteractionListener) {
        //} else {
          //  throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
        //}
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
        //void onFragmentInteraction(Uri uri);
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // don't do anything; we don't care
    }
    public void onSensorChanged(SensorEvent event) {
        bubbleView.move((180-event.values[0])/3, (-event.values[1]-90)/3);
        bubbleView.invalidate();
    }
//180 kanan makin kecil
  //  -90 atas makin besar
}
