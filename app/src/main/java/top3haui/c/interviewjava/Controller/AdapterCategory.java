package top3haui.c.interviewjava.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import top3haui.c.interviewjava.R;
import top3haui.c.interviewjava.models.Categorys;

/**
 * Created by VanCuong on 24/06/2017.
 */

public class AdapterCategory extends BaseAdapter {

    public class ViewHolder {
        TextView txtTitle;

    }

    public ArrayList<Categorys> categorysArrayList;

    public Context context;
    ArrayList<Categorys> arraylist;

    public AdapterCategory(ArrayList<Categorys> countries, Context context) {
        this.context = context;
        categorysArrayList =countries;
        arraylist = new ArrayList<Categorys>();
        arraylist.addAll(categorysArrayList);
        // arraylist.addAll(parkingList);

    }

    @Override
    public int getCount() {
        return categorysArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_category, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.tv_name_category);

            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTitle.setText(categorysArrayList.get(position).getValue());
        //viewHolder.txtSubTitle.setText(parkingList.get(position).getPostSubTitle() + "");
        return rowView;


    }

//    public void filter(String charText) {
//
//        charText = charText.toLowerCase(Locale.getDefault());
//
//        parkingList.clear();
//        if (charText.length() == 0) {
//            parkingList.addAll(arraylist);
//
//        } else {
//            for (Categorys c : arraylist) {
//                if (charText.length() != 0 && c.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    parkingList.add(c);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
}
