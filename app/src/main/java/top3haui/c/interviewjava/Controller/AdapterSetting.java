package top3haui.c.interviewjava.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import top3haui.c.interviewjava.R;
import top3haui.c.interviewjava.models.ItemBackground;

/**
 * Created by VanCuong on 26/06/2017.
 */

public class AdapterSetting extends RecyclerView.Adapter<AdapterSetting.RecyclerViewHolder> {
    private ClickListener clickListener;
    private Context context;
    private ArrayList<ItemBackground> arraylist;

    private String idSelect = "";


    public AdapterSetting(Context context, ArrayList<ItemBackground> arraylist, ClickListener clickListener) {
        this.clickListener = clickListener;
        this.context = context;
        this.arraylist = arraylist;
    }

    public AdapterSetting(Context context, ArrayList<ItemBackground> arraylist) {

        this.context = context;
        this.arraylist = arraylist;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_setting, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterSetting.RecyclerViewHolder holder, int position) {
        final ItemBackground itemBackground = arraylist.get(position);
        holder.txtTitle.setText(itemBackground.getValue());
        if (itemBackground.getId().equals(idSelect)) {
            holder.checkBox.setChecked(true);
            holder.item_layout_setting.setBackgroundResource(R.color.colorcam1);
        } else {
            holder.checkBox.setChecked(false);
            holder.item_layout_setting.setBackgroundResource(R.color.colortransparent);
        }

    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle;
        CheckBox checkBox;
        RelativeLayout item_layout_setting;

        public RecyclerViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            txtTitle = (TextView) v.findViewById(R.id.item_title_setting);
            checkBox = (CheckBox) v.findViewById(R.id.cb_setting);
            item_layout_setting = (RelativeLayout) v.findViewById(R.id.item_layout_setting);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
            idSelect = arraylist.get(this.getLayoutPosition()).getId();
        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }
}
