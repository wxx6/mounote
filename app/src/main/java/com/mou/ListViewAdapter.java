package com.mou;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private final String TAG ="wxx";
    private List<String> mTitleList;
    private List<String> mComponentList;

    public ListViewAdapter(List<String> titleList, List<String> componentList) {
        mTitleList = titleList;
        mComponentList = componentList;
    }

    @Override
    public int getCount() {
        return mTitleList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView textView1 = convertView.findViewById(R.id.listView_tv1);
        String text = mTitleList.get(position);
        textView1.setText(text);
        TextView textView2 = convertView.findViewById(R.id.listView_tv2);
        text = mComponentList.get(position);
        textView2.setText(text);

        LinearLayout linearLayout = convertView.findViewById(R.id.listView_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("敬请期待");
            }
        });
        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterItems(String searchText) {
        List<String> mmTitle = new ArrayList<>();
        List<String> mmComponent = new ArrayList<>();
        for (String item : mTitleList) {
            if(TextUtils.isEmpty(searchText)){
                Log.d(TAG, "filterItems: 文本为空");
            }else Log.d(TAG, "filterItems: 文本不为空");
            if (item.toLowerCase().contains(searchText)|| TextUtils.isEmpty(searchText)) {
                mmTitle.add(item);
                int index = mTitleList.indexOf(item);
                mmComponent.add(mComponentList.get(index));
            }
        }

        mTitleList = mmTitle;
        mComponentList = mmComponent;
        notifyDataSetChanged();
    }
}
