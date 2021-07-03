package model;

import view.*;

public class MenuModel {
    private MenuUI v;

    public void registerView(MenuUI v) {
        this.v = v;
    }

    public void requestView() {
        v.requestView();
    }
}
