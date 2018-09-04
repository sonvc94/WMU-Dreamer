package top3haui.c.interviewjava.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import top3haui.c.interviewjava.Controller.AdapterSetting;
import top3haui.c.interviewjava.R;
import top3haui.c.interviewjava.models.ItemBackground;

/**
 * Created by VanCuong on 26/06/2017.
 */

public class ActivitySetting extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterSetting adapterSetting;
    private ArrayList<ItemBackground> itemBackgrounds;
    private LinearLayout layout_setting;
    private int indexItem = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_setting);
        layout_setting = (LinearLayout) findViewById(R.id.layout_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đổi hình nền");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String id = sharedPref.getString("id", null);


        itemBackgrounds = new ArrayList<>();
        itemBackgrounds.add(new ItemBackground("1", "Hình nền 1", R.drawable.dep1));
        itemBackgrounds.add(new ItemBackground("2", "Hình nền 2", R.drawable.dep2));
        itemBackgrounds.add(new ItemBackground("3", "Hình nền 3",R.drawable.dep3));
        itemBackgrounds.add(new ItemBackground("4", "Hình nền 4",R.drawable.dep4));
        if (id != null) {
            for (int i = 0; i < itemBackgrounds.size(); i++) {
                if (itemBackgrounds.get(i).getId().equals(id)) {
                    layout_setting.setBackgroundResource(itemBackgrounds.get(i).getImg());
                    break;
                }
            }

        }
        adapterSetting = new AdapterSetting(this, itemBackgrounds);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterSetting);
        adapterSetting.setClickListener(new AdapterSetting.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                indexItem = position + 1;
//                Toast.makeText(getApplicationContext(),itemBackgrounds.get(position).getId(),Toast.LENGTH_LONG).show();
                adapterSetting.notifyDataSetChanged();
                    layout_setting.setBackgroundResource(itemBackgrounds.get(position).getImg());

            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_savesettings:
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (indexItem == 0) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Lỗi lưu!").
                            setMessage("Bạn chưa chọn hình nền nào").
                            setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                } else {
                    editor.putString("id", itemBackgrounds.get(indexItem - 1).getId() + "");
                    editor.commit();
                    Intent intent = getIntent();
                    //int value= Integer.parseI(cityArrayList.get(i).getName()+"");
                    intent.putExtra("dataid", itemBackgrounds.get(indexItem - 1).getId());
                    setResult(1, intent);
                    finish();
                    return true;
                }

            default:
                return super.onContextItemSelected(item);
        }
    }
}
