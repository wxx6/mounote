package com.mou;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

public class AddNewFolderDialog extends Dialog {
    Activity context;
    private View.OnClickListener clickListener;
    public AddNewFolderDialog(Activity context){
        super(context);
        this.context=context;
    }

    public AddNewFolderDialog(Activity context, int theme, View.OnClickListener clickListener){
        super(context,theme);
        this.context=context;
        this.clickListener=clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.add_new_folder_dialog);
    }
}
