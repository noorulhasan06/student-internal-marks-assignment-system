package com.mcs.du.noorul.smas;

/**
 * Created by Muneeb on 8/16/2016.
 */

public class DataModel {

    private String name;
    private String contact;

    public DataModel(String _name, String _contact) {
        this.name = _name;
        this.contact = _contact;
    }

    public String getName() {
        return name;
    }
    public String getContact() {
        return contact;
    }

}
