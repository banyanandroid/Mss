package Adapter;

import com.banyan.mss.Bookdetail_Activity;
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

public class Book_Detail_List_Adapter extends BaseAdapter{
    private Activity activity;
    private Context context;

    private ArrayList<HashMap<String, String>> data;
    private ArrayList<String> drop;
    private static LayoutInflater inflater=null;

    public Book_Detail_List_Adapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
        View v=convertView;
        if(convertView==null)
            v = inflater.inflate(R.layout.list_book_detail, null);

        TextView list_detaillist_txt_pnr = (TextView)v.findViewById(R.id.list_detaillist_txt_pnr);
        TextView list_detaillist_txt_edate = (TextView) v.findViewById(R.id.list_detaillist_txt_edate);

        TextView list_detaillist_txt_etime = (TextView) v.findViewById(R.id.list_detaillist_txt_etime);
        TextView list_detaillist_txt_efrom = (TextView) v.findViewById(R.id.list_detaillist_txt_efrom);
        TextView list_detaillist_txt_eto = (TextView) v.findViewById(R.id.list_detaillist_txt_eto);
        TextView list_detaillist_txt_enos = (TextView) v.findViewById(R.id.list_detaillist_txt_enos);

        HashMap<String, String> result = new HashMap<String, String>();
        result = data.get(position);

        list_detaillist_txt_pnr.setText(result.get(Bookdetail_Activity.TAG_PNR));
        list_detaillist_txt_edate.setText(result.get(Bookdetail_Activity.TAG_EDATE));
        list_detaillist_txt_etime.setText(result.get(Bookdetail_Activity.TAG_ETIME));
        list_detaillist_txt_efrom.setText(result.get(Bookdetail_Activity.TAG_EFROM));
        list_detaillist_txt_eto.setText(result.get(Bookdetail_Activity.TAG_ETO));
        list_detaillist_txt_enos.setText(result.get(Bookdetail_Activity.TAG_ENOS));

        return v;

    }


}
