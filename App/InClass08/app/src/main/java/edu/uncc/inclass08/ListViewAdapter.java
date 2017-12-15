package edu.uncc.inclass08;

/**
 * Created by sunand on 11/30/17.
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<User> data;
    private static LayoutInflater inflater=null;

    public ListViewAdapter(Activity a, ArrayList<User> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.listviewitem, null);

        TextView name = (TextView)vi.findViewById(R.id.txtName);
        TextView gender = (TextView)vi.findViewById(R.id.txtGender);
        TextView ipAddress = (TextView)vi.findViewById(R.id.txtIpAddress);

        User user = new User();
        user = data.get(position);

        //Setting all values in listview
        name.setText(user.getFirstName() +" " + user.getLastName());
        gender.setText(user.getGender());
        ipAddress.setText(user.getIpAddress());
        return vi;
    }
}