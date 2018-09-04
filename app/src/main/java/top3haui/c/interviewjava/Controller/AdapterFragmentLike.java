package top3haui.c.interviewjava.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import top3haui.c.interviewjava.R;
import top3haui.c.interviewjava.models.Items;
import top3haui.c.interviewjava.models.SQLite;


/**
 * Created by VanCuong on 23/06/2017.
 */

public class AdapterFragmentLike extends BaseExpandableListAdapter {
    private Context mContext;
    private int position=0;
    private List<Items> mParent;
    private HashMap<Items, List<String>> mChild;
    public boolean checkClick = false;
    public OnImageClickListener mListener;
    SQLite sqLite;
    public AdapterFragmentLike(Context context, List<Items> mParent, HashMap<Items, List<String>> mChild) {
        mContext = context;
        this.mParent = mParent;
        this.mChild = mChild;
        sqLite= new SQLite(mContext);
    }
    @Override
    public int getGroupCount() {
        return mParent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChild.get(mParent.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mParent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChild.get(mParent.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(mContext);
            convertView = li.inflate(R.layout.item_row, parent, false);
        }

        TextView tvHeader = (TextView) convertView.findViewById(R.id.item_title);
        tvHeader.setText(mParent.get(groupPosition).getTitle());
        ImageView imAdd= (ImageView) convertView.findViewById(R.id.img_add_like);
        RelativeLayout rela_add= (RelativeLayout) convertView.findViewById(R.id.rela_add);

        final String idItem=mParent.get(groupPosition).getId()+"";
            if (sqLite.isItemLikeHave(idItem)) {

                imAdd.setImageResource(R.drawable.ic_delete);
            } else {

                imAdd.setImageResource(R.drawable.ic_add_ch);
            }

        rela_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setPosition(groupPosition);
                if(sqLite.isItemLikeHave(idItem)==false){
                    Put();
                }else {
                    Xoa(groupPosition);

                }

            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(mContext);
            convertView = li.inflate(R.layout.item_row_child, parent, false);
        }

        TextView tvinformation = (TextView) convertView.findViewById(R.id.child_item_text);

        tvinformation.setText(( getChild(groupPosition, childPosition)).toString());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private  void Put()

    {
        SQLite like = new SQLite(mContext);
        like.insertLike(mParent.get(getPosition()));
        notifyDataSetChanged();
    }
    public void Xoa(int position)
    {
        SQLite like = new SQLite(mContext);

        like.deleteLike(mParent.get(position).getId());
        mListener.onImageClicked();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public interface OnImageClickListener {
        public void onImageClicked();
    }
}
