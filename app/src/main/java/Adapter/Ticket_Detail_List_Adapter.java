package Adapter;

import com.banyan.mss.Bookdetail_Activity;
import com.banyan.mss.R;
import com.banyan.mss.Ticketdetail_Activity;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class Ticket_Detail_List_Adapter extends BaseAdapter{
    private Activity activity;
    private Context context;
    private TextView bd,bd_time,bd_seat_no,bd_name,bd_sex,bd_age,bd_famt,bd_samt,bd_amt;

    private ArrayList<HashMap<String, String>> data;
    private ArrayList<String> drop;
    private static LayoutInflater inflater=null;

    public Ticket_Detail_List_Adapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            v = inflater.inflate(R.layout.list_ticket_detail, null);

        bd = (TextView)v.findViewById(R.id.list_ticket_txt_id);
        bd_time = (TextView)v.findViewById(R.id.list_ticket_txt_time);
        bd_seat_no = (TextView)v.findViewById(R.id.list_ticket_txt_seat_no);

        bd_name = (TextView)v.findViewById(R.id.list_ticket_txt_name);
        bd_sex = (TextView)v.findViewById(R.id.list_ticket_txt_sex);
        bd_age = (TextView)v.findViewById(R.id.list_ticket_txt_age);

        bd_famt = (TextView)v.findViewById(R.id.list_ticket_txt_famt);
        bd_samt = (TextView)v.findViewById(R.id.list_ticket_txt_samt);
        bd_amt = (TextView)v.findViewById(R.id.list_ticket_txt_amt);

        HashMap<String, String> result = new HashMap<String, String>();
        result = data.get(position);

        bd.setText("ID:\t\t"+result.get(Ticketdetail_Activity.TAG_BD));
        bd_time.setText(result.get(Ticketdetail_Activity.TAG_TIME));
        bd_seat_no.setText(result.get(Ticketdetail_Activity.TAG_SEATNO));
        bd_name.setText(result.get(Ticketdetail_Activity.TAG_NAME));
        bd_sex.setText(result.get(Ticketdetail_Activity.TAG_SEX));
        bd_age.setText("Age\t:\t\t"+result.get(Ticketdetail_Activity.TAG_AGE));
        bd_famt.setText("F_Amt\t:\t\t"+result.get(Ticketdetail_Activity.TAG_FAMT));
        bd_samt.setText("S_Amt\t:\t\t"+result.get(Ticketdetail_Activity.TAG_SAMT));
        bd_amt.setText("T_Amt\t:\t\t"+result.get(Ticketdetail_Activity.TAG_AMT));

        return v;

    }


}
