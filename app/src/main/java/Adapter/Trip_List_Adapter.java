package Adapter;

import com.banyan.mss.R;
import com.banyan.mss.Trip_List_Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Sangavi on 5/25/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Trip_List_Adapter extends BaseAdapter{
    private Activity activity;
    private Context context;

    private ArrayList<HashMap<String, String>> data;
    private ArrayList<String> drop;
    private static LayoutInflater inflater=null;

    public Trip_List_Adapter(Activity a, ArrayList<HashMap<String, String>> d, ArrayList<String> drop_list) {
        activity = a;
        data=d;
        drop = drop_list;
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
        View v=convertView;
        if(convertView==null)
            v = inflater.inflate(R.layout.listview_trip_list, null);

        TextView list_triplist_txt_deptime = (TextView)v.findViewById(R.id.list_triplist_txt_deptime);
        TextView list_triplist_txt_arrtime = (TextView) v.findViewById(R.id.list_triplist_txt_arrtime);
        TextView list_triplist_txt_drop = (TextView) v.findViewById(R.id.list_triplist_txt_drop);
        TextView list_triplist_txt_fare = (TextView) v.findViewById(R.id.list_triplist_txt_fare);
        TextView list_triplist_txt_bustype = (TextView) v.findViewById(R.id.list_triplist_txt_bustype);
        TextView list_triplist_txt_btype = (TextView) v.findViewById(R.id.list_triplist_txt_btype);
        TextView list_triplist_txt_seats = (TextView) v.findViewById(R.id.list_triplist_txt_seats);
        TextView list_triplist_txt_offernames = (TextView) v.findViewById(R.id.list_triplist_txt_offernames);
        TextView list_triplist_txt_offeramount = (TextView) v.findViewById(R.id.list_triplist_txt_offeramt);
        TextView list_triplist_txt_offer_rate = (TextView) v.findViewById(R.id.list_triplist_txt_offer_rate);

        HashMap<String, String> result = new HashMap<String, String>();
        result = data.get(position);

        String[] namesArr = new String[drop.size()];
        for (int i = 0; i < drop.size(); i++) {
            namesArr[i] = String.valueOf(drop.get(i));
        }

        System.out.println("array"+ Arrays.toString(namesArr));

        float normal_amount = Float.parseFloat(result.get(Trip_List_Activity.TAG_FARE));
        float discount = Float.parseFloat(result.get(Trip_List_Activity.TAG_OFFERAMOUNT));

        String discount_amt = String.valueOf(normal_amount - discount);

        list_triplist_txt_deptime.setText(result.get(Trip_List_Activity.TAG_DEP_TIME));
        list_triplist_txt_arrtime.setText(result.get(Trip_List_Activity.TAG_ARR_TIME));

        list_triplist_txt_drop.setText("Droping Point: "+ Arrays.toString(namesArr).replaceAll("\\[|\\]", ""));
        //list_triplist_txt_drop.setText(drop_map.get(Trip_List_Activity.TAG_DROP_POINT));
        list_triplist_txt_fare.setText(result.get(Trip_List_Activity.TAG_FARE));
        list_triplist_txt_bustype.setText(result.get(Trip_List_Activity.TAG_TYPE));
        list_triplist_txt_btype.setText(result.get(Trip_List_Activity.TAG_BTYPE));
        list_triplist_txt_offernames.setText(result.get(Trip_List_Activity.TAG_OFFERNAME));
        list_triplist_txt_offeramount.setText(result.get(Trip_List_Activity.TAG_OFFERAMOUNT));
        list_triplist_txt_seats.setText("Available Seats "+result.get(Trip_List_Activity.TAG_SEATS));
        list_triplist_txt_offer_rate.setText(discount_amt);

        return v;

    }


}
