package com.jsrdev.calendarapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsrdev.calendarapp.Database.SqliteDbHelper;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        ListView eventsListView = findViewById(R.id.eventsList);
        TextView addBtn = findViewById(R.id.addEventButton);
        TextView backBtn = findViewById(R.id.backButton);

        List<Event> eventList = new ArrayList<>();
        eventList = getAllData("",null);
        EventsAdapter adapter = new EventsAdapter(this , eventList);
        eventsListView.setAdapter(adapter);

        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.idtxt);
                AddNewEventFragmentDialog addNewEventFragmentDialog = new AddNewEventFragmentDialog();
                Bundle args = new Bundle();
                args.putString("Event_id" , (String) textView.getText());
                addNewEventFragmentDialog.setArguments(args);
                addNewEventFragmentDialog.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.Theme_AppCompat_Light_NoActionBar);
                addNewEventFragmentDialog.show(getSupportFragmentManager(),"addNewEventFragmentDialog");
            }
        });

    }

    public List<Event> getAllData(String condition, String[] selArgs) {
        List<Event> eventList = new ArrayList<>();
        SqliteDbHelper db = new SqliteDbHelper(this);
        Cursor cursor = db.showAllData(condition,selArgs);
        if (cursor.getCount() == 0){
            Toast.makeText(this, "Faled", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String location = cursor.getString(2);
                String description = cursor.getString(3);
                String startDate = cursor.getString(4);
                String endDate = cursor.getString(5);
                Event event = new Event(id,title,location,description,startDate,endDate);
                eventList.add(event);
            }
        }
        return eventList;
    }

    public void startMainActivity(View view) {
        Intent intent = new Intent(EventsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
