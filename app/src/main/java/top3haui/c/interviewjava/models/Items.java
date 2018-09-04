package top3haui.c.interviewjava.models;



import java.util.List;

/**
 * Created by VanCuong on 22/06/2017.
 */

public class Items  {
    private int id;
    private String title;
    private String information;
    private String type;
    private String categogy;
    private List<Object> mChildrenList;
    public Items(int id) {
        this.id = id;
    }

    public Items(int id, String title, String information, String type, String categogy) {
        this.id = id;
        this.title = title;
        this.information = information;
        this.categogy = categogy;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getCategogy() {
        return categogy;
    }

    public void setCategogy(String categogy) {
        this.categogy = categogy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }






}
