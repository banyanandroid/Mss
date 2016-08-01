package Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.banyan.mss.Item;
import com.banyan.mss.R;

public class Sleeper32_GridviewAdapter extends ArrayAdapter<Item>
{

	Context context;
	int layoutResourceId;
	ArrayList<Item> data = new ArrayList<Item>();

	public Sleeper32_GridviewAdapter(Context context, int layoutResourceId, ArrayList<Item> data)
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
		if(position==0 ||position==1 || position==2||position==3||position==5|| position==9|| position==13|| position==17 || position==21|| position==24|| position==26|| position==27|| position==28|| position==29|| position==30|| position==31|| position==33|| position==37|| position==41|| position==45|| position==49|| position==52|| position==54|| position==55)
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