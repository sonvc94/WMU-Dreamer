package top3haui.c.interviewjava.models;

import java.io.Serializable;

/**
 * Created by VanCuong on 26/06/2017.
 */

public class ItemBackground implements Serializable{
    private String id;
    private String value;
    private int Img;

    public ItemBackground(String id, String value, int img) {
        this.id = id;
        this.value = value;
        Img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getImg() {
        return Img;
    }

    public void setImg(int img) {
        Img = img;
    }
}
