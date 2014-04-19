package com.orient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class HistoryGroup extends AbstractActivityGroup{

    public static HistoryGroup historyGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewHistory = new ArrayList<View>();
        historyGroup = this;

        View decorView = getLocalActivityManager().startActivity("Room_First", new Intent(this, Room_First.class)).getDecorView();
        replaceContentView(decorView);
    }
}
