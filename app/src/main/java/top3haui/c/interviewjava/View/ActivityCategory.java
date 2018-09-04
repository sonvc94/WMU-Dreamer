package top3haui.c.interviewjava.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import top3haui.c.interviewjava.Controller.AdapterCategory;
import top3haui.c.interviewjava.R;
import top3haui.c.interviewjava.models.Categorys;
import top3haui.c.interviewjava.models.SQLite;

/**
 * Created by VanCuong on 24/06/2017.
 */

public class ActivityCategory extends AppCompatActivity {
    private ArrayList<Categorys> categoryses;
    private AdapterCategory adapter;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mục lục");
        listView= (ListView) findViewById(R.id.list_category);
        getList();
    }
    private void getList() {
        SQLite like = new SQLite(this);
        categoryses=new ArrayList<>();
        categoryses= (ArrayList<Categorys>) getIntent().getSerializableExtra("arrcategory");
        adapter=new AdapterCategory(categoryses,this);
        // acTextView.setAdapter(adapter);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sendToMain(2,i);
            }
        });
    }
    public void sendToMain(int resultcode, int i)
    {
        Intent intent=getIntent();

        //int value= Integer.parseI(cityArrayList.get(i).getName()+"");
        intent.putExtra("data", categoryses.get(i));
        setResult(resultcode, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
            default:
                return super.onContextItemSelected(item);
        }
    }
}
