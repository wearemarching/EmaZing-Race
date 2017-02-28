package com.example.loginregister;


import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mLocationMarker;
    LocationRequest mLocationRequest;
    private double i = 0.01;

    double lat;
    double lng;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;


    final Random rnd = new Random();

    private OnFragmentInteractionListener mListener;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(String param1) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

       /* mapView = (MapView) getView().findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);*/

        /*Intent myIntent = getIntent();
        final String gameToken = myIntent.getStringExtra("gameToken");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent mServiceIntent = new Intent(getApplicationContext(), LocationBroadcastService.class);
        mServiceIntent.setData(Uri.parse("https://whispering-lake-62045.herokuapp.com/location?gameToken=" + gameToken));
        startService(mServiceIntent);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();

        fragment.getMapAsync(this);

        return inflater.inflate(R.layout.fragment_maps, container, false);
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
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        super.onPause();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        if(mMap !=null){
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

                @Override
                public View getInfoWindow(Marker marker){
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker){
                    View v = getActivity().getLayoutInflater().inflate(R.layout.info_window,null);

                    TextView judul_Pos= (TextView) v.findViewById(R.id.judulPos);
                    TextView nama_Pos= (TextView) v.findViewById(R.id.namaPos);
                    TextView ket_Pos= (TextView) v.findViewById(R.id.ketPos);
                    TextView point_Pos= (TextView) v.findViewById(R.id.pointPos);

                    judul_Pos.setText(marker.getTitle());
                    if(marker.getTitle().matches("POS A")){
                        nama_Pos.setText("PERMAINAN CONGKLAK");
                        ket_Pos.setText("Anda harus mencari papan congklak dan bermain bersama pasangan");
                        point_Pos.setText("Point: 100pts");
                    }
                    else if (marker.getTitle().matches("POS B")){
                        nama_Pos.setText("PERMAINAN BOLA");
                        ket_Pos.setText("Anda harus memasukan bola ke dalam keranjang sebanyak - banyaknya");
                        point_Pos.setText("Point: setiap lemparan masuk 5pts");
                    }
                    else if (marker.getTitle().matches("POS C")){
                        nama_Pos.setText("PERMAINAN apaya");
                        ket_Pos.setText("apaya");
                        point_Pos.setText("Point: pts");
                    }
                    else if (marker.getTitle().matches("POS D")){
                        nama_Pos.setText("PERMAINAN ?");
                        ket_Pos.setText("?");
                        point_Pos.setText("Point: pts");
                    }
                    return v;
                }
            });
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    //Generate Player
    public void createPlayer1(double x, double y){
        MarkerOptions player1 = new MarkerOptions();
        LatLng koord = new LatLng(x,y);
        player1.position(koord);
        player1.title("Player 1");
        player1.icon(BitmapDescriptorFactory.fromResource(R.mipmap.villain));
        mLocationMarker = mMap.addMarker(player1);
    }

    public void createPlayer2(double x, double y){
        MarkerOptions player2 = new MarkerOptions();
        LatLng koord = new LatLng(x,y);
        player2.position(koord);
        player2.title("Player 2");
        player2.icon(BitmapDescriptorFactory.fromResource(R.mipmap.villain));
        mLocationMarker = mMap.addMarker(player2);
    }

    public void createPlayer3(double x, double y){
        MarkerOptions player3 = new MarkerOptions();
        LatLng koord = new LatLng(x,y);
        player3.position(koord);
        player3.title("Player 3");
        player3.icon(BitmapDescriptorFactory.fromResource(R.mipmap.villain));
        mLocationMarker = mMap.addMarker(player3);
    }

    public void createPlayer4(double x, double y){
        MarkerOptions player4 = new MarkerOptions();
        LatLng koord = new LatLng(x,y);
        player4.position(koord);
        player4.title("Player 1");
        player4.icon(BitmapDescriptorFactory.fromResource(R.mipmap.villain));
        mLocationMarker = mMap.addMarker(player4);
    }

    public void addMarker(){

        //POS A = Labtek 5 tengah
        LatLng labtek5 = new LatLng(-6.890555, 107.609830);
        MarkerOptions posA = new MarkerOptions();
        posA.position(labtek5);
        posA.title("POS A");
        posA.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mLocationMarker = mMap.addMarker(posA);

        //POS B = Labtek 6 hadap Intel
        LatLng labtek6 = new LatLng(-6.890567, 107.610568);
        MarkerOptions posB = new MarkerOptions();
        posB.position(labtek6);
        posB.title("POS B");
        posB.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mLocationMarker = mMap.addMarker(posB);

        //POS C = Labtek 7 hadap GKU Timur
        LatLng labtek7 = new LatLng(-6.890078, 107.611375);
        MarkerOptions posC = new MarkerOptions();
        posC.position(labtek7);
        posC.title("POS C");
        posC.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        mLocationMarker = mMap.addMarker(posC);

        //POS D = Labtek 7 hadap Intel
        LatLng labtek8 = new LatLng(-6.890193, 107.610133);
        MarkerOptions posD = new MarkerOptions();
        posD.position(labtek8);
        posD.title("POS D");
        posD.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        mLocationMarker = mMap.addMarker(posD);
    }

    public boolean checkLocationSame(){
        LatLng latLng = new LatLng(lat, lng);
        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(
                (((latLng.latitude <= -6.890555)&&(latLng.latitude > -6.890555)) &&
                ((latLng.longitude <=107.609830)&&(latLng.longitude>107.609830))) ||
                        (((latLng.latitude <= -6.890567)&&(latLng.latitude > -6.890567)) &&
                        ((latLng.longitude <=107.610568)&&(latLng.longitude>107.610568))) ||
                        (((latLng.latitude <= -6.890078)&&(latLng.latitude > -6.890078)) &&
                        ((latLng.longitude <=107.611375)&&(latLng.longitude>107.611375)))||
        (((latLng.latitude <= -6.890193)&&(latLng.latitude > -6.890193)) &&
                ((latLng.longitude <=107.610133)&&(latLng.longitude>107.610133)))
        ){
            return true;
        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();
        addMarker();
        //Place current location marker

        lat = location.getLatitude();
        lng = location.getLongitude();

        LatLng latLng = new LatLng(lat, lng);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

        //stop location updates
        if (mGoogleApiClient == null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResut) {

    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
}