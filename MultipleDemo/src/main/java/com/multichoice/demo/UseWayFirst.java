package com.multichoice.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.multichoice.decorate.MultiChoiceProxy;
import com.multichoice.decorate.OnSelectedChangeListener;
import com.multichoice.decorate.SelectMode;
import com.multichoice.demo.adapter.StringAdapter;
import com.multichoice.demo.date.ListDates;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aiyongkun on 2017/12/22.
 */
public class UseWayFirst extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, OnSelectedChangeListener {
    TextView select_count;
    ListView mListView;

    List<String> mDates;
    BaseAdapter mBaseAdapter;
    MultiChoiceProxy<String> multiChoiceProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list_layout);
        select_count = (TextView) findViewById(R.id.select_count);
        mListView = (ListView) findViewById(R.id.listView);

        multiChoiceProxy = new MultiChoiceProxy<String>(this);
        mDates = new ArrayList<>();
        ListDates.loadTestDates(mDates);

        mBaseAdapter = new StringAdapter(this, mDates);

        //// TODO: 使用默认选择样式
        multiChoiceProxy.adapterProxy(mBaseAdapter, mListView);

        multiChoiceProxy.setOnSelectedChangeListener(this);

        multiChoiceProxy.restoreState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        multiChoiceProxy.saveState(outState);
    }

    public void onGetSelectItems() {
        List<String> items = multiChoiceProxy.getSelectedItems();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void selectAll(View view) {
        multiChoiceProxy.selectAll();
    }

    public void cancel(View view) {
        multiChoiceProxy.cancelAll();
    }


    public void selectFisrt(View view) {
        multiChoiceProxy.setItemChecked(1, true);
    }

    public void selectThree(View view) {
        multiChoiceProxy.selectCount(3);
    }

    public void multiMode(View view) {
        multiChoiceProxy.setMultiMode(SelectMode.Multi);
    }

    public void singleMode(View view) {
        multiChoiceProxy.setMultiMode(SelectMode.Single);
    }

    public void close(View view) {
        multiChoiceProxy.setMultiMode(SelectMode.NONE);
    }

    boolean haveListener = false;

    public void setListener(View view) {
        if (!haveListener) {
            multiChoiceProxy.setOnItemClickListener(this);
            multiChoiceProxy.setOnItemLongClickListener(this);
            haveListener = true;
        } else {
            multiChoiceProxy.setOnItemClickListener(null);
            multiChoiceProxy.setOnItemLongClickListener(null);
            haveListener = false;
        }
    }

    @Override
    public void onSelectedChanged(SelectMode openFlag, int selectCount) {
        switch (openFlag) {
            case NONE:
                select_count.setText("close :" + selectCount);
                break;
            case Multi:
                select_count.setText("open :" + selectCount);
                break;
            case Single:
                select_count.setText("open :" + selectCount);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        showToast("onItemClick! " + i);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        showToast("onItemLongClick! " + i);
        return true;
    }
}