package top3haui.c.interviewjava.View;


import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import top3haui.c.interviewjava.Controller.AdapterView;
import top3haui.c.interviewjava.R;
import top3haui.c.interviewjava.models.Categorys;
import top3haui.c.interviewjava.models.ItemBackground;
import top3haui.c.interviewjava.models.Items;
import top3haui.c.interviewjava.models.SQLite;
import top3haui.c.interviewjava.models.SQLiteDataController;


/**
 * Created by VanCuong on 23/06/2017.
 */

public class FragmentHome extends Fragment {
    private ArrayList<Items> item;
    private ArrayList<Categorys> categoryses;
    private RelativeLayout layout_home;
    private AdapterView adapterView;
    private Button pre, next, btn_category;
    private HashMap<Items, List<String>> mChild;
    private ExpandableListView expandableListView;
    private int pageNumber = 1, categogySize = 0;
    private ArrayList<ItemBackground> itemBackgrounds;
    private AdView avBanner;
    private AdRequest adRequest;
    public static FragmentHome newInstance() {

        FragmentHome fragment = new FragmentHome();
        return fragment;
    }

//    public FragmentHome(ArrayList<Items> litsParent, HashMap<Items, List<String>> litsParent) {
//    }
    @SuppressLint("ValidFragment")
    public FragmentHome(ArrayList<Items> litsParent, HashMap<Items, List<String>> litmChild,ArrayList<Categorys> listcategoryses) {
        super();
        item=new ArrayList<>();
        mChild=new HashMap<>();
        this.item = litsParent;
        this.mChild = litmChild;
        this.categoryses=listcategoryses;
    }

    public FragmentHome(){
    super();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_main, null);
        layout_home = (RelativeLayout) root.findViewById(R.id.layout_home);
        pre = (Button) root.findViewById(R.id.btn_preview);
        next = (Button) root.findViewById(R.id.btn_next);
        btn_category = (Button) root.findViewById(R.id.btn_category);
        changeBGR();
        expandableListView = (ExpandableListView) root.findViewById(R.id.expand_list);
        mChild = new HashMap<>();
        createDB();
        //getList();

        bindData();
        createDisplay();
//        DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int width = metrics.widthPixels;
//        expandableListView.setIndicatorBounds(width - dp2px(50), width - dp2px(10));
        avBanner = (AdView) root.findViewById(R.id.adView_main);
        adRequest = new AdRequest.Builder().build();

        //load ads
        avBanner.loadAd(adRequest);
        btnClick();
        return root;

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
    private void btnClick() {
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageNumber == 1) {
                    pre.setText("Đến trang cuối");
                    pageNumber = categogySize;

                    bindData();
                    adapterView.notifyDataSetChanged();
                } else {
                    pre.setText("Quay về");
                    pageNumber = pageNumber - 1;

                    bindData();
                    adapterView.notifyDataSetChanged();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageNumber == (categogySize)) {
//                    final Toast toast = Toast.makeText(getActivity(), "Bạn đang ở trang cuối", Toast.LENGTH_SHORT);
//                    toast.show();
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            toast.cancel();
//                        }
//                    }, 500);
                    next.setText("Đến trang đầu");
                    pageNumber = 1;
                    bindData();
                    adapterView.notifyDataSetChanged();
                } else {
                    next.setText("Tiếp theo");
                    pageNumber = pageNumber + 1;
                    bindData();
                    adapterView.notifyDataSetChanged();
                }
            }
        });
        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ActivityCategory.class);
                i.putExtra("arrcategory",categoryses);
                startActivityForResult(i, 1);
            }
        });
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
                String id = sharedPref.getString("idbg", null);
                if (id != null) {
                    for (int i = 0; i < itemBackgrounds.size(); i++) {
                        if (itemBackgrounds.get(i).getId().equals(id)) {
                            layout_home.setBackgroundResource(itemBackgrounds.get(i).getImg());
                            break;
                        }
                    }
                }
            }
        };
        handler.postDelayed(r, 0);

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

    private void getList() {
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                SQLite like = new SQLite(getActivity());
                item = new ArrayList<>();
                categoryses = new ArrayList<>();
                categoryses = like.getListCategory();
                categogySize = categoryses.size();
                ArrayList<Items> list = new ArrayList<>();

                list = like.getList();
                for (Items s : list) {
                    ArrayList<String> childList = new ArrayList<>();
                    childList.add(s.getInformation());
                    item.add(s);
                    mChild.put(s, childList);
                }
            }
        };
        handler.postDelayed(r, 0);
    }

    private void bindData() {
        categogySize = categoryses.size();
        Handler handler1 = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                if (pageNumber == 1) {
                    pre.setText("Đến trang cuối");
                } else {
                    pre.setText("Quay về");
                }
                if (pageNumber == (categogySize)) {
                    next.setText("Đến trang đầu");
                } else {
                    next.setText("Tiếp theo");
                }
                ArrayList<Items> listSub = new ArrayList<>();
                HashMap<Items, List<String>> mSubChild = new HashMap<>();
                for (int i = 0; i < item.size(); i++) {
                    if (item.get(i).getCategogy().equals(pageNumber + "")) {

                        ArrayList<String> childList = new ArrayList<>();
                        childList.add(item.get(i).getInformation());
                        listSub.add(item.get(i));
                        mSubChild.put(item.get(i), childList);
                    }
                }
                adapterView = new AdapterView(getActivity(), listSub, mSubChild);
                expandableListView.setAdapter(adapterView);
                final Toast toast = Toast.makeText(getActivity(), categoryses.get(pageNumber - 1).getValue(), Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        toast.cancel();
                    }
                }, 500);
                //getActivity().getActionBar().setTitle(categoryses.get(pageNumber-1).getValue());
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(categoryses.get(pageNumber - 1).getValue());
            }
        };
        handler1.postDelayed(r, 10);


    }

    public int dp2px(float dp) {
        // Get the screen's density scale
        final float density = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * density + 0.5f);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case 2:
                    //value from InputActivity
                    Categorys categorys = (Categorys) data.getSerializableExtra("data");
                    pageNumber = Integer.parseInt(categorys.getId());
                    bindData();
//                    Toast.makeText(getActivity().getApplicationContext(), v.getId(), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    String background = data.getStringExtra("dataid");
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("idbg", background + "");
                    editor.commit();

                    if (background != null) {

                        for (int i = 0; i < itemBackgrounds.size(); i++) {
                            if (itemBackgrounds.get(i).getId().equals(background)) {
                                layout_home.setBackgroundResource(itemBackgrounds.get(i).getImg());
                                break;
                            }
                        }
                    }
                    break;
            }
        }
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
}
