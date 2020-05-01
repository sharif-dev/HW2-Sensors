package com.example.fulloffeatures.fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.fulloffeatures.R;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
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
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        Button setAlarmButton = view.findViewById(R.id.button_time_select);
        Button resetAlarmButton = view.findViewById(R.id.button_alarm_reset);
        final TextView alarmTimeText = view.findViewById(R.id.text_alarm_time);
        int hour = loadTime("HOUR");
        int minute = loadTime("MINUTE");
        if (hour == -1 && minute == -1) {
            alarmTimeText.setText("");
        } else {
            alarmTimeText.setText(getAlarmText(hour, minute));
        }
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        alarmTimeText.setText(getAlarmText(hour, minute));
                        saveTime(hour, minute);
                    }
                });
                timePicker.show(getChildFragmentManager(), "timePicker");

            }
        });

        resetAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmTimeText.setText("");
                saveTime(-1, -1);
            }
        });

        return view;
    }

    private String getAlarmText(int hour, int minute) {
        return String.format("Alarm: %02d:%02d", hour, minute);
    }

    private void saveTime(int hour, int minute) {
        SharedPreferences preference = Objects.requireNonNull(this.getActivity()).getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("ALARM_HOUR", hour);
        editor.putInt("ALARM_MINUTE", minute);
        editor.apply();
    }

    private int loadTime(String key) {
        SharedPreferences settings = Objects.requireNonNull(this.getActivity()).getPreferences(Activity.MODE_PRIVATE);
        return settings.getInt("ALARM_" + key, -1);
    }

}
