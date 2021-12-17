package com.pradeep.app;

import android.view.View;

import java.util.List;

public interface ClickListioner {
    public void onItemClick(View v,  final List<User> list ,int position,int type);
}
