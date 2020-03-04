package com.jsrdev.calendarapp;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jsrdev.calendarapp.Database.SqliteDbHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewEventFragmentDialog extends DialogFragment {

    TextView titleTxt , locationTxt , descriptionTxt ,startTxt , endTxt , closeBtn , saveBtn , activityTitle;
    SqliteDbHelper db;

    public AddNewEventFragmentDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_event_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startTxt = getView().findViewById(R.id.startTxt);
        endTxt = getView().findViewById(R.id.endTxt);
        titleTxt = getView().findViewById(R.id.titleTxt);
        locationTxt = getView().findViewById(R.id.locationTxt);
        descriptionTxt = getView().findViewById(R.id.descriptionTxt);
        closeBtn = getView().findViewById(R.id.closeButton);
        saveBtn = getView().findViewById(R.id.saveButton);
        activityTitle = getView().findViewById(R.id.activityTitle);

        db = new SqliteDbHelper(getContext());


        Bundle args = getArguments();

        String selectedDate = args.getString("Date");
        startTxt.setText(selectedDate);
        endTxt.setText(selectedDate);

        insertData();


        String id = args.getString("Event_id");

        if (id != null) {
            getAllData("where id =?", new String[]{id});
        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });




    }

    public void insertData() {

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean result = db.insertData(titleTxt.getText().toString()
                        ,locationTxt.getText().toString()
                        ,descriptionTxt.getText().toString()
                        ,startTxt.getText().toString()
                        ,endTxt.getText().toString());

                if (result == true){
                    Toast.makeText(getContext(), "Event Added Succesfully", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }else {
                    Toast.makeText(getContext(), "Faled to add event", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    private void getAllData(String condition, String[] selArgs) {
        List<Event> eventList = new ArrayList<>();
        SqliteDbHelper db = new SqliteDbHelper(getContext());
        Cursor cursor = db.showAllData(condition,selArgs);

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            titleTxt.setText(cursor.getString(1));
            titleTxt.setEnabled(false);
            locationTxt.setText(cursor.getString(2));
            locationTxt.setEnabled(false);
            descriptionTxt.setText(cursor.getString(3));
            descriptionTxt.setEnabled(false);
            startTxt.setText(cursor.getString(4));
            endTxt.setText(cursor.getString(5));

            closeBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.GONE);
            activityTitle.setText("Event");
        }


    }

    public void closeDialog(View view){

    }
}
