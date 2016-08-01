package com.banyan.mss;

        import java.util.ArrayList;

        import android.app.Activity;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

public class CustomGridViewAdapter extends ArrayAdapter<Item>
{

    Context context;
    int layoutResourceId;
    ArrayList<Item> data = new ArrayList<Item>();

    public CustomGridViewAdapter(Context context, int layoutResourceId, ArrayList<Item> data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        RecordHolder holder = null;

        try
        {
            if (row == null)
            {


                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new RecordHolder();
                holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
                holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
                row.setTag(holder);
            }
            else
            {
                holder = (RecordHolder) row.getTag();
            }

            Item item = data.get(position);
            holder.txtTitle.setText(item.getTitle());
            holder.imageItem.setImageBitmap(item.getImage());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        if(position==0 ||position==1 || position==2||position==3||position==4 ||position==6|| position==7|| position==12|| position==17 || position==22|| position==27|| position==32 ||position==37||position==42||position==47)
        {
            return false;
        }
        else
        {
            return true;
        }


    }


    public static class RecordHolder
    {
        public TextView txtTitle;
        public ImageView imageItem;

    }
}