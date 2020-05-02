package com.example.fulloffeatures.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.fulloffeatures.R;
import com.example.fulloffeatures.services.ShakeService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShakeItFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShakeItFragment extends Fragment {

    private boolean serviceRunning = false;
    private final String TAG = "shake fragment";
    private ShakeService shakeService = null;
    private Activity activity;


    private SeekBar seekBar;
    private Button mShakeButton;

    public ShakeItFragment() {
        // Required empty public constructor
    }

    public static ShakeItFragment newInstance() {
        ShakeItFragment fragment = new ShakeItFragment();
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
        View view = inflater.inflate(R.layout.fragment_shake_it, container, false);
        mShakeButton = view.findViewById(R.id.shake_it_fragment_shake_button);
        shakeService = new ShakeService();
        activity = getActivity();

        mShakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!serviceRunning) {
                    Log.d(TAG, "shake service started ");
                    mShakeButton.setText(R.string.stop_shake);
                    activity.startService(new Intent(activity, ShakeService.class));
                    serviceRunning = true;
                } else {
                    Log.d(TAG, "shake service turned off");
                    mShakeButton.setText(R.string.start_shake);
                    activity.stopService(new Intent(activity, ShakeService.class));
                    serviceRunning = false;
                }
            }
        });

        return view;
    }






}
