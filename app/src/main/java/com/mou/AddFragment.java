package com.mou;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AddFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private List<String> mParam3 = new ArrayList<>();
    private View rootView;
    private Spinner spinner;
    private final String TAG = "wxx";
    private AddNewFolderDialog addNewFolderDialog;
    private DialogCallBack mDialogCallBack;
    private CommitCallBack mCommitCallBack;
    private String spinner_item;

    public AddFragment() {
        // Required empty public constructor
    }

    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddFragment newInstance(List<String> param1) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("string", (ArrayList<String>) param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof DialogCallBack)) {
            throw new IllegalStateException("FirstFragment对应的Activity没有实现FirstCallBack接口！");

        } else {
            mDialogCallBack = (DialogCallBack) context;
            mCommitCallBack = (CommitCallBack) context;
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getStringArrayList("string");
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_add, container, false);
        }
        //Log.d("wxx", "onCreateView: ");
        initSpinner();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.spinner);
        View.OnClickListener onClickListener = v -> {
            Log.d(TAG, "onViewCreated: 点击监听");
            //获取dialog的操作信息
            switch (v.getId()) {
                case R.id.add_new_folder_dialog_confirm:
                    EditText editText = addNewFolderDialog.findViewById(R.id.add_new_folder_dialog_edit);
                    if (editText.getText().toString().isEmpty()) {
                        addNewFolderDialog.dismiss();
                    } else {
                        mDialogCallBack.onDialogCallBack(editText.getText().toString());
                        addNewFolderDialog.dismiss();
                        ArrayAdapter spinnerAdapter = (ArrayAdapter) spinner.getAdapter();
                        spinnerAdapter.remove("新建");
                        spinnerAdapter.add(editText.getText().toString());
                        spinnerAdapter.add("新建");
                    }
                    break;
                case R.id.add_new_folder_dialog_cancel:
                    addNewFolderDialog.dismiss();
                    break;
            }
        };
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_item = String.valueOf(parent.getSelectedItem());
                //Log.d(TAG, "onItemSelected: " + item);
                if (spinner_item.equals("新建")) {
                    addNewFolderDialog = new AddNewFolderDialog(getActivity(), R.style.NewDialogTheme, onClickListener);
                    addNewFolderDialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button btn_commit = view.findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit_title = view.findViewById(R.id.edit_title);
                EditText edit_component = view.findViewById(R.id.edit_component);
                //传递三个字符串：两个编辑框，一个spinner文件夹信息
                List<String> string_list=new ArrayList<>();
                string_list.add(edit_title.getText().toString());
                string_list.add(edit_component.getText().toString());
                string_list.add(spinner_item);
                mCommitCallBack.onCommitCallBack(string_list);
                edit_title.setText(null);
                edit_component.setText(null);
            }
        });
    }

    private void initSpinner() {
        Spinner spinner = rootView.findViewById(R.id.spinner);
        String[] spin_text = {"默认", "新建"};
        //String[] tmp = append(spin_text, "默认");
        if (mParam3.isEmpty()) Log.d("wxx", "initSpinner: mParam3为空");
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, mParam3);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (spinner != null) {
            spinner.setAdapter(adapter);
            //Log.d("wxx", "onCreate: 已设置");
        } else {
            //Log.d("wxx", "onCreate: spinner为空");
        }
    }

}