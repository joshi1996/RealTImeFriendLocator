package ca.cmetcalfe.locationshare;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String str;
    private String locations;
    private FirebaseDatabase database;
    DatabaseReference myRef;
   static   double dLat;
    static double dLong;
   // String DeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        database = FirebaseDatabase.getInstance();

        myRef = database.getReference("Location");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locations = dataSnapshot.getValue(String.class);
                if (locations != null) {
                    String[] latlog = locations.split(",");
                    String latiPos= latlog[0].trim();
                    String longiPos= latlog[1].trim();
                    dLat = Double.parseDouble(latiPos);
                    dLong = Double.parseDouble(longiPos);
                    Toast.makeText(MapsActivity.this,dLat +"  "+dLong,Toast.LENGTH_LONG).show();
                    if(dLat==0 || dLong ==0){
                        Toast.makeText(MapsActivity.this,"error in fetching data",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       /* str = getIntent().getExtras().getString("LOCVAL");
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
        String [] separated = str.split(",");
        String latiPos= separated[0].trim();
        String longiPos =separated[1].trim();
        dLat = Double.parseDouble(latiPos);
        dLong = Double.parseDouble(longiPos);
    }*/
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
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(26.78, 75.45);
        //Toast.makeText(MapsActivity.this,latitude+"  "+longitude,Toast.LENGTH_LONG).show();
        //Toast.makeText(MapsActivity.this,dLat+"  "+dLong,Toast.LENGTH_LONG).show();
        LatLng sydney = new LatLng(MapsActivity.dLat,MapsActivity.dLong);
        MarkerOptions marker = new MarkerOptions().position(sydney).title("Marker");
        //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus));
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }
    public Double getLatitude(){
        return this.dLat;
    }
    public Double getLogitude(){
        return this.dLong;
    }
}
