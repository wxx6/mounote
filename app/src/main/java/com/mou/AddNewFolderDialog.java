package com.mou;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class AddNewFolderDialog extends Dialog {
    Activity context;
    private View.OnClickListener clickListener;
    private Button confirm_button;
    private Button cancel_button;

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
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        // 获取屏幕宽、高用
        DisplayMetrics d = new DisplayMetrics();
        m.getDefaultDisplay().getMetrics(d);
        // 获取对话框当前的参数值
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        // 宽度设置为屏幕的0.8
        p.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(p);
        confirm_button = findViewById(R.id.add_new_folder_dialog_confirm);
        cancel_button = findViewById(R.id.add_new_folder_dialog_cancel);
        confirm_button.setOnClickListener(clickListener);
        cancel_button.setOnClickListener(clickListener);
    }
}
