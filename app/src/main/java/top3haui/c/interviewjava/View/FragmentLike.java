package top3haui.c.interviewjava.View;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import top3haui.c.interviewjava.Controller.AdapterFragmentLike;
import top3haui.c.interviewjava.R;
import top3haui.c.interviewjava.models.Categorys;
import top3haui.c.interviewjava.models.ItemBackground;
import top3haui.c.interviewjava.models.Items;
import top3haui.c.interviewjava.models.SQLite;
import top3haui.c.interviewjava.models.SQLiteDataController;
import top3haui.c.interviewjava.models.SwipeDismissListViewTouchListener;

/**
 * Created by VanCuong on 23/06/2017.
 */

public class FragmentLike extends Fragment {

    private ArrayList<Items> item;
    private ArrayList<Categorys> categoryses;
    // private RecyclerView recyclerView;
    private AdapterFragmentLike adapterView;
    private HashMap<Items, List<String>> mChild;
    private ExpandableListView expandableListView;
    private TextView tv_like_checkshow;
    private ArrayList<ItemBackground> itemBackgrounds;
    RelativeLayout layout_like;
    private AdView avBanner;
    private AdRequest adRequest;
    public FragmentLike() {
        super();
    }

    public static FragmentLike newInstance() {
        FragmentLike fragment = new FragmentLike();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_like, null);
        layout_like= (RelativeLayout) root.findViewById(R.id.layout_like);
        expandableListView = (ExpandableListView) root.findViewById(R.id.expand_list_like);
        mChild = new HashMap<>();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mục yêu thích");
        tv_like_checkshow = (TextView) root.findViewById(R.id.tv_like_checkshow);

         createDB();
        getList();
        changeBGR();
        createDisplay();
        avBanner = (AdView) root.findViewById(R.id.adView_like);
        adRequest = new AdRequest.Builder().build();

        //load ads
        avBanner.loadAd(adRequest);
        return root;
        // return inflater.inflate(R.layout.activity_category, container, false);
    }
public void createDisplay(){
    Handler handler = new Handler();
    final Runnable r = new Runnable() {
        public void run() {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;
            expandableListView.setIndicatorBounds(width - dp2px(50), width - dp2px(10));
        }
    };
    handler.postDelayed(r, 0);
}
    private void changeBGR() {
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                itemBackgrounds = new ArrayList<>();
                itemBackgrounds.add(new ItemBackground("1", "Hình nền 1", R.drawable.dep1));
                itemBackgrounds.add(new ItemBackground("2", "Hình nền 2", R.drawable.dep2));
                itemBackgrounds.add(new ItemBackground("3", "Hình nền 3", R.drawable.dep3));
                itemBackgrounds.add(new ItemBackground("4", "Hình nền 4", R.drawable.dep4));
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String id = sharedPref.getString("idbgl", null);
                if (id != null) {
                    for (int i = 0; i < itemBackgrounds.size(); i++) {
                        if (itemBackgrounds.get(i).getId().equals(id)) {
                            layout_like.setBackgroundResource(itemBackgrounds.get(i).getImg());
                            break;
                        }
                    }
                }
            }
        };
        handler.postDelayed(r, 0);

    }
    private void getList() {
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                SQLite like = new SQLite(getActivity());
                item = new ArrayList<>();
                categoryses = new ArrayList<>();
                categoryses = like.getListCategory();

                ArrayList<Items> list = new ArrayList<>();

                list = like.getListLike();
                if (list.size() <= 0) {
                    tv_like_checkshow.setVisibility(View.VISIBLE);
                } else tv_like_checkshow.setVisibility(View.GONE);
                for (Items s : list) {
                    ArrayList<String> childList = new ArrayList<>();
                    childList.add(s.getInformation());
                    item.add(s);
                    mChild.put(s, childList);
                }
                setAdapterView();
                adapterView.mListener = new AdapterFragmentLike.OnImageClickListener() {
                    @Override
                    public void onImageClicked() {
                        getList();
                        adapterView.notifyDataSetChanged();
                    }
                };
            }
        };
        handler.postDelayed(r, 0);
        //getActivity().getActionBar().setTitle(categoryses.get(pageNumber-1).getValue());
    }
    private void createDB() {
// khởi tạo database
        SQLiteDataController sql = new SQLiteDataController(getActivity());
        try {
            sql.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setAdapterView() {

        adapterView = new AdapterFragmentLike(getActivity(), item, mChild);
        expandableListView.setAdapter(adapterView);

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        expandableListView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ExpandableListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    adapterView.Xoa(position);
                                    adapterView.notifyDataSetChanged();
                                }
                            }
                        });
        expandableListView.setOnTouchListener(touchListener);
    }

    public int dp2px(float dp) {
        // Get the screen's density scale
        final float density = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * density + 0.5f);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(getActivity(), ActivitySetting.class), 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {

                case 1:
                    String background = data.getStringExtra("dataid");
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("idbgl", background + "");
                    editor.commit();

                    if (background != null) {

                        for (int i = 0; i < itemBackgrounds.size(); i++) {
                            if (itemBackgrounds.get(i).getId().equals(background)) {
                                layout_like.setBackgroundResource(itemBackgrounds.get(i).getImg());
                                break;
                            }
                        }
                    }
                    break;
            }
        }
    }

}
