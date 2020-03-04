package com.jsrdev.calendarapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {

    private List<Event> mEventList;
    private Context mContext;

    public EventsAdapter(Context context, List<Event> eventList) {
        super(context, 0,eventList);
        mEventList = eventList;
        mContext = context;
    }


    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.event_list_item,parent,false);
        }

        Event event = getItem(position);




        TextView iconTxt = listItemView.findViewById(R.id.iconTxt);
        iconTxt.setText(getIcon(event.getStartDate()));

       // iconTxt.setBackground(ContextCompat.getDrawable(mContext,R.drawable.oval_shape));

        TextView titleText = listItemView.findViewById(R.id.listTitle);
        titleText.setText(event.getTitle());

        TextView id = listItemView.findViewById(R.id.idtxt);
        id.setText(Integer.toString(event.getId()));
        return listItemView;
    }

    private String getIcon(String startDate) {
        String[] splitText = startDate.split(" ");
        return splitText[0];
    }
}
