package top3haui.c.interviewjava.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;

public class SQLite extends SQLiteDataController {

    public SQLite(Context con) {
        super(con);
    }



    public ArrayList<Items> getList() {
        ArrayList<Items> list = new ArrayList<>();
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select id,title,information,type,category from QS200", null);
            Items item;
            while (cs.moveToNext()) {
                item = new Items(Integer.parseInt(cs.getString(0)), cs.getString(1), cs.getString(2), cs.getString(3), cs.getString(4));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return list;
    }
    public ArrayList<Categorys> getListCategory() {
        ArrayList<Categorys> list = new ArrayList<>();
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select id,value from category", null);
            Categorys item;
            while (cs.moveToNext()) {
                item = new Categorys(cs.getString(0), cs.getString(1));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }
    public ArrayList<Items> getListLike() {
        ArrayList<Items> list = new ArrayList<>();
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select id,title,information,type,category from QSLike", null);
            Items item;
            while (cs.moveToNext()) {
                item = new Items(Integer.parseInt(cs.getString(0)), cs.getString(1), cs.getString(2), cs.getString(3), cs.getString(4));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }
    public boolean insertLike(Items items) {
        boolean result = false;
        try {

            openDataBase();
            ContentValues values = new ContentValues();
            values.put("id", items.getId());
            values.put("title", items.getTitle().trim());
            values.put("information", items.getInformation().trim());
//            values.put("type", items.getType().trim());
            values.put("category", items.getCategogy().trim());

            long rs = database.insert("QSLike", null, values);
            if (rs > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    public boolean deleteLike(int id) {
        boolean result = false;
        try {

            openDataBase();
            //
            int rs = database.delete("QSLike", "id=" + id, null);
            if (rs > 0) {
                result = true;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }
    public boolean isItemLikeHave(String id) {
       boolean check=false;
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select* from QSLike where id="+id, null);
            Items item;
            if(cs.getCount()!=0){
                check=true;
            }
            else check=false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return check;
    }
}
