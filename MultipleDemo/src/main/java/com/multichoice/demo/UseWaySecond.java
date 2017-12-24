package com.multichoice.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.multichoice.decorate.MultiChoiceProxy;
import com.multichoice.decorate.OnSelectedChangeListener;
import com.multichoice.decorate.SelectMode;
import com.multichoice.demo.adapter.BaseAdapterImpl;
import com.multichoice.demo.adapter.StringAdapter;
import com.multichoice.demo.date.ListDates;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aiyongkun on 2017/12/22.
 */
public class UseWaySecond extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, OnSelectedChangeListener {
    TextView select_count;
    ListView mListView;

    List<String> mDates;
    BaseAdapter mBaseAdapter;
    MultiChoiceProxy<String> stringAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list_layout);
        select_count = (TextView) findViewById(R.id.select_count);
        mListView = (ListView) findViewById(R.id.listView);

        stringAdapter = new MultiChoiceProxy<String>(this) {
//            @Override
//            public boolean isItemCheckable(int position) {
//                if(position < 3) {
//                    //todo 前三项不可点击，不能选中
//                    return false;
//                }
//                return super.isItemCheckable(position);
//            }
//
//            @Override
//            public boolean onItemClicked(int position, long id, boolean currentStatus) {
//
//                helper.setItemChecked(position, !currentStatus);
////                return true 设置选中后，true表示已经处理了，否则会执行ITEM的点击事件方法
////                return false 设置选中后，false表示可以后续处理，会执行ITEM的点击事件方法
//                return super.onItemClicked(position, id, currentStatus);
//            }
//
//            @Override
//            public boolean onItemLongClicked(int position, long id, boolean currentStatus) {
//                return super.onItemLongClicked(position, id, currentStatus);
//            }


            @Override
            protected int getLayoutResId() {
                //// TODO: 定制选择状态样式
                return R.layout.layout_checkbox_container_right;
            }
        };
        mDates = new ArrayList<>();
        ListDates.loadTestDates(mDates);

        mBaseAdapter = new StringAdapter(this, mDates);

        stringAdapter.adapterProxy(mBaseAdapter, mListView);

        stringAdapter.setOnSelectedChangeListener(this);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void selectAll(View view) {
        stringAdapter.selectAll();
    }

    public void cancel(View view) {
        stringAdapter.cancelAll();
    }


    public void selectFisrt(View view) {
        stringAdapter.setItemChecked(1, true);
    }

    public void selectThree(View view) {
        stringAdapter.selectCount(3);
    }

    public void multiMode(View view) {
        stringAdapter.setMultiMode(SelectMode.Multi);
    }

    public void singleMode(View view) {
        stringAdapter.setMultiMode(SelectMode.Single);
    }

    public void close(View view) {
        stringAdapter.setMultiMode(SelectMode.NONE);
    }

    boolean haveListener = false;

    public void setListener(View view) {
        if (!haveListener) {
            stringAdapter.setOnItemClickListener(this);
            stringAdapter.setOnItemLongClickListener(this);
            haveListener = true;
        } else {
            stringAdapter.setOnItemClickListener(null);
            stringAdapter.setOnItemLongClickListener(null);
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