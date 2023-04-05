package com.mou;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements TextWatcher {

    private static final String TAG = "wxx";
    private List<String> mTitleList = new ArrayList<>();
    View rootView;
    private List<String> mComponentList = new ArrayList<>();
    private ListViewAdapter adapter;
    private ListView listView;
    private ScrollCallBack mScrollCallBack;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(List<String> param1, List<String> param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("title", (ArrayList<String>) param1);
        args.putStringArrayList("component", (ArrayList<String>) param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof ScrollCallBack)) {
            throw new IllegalStateException("没有实现ScrollCallBack接口");
        } else {
            mScrollCallBack = (ScrollCallBack) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitleList = getArguments().getStringArrayList("title");
            mComponentList = getArguments().getStringArrayList("component");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
        }
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 当用户点击非 EditText 区域时，隐藏输入法
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View focusedView = getActivity().getCurrentFocus();
                    if (focusedView instanceof EditText) {
                        Rect outRect = new Rect();
                        focusedView.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            focusedView.clearFocus();
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });
        EditText editText = rootView.findViewById(R.id.search);
        editText.addTextChangedListener(this);
        listView = rootView.findViewById(R.id.listView);
        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mScrollCallBack.onScrollCallBack(mTitleList, mComponentList);
                initHome();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        initHome();
        return rootView;
    }

    private void initHome() {
        //LinearLayout layout= rootView.findViewById(R.id.layout_database_scroll_view);

        adapter = new ListViewAdapter(mTitleList, mComponentList);
        listView.setAdapter(adapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String searchText = s.toString();
        adapter.filterItems(searchText);
        Log.d(TAG, "onTextChanged: " + s);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString())) {
            initHome();

        }
    }

}