package top3haui.c.interviewjava.models;

import java.io.Serializable;

/**
 * Created by VanCuong on 23/06/2017.
 */

public class Categorys implements Serializable {
    private String id;
    private String value;

    public Categorys(String id, String value) {
        this.id = id;
        this.value = value;
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
}
