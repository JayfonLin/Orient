package com.orient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class JoinGroup extends AbstractActivityGroup{

    public static JoinGroup BROWSE_GROUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewHistory = new ArrayList<View>();
        BROWSE_GROUP = this;

        View decorView = getLocalActivityManager().startActivity("Romm_Third", new Intent(this, Room_Third.class)).getDecorView();
        replaceContentView(decorView);
    }
}
