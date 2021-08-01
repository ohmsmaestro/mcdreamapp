package com.stetis.utils;




import com.stetis.entities.Item;
import com.stetis.entities.Staff;

import java.util.ArrayList;


/**
 * Created by jante on 8/1/15.
 */
public class SinglePresent {
    private boolean backgroundactive;
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Staff> staff = new ArrayList<>();

    public boolean isBackgroundactive() {
        return backgroundactive;
    }

    public void setBackgroundactive(boolean backgroundactive) {
        this.backgroundactive = backgroundactive;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Staff> getStaff() {
        return staff;
    }

    public void setStaff(ArrayList<Staff> staff) {
        this.staff = staff;
    }
}
