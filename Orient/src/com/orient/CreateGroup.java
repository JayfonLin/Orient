package com.orient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class CreateGroup extends AbstractActivityGroup{

    public static CreateGroup createGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewHistory = new ArrayList<View>();
        createGroup = this;

        View decorView = getLocalActivityManager().startActivity("Romm_Second_1", new Intent(this, Room_Second_1.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
        replaceContentView(decorView);
    }
}
