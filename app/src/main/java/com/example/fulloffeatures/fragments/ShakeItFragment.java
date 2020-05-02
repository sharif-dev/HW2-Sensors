package com.example.fulloffeatures.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.fulloffeatures.R;
import com.example.fulloffeatures.services.ShakeService;

import java.util.prefs.Preferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShakeItFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShakeItFragment extends Fragment {

    private boolean serviceRunning = false;
    private final String TAG = "shake fragment";
    private Activity activity;
    private int sensitivity = 50;


    private TextView percentTv;
    private SeekBar seekBar;
    private Button mShakeButton;

    private Intent intent;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public ShakeItFragment() { }

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
        activity = getActivity();
        seekBar = view.findViewById(R.id.shake_it_fragment_seek_bar);
        percentTv = view.findViewById(R.id.shake_it_fragment_percent_tv);
        preferences = getContext()
                .getSharedPreferences(getString(R.string.default_pref), Context.MODE_PRIVATE);
        editor = preferences.edit();
        serviceRunning = preferences.getBoolean(getString(R.string.running), false);
        sensitivity = preferences.getInt("sensitivity", 50);
        if (serviceRunning) {
            mShakeButton.setText(getString(R.string.stop_shake));
        }
        percentTv.setText(String.valueOf(sensitivity));
        seekBar.setProgress(sensitivity);
        intent = new Intent(activity, ShakeService.class);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                percentTv.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sensitivity = seekBar.getProgress();
                if (serviceRunning) {
                    activity.stopService(intent);
                    Log.d(TAG, "rerunning shake service ");
                    intent = new Intent(activity, ShakeService.class);
                    intent.putExtra(getString(R.string.sensitivity), sensitivity);
                    activity.startService(intent);
                }
                editor.putInt(getString(R.string.sensitivity), sensitivity);
            }
        });

        mShakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!serviceRunning) {
                    Log.d(TAG, "shake service started ");
                    mShakeButton.setText(R.string.stop_shake);
                    intent = new Intent(activity, ShakeService.class);
                    intent.putExtra(getString(R.string.sensitivity), sensitivity);
                    activity.startService(intent);
                    serviceRunning = true;
                } else {
                    Log.d(TAG, "shake service turned off");
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
}
