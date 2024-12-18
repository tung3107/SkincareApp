package btl.skincareapp.model;

import java.io.Serializable;
import java.util.List;

public class Routine implements Serializable {
    private int id, user_id;
    private String name, time;

    private List<MyProduct> productList;


    public Routine() {}
    public Routine(int id, int user_id, String name, String time,List<MyProduct> productList) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.productList = productList;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MyProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<MyProduct> productList) {
        this.productList = productList;
    }
}
