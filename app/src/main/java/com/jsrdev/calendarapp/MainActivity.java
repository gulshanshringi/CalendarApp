package com.jsrdev.calendarapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jsrdev.calendarapp.Database.SqliteDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Calendar calendar;
    private FloatingActionButton floatingActionButton;
    private int startPosition;
    TextView monthYearTxt;
    String selectedDate;
    private SqliteDbHelper db ;
    private int activeMonth , activeYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new SqliteDbHelper(this);
        calendar = Calendar.getInstance();

        monthYearTxt = findViewById(R.id.monthYearTxt);

        String date = String.valueOf(Calendar.getInstance().get(Calendar.DATE)
                + Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        activeMonth = calendar.get(Calendar.MONTH);
        activeYear = calendar.get(Calendar.YEAR);

        setUpCalendarView();

        floatingActionButton = findViewById(R.id.floatingButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewEventFragmentDialog addNewEventFragmentDialog = new AddNewEventFragmentDialog();
                Bundle args = new Bundle();
                args.putString("Date",selectedDate);
                addNewEventFragmentDialog.setArguments(args);
                addNewEventFragmentDialog.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.Theme_AppCompat_Light_NoActionBar);
                addNewEventFragmentDialog.show(getSupportFragmentManager(),"addNewEventFragmentDialog");
            }
        });

        TextView showAllEvents = findViewById(R.id.showAllEventsTxt);
        showAllEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EventsActivity.class);
                startActivity(intent);

            }
        });
    }

    private void setUpCalendarView() {

        setUpMonthAndYear(monthYearTxt , activeMonth ,activeYear);

        TextView prevMonthBtn = findViewById(R.id.prevMonthBtn);
        TextView nextMonthBtn = findViewById(R.id.nextMonthBtn);

        prevMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeMonth == 0){
                    activeMonth = 12;
                    activeYear = activeYear - 1;
                }
                activeMonth = activeMonth - 1;
                setUpMonthAndYear(monthYearTxt , activeMonth ,activeYear);
            }
        });

        nextMonthBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (activeMonth == 11){
                    activeMonth = 0;
                    activeYear = activeYear + 1;
                }
                activeMonth = activeMonth + 1;
                setUpMonthAndYear(monthYearTxt , activeMonth ,activeYear);

            }
        });

        final GridView weekGridView = findViewById(R.id.weekGridView);
        String[] weekList = {"M","T","W","T","F","S","S"};

        ArrayAdapter weekAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1 ,weekList);
        weekGridView.setAdapter(weekAdapter);

    }

    private void setUpMonthAndYear(final TextView monthYearTxt,int currentMonth,int currentYear) {

        String month = null;
        switch (currentMonth){
            case 0:
                month = "January";
                break;
            case 1:
                month = "February";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            case 11:
                month = "December";
                break;
        }
        monthYearTxt.setText(month);
        monthYearTxt.append(" " + Integer.toString(currentYear));
        calendar.set(Calendar.MONTH,currentMonth);
        calendar.set(Calendar.DATE,1);

        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        int a = calendar.get(Calendar.DATE);



        calendar = Calendar.getInstance();
        List<String> dayList = new ArrayList<>();
        dayList.clear();
        for (int i = 1 ; i <= a ; i++){
            dayList.add(Integer.toString(i));
        }

        final GridView dayGridView = findViewById(R.id.dayGridView);
        dayGridView.setAdapter(null);
        CustomDayAdapter customDayAdapter = new CustomDayAdapter(this,dayList,firstDayOfMonth);
        dayGridView.setAdapter(customDayAdapter);


        dayGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position >= startPosition) {

                    final int size = dayGridView.getChildCount();
                    for (int i = 0; i < size; i++) {
                        ViewGroup gridChild = (ViewGroup) dayGridView.getChildAt(i);
                        int childSize = gridChild.getChildCount();
                        for (int k = 0; k < childSize; k++) {
                            if (gridChild.getChildAt(k) instanceof TextView) {
                                ((TextView) gridChild.getChildAt(k)).setBackground(null);
                                ((TextView) gridChild.getChildAt(k)).setTextColor(Color.parseColor("#A3A2A2"));
                            }
                        }
                    }

                    TextView textView = view.findViewById(R.id.dateTxt);
                    selectedDate = (String) textView.getText();
                    selectedDate = selectedDate + " " + monthYearTxt.getText();
                    setDateActive(position, textView);


                }
            }
        });
    }


    public class CustomDayAdapter extends BaseAdapter{

        private Context mContext;
        private List<String> mDateList;
        private int mStartingDay;
        LayoutInflater inflater;


        CustomDayAdapter(Context context , List<String> dateList,int startingDay){
            mContext = context;
            mDateList = dateList;
            mStartingDay = startingDay;
        }

        public int getCount() {
            int size = mDateList.size() + startPosition;

            return size;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,ViewGroup parent) {
            inflater = ( LayoutInflater )mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View gridView;
            gridView = new View(mContext);
            if (convertView == null) {
                gridView = inflater.inflate(R.layout.grid_item, null);

            }else {
                gridView = convertView;
            }
            TextView textView = gridView.findViewById(R.id.dateTxt);
            
             startPosition = -1;

            if (mStartingDay >= 2 && mStartingDay <= 7) {
                startPosition = mStartingDay - 2;
            }else {
                startPosition = mStartingDay + 5;
            }
            if (position < startPosition){
                textView.setEnabled(false);
            }else {
                textView.setText(mDateList.get(position - (startPosition) ));
                if (position - startPosition == 0){
                    selectedDate = (String) textView.getText();
                    selectedDate = selectedDate + " " + monthYearTxt.getText();
                    setDateActive(position , textView);
                }
            }
                return gridView;
        }
    }

    private void setDateActive(int position , TextView textView) {
        textView.setTextColor(Color.WHITE);
        textView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.oval_shape));

        getEventData();

    }

    private void getEventData() {

        ListView listView = findViewById(R.id.eventsCalList);
        listView.setAdapter(null);
        List<Event> eventList = new ArrayList<>();
        eventList = getAllData(" where START_DATE =?", new String[]{selectedDate});
        if (eventList.size() >= 0) {
            EventsAdapter adapter = new EventsAdapter(this, eventList);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private List<Event> getAllData(String condition, String[] selArgs) {
        List<Event> eventList = new ArrayList<>();
        SqliteDbHelper db = new SqliteDbHelper(this);
        Cursor cursor = db.showAllData(condition,selArgs);

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

        return eventList;
    }
}
