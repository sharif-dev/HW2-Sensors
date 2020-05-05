package com.example.fulloffeatures.fragments;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.hardware.Sensor;
        import android.hardware.SensorManager;
        import android.os.Bundle;

        import androidx.fragment.app.Fragment;

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;

        import com.example.fulloffeatures.R;
        import com.example.fulloffeatures.services.ShakeService;
        import com.google.android.material.snackbar.Snackbar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScreenLockerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScreenLockerFragment extends Fragment {

    private boolean serviceRunning = false;
    private final String TAG = "shake fragment";
    private Activity activity;

    private Button mShakeButton;

    private Intent intent;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public ScreenLockerFragment() {
    }

    public static ScreenLockerFragment newInstance() {
        ScreenLockerFragment fragment = new ScreenLockerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_locker, container, false);
        mShakeButton = view.findViewById(R.id.screen_locker_fragment_start_button);
        activity = getActivity();
        preferences = getContext()
                .getSharedPreferences(getString(R.string.default_pref), Context.MODE_PRIVATE);
        editor = preferences.edit();
        serviceRunning = preferences.getBoolean(getString(R.string.running), false);
        if (serviceRunning) {
            mShakeButton.setText(getString(R.string.stop_screen_locker_service));
        }
        intent = new Intent(activity, ShakeService.class);

        mShakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sensorsIsAvailable()) {
                    Snackbar.make(
                            v,
                            getString(R.string.hardware_compatibility_error),
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!serviceRunning) {
                    Log.d(TAG, "screen locker service started ");
                    mShakeButton.setText(R.string.stop_shake);
                    intent = new Intent(activity, ShakeService.class);
                    intent.putExtra(getString(R.string.sensitivity), 20);
                    System.out.println("////////////////////////////////");
                    activity.startService(intent);
                    serviceRunning = true;
                } else {
                    Log.d(TAG, "screen locker turned off");
                    mShakeButton.setText(R.string.start_shake);
                    activity.stopService(intent);
                    serviceRunning = false;
                }
                editor.putBoolean(getString(R.string.running), serviceRunning);
            }
        });

        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
        super.onDestroy();
        editor.commit();
    }

    private boolean sensorsIsAvailable() {
        SensorManager sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        Sensor acceleratorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (acceleratorSensor == null) {
            return false;
        }
        return true;
    }
}
