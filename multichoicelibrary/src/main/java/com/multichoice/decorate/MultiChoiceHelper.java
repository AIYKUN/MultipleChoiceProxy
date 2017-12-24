package com.multichoice.decorate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 多选帮助类
 */
public class MultiChoiceHelper implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    private static final String SELECTION_MODE = "SELECTION_MODE";
    private static final String BUNDLE_KEY = "list__selection";

    //单选模式-多选模式
    private SelectMode mMultiMode = SelectMode.NONE;
    private final Set<Long> checkedItems = new HashSet<Long>();

    private ItemCheckableProxy mItemCheckable;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;
    private OnSelectedChangeListener mOnSelectedChangeListener = null;
    private IAdapterNotifyListner mIAdapterNotifyListner;

    public MultiChoiceHelper() {
        mMultiMode = SelectMode.NONE;
    }

    public void saveState(Bundle outState) {
        if(outState == null) {
            return;
        }

        long[] array = new long[checkedItems.size()];
        int i = 0;
        for (Long id : checkedItems) {
            array[i++] = id;
        }
        outState.putLongArray(BUNDLE_KEY, array);
        outState.putInt(SELECTION_MODE, mMultiMode == null ? SelectMode.Multi.code : mMultiMode.code);
    }

    public void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        mMultiMode = SelectMode.findSelectMode(savedInstanceState.getInt(SELECTION_MODE));
        long[] array = savedInstanceState.getLongArray(BUNDLE_KEY);
        checkedItems.clear();
        if (array != null) {
            for (long id : array) {
                setItemChecked(id, true);
            }
        }
    }

    public void setItemChecked(long handle, boolean checked) {
        if(!isSelectFlag()) {
            return;
        }

        if (checked) {
            checkItem(handle);
        } else {
            unCheckItem(handle);
        }
    }

    private void checkItem(long handle) {
        boolean wasSelected = isChecked(handle);
        if (wasSelected) {
            return;
        }
        if(getMultiMode() == SelectMode.Single) {
            checkedItems.clear();
        }
        checkedItems.add(handle);
        notifyDataSetChanged();
        onItemSelectedStateChanged();
    }

    private void unCheckItem(long handle) {
        boolean wasSelected = isChecked(handle);
        if (!wasSelected) {
            return;
        }
        checkedItems.remove(handle);
        notifyDataSetChanged();
        onItemSelectedStateChanged();
    }

    public boolean isChecked(long handle) {
        return checkedItems.contains(handle);
    }

    public int getCheckedItemCount() {
        return checkedItems.size();
    }

    private void notifyDataSetChanged() {
        if(mIAdapterNotifyListner != null && mIAdapterNotifyListner.isAdapterPrepared()) {
            mIAdapterNotifyListner.notifyDataSetChanged();
        }
    }

    private void onItemSelectedStateChanged() {
        if (mOnSelectedChangeListener != null) {
            final int count = getCheckedItemCount();
            mOnSelectedChangeListener.onSelectedChanged(getMultiMode(), count);
        }
    }

    public void setAdapterView(AdapterView<? super BaseAdapter> adapterView) {
        if(adapterView != null) {
            adapterView.setOnItemClickListener(this);
            adapterView.setOnItemLongClickListener(this);
        }
    }

    public void setMultiMode(SelectMode multiMode) {
        setMultiMode(multiMode, true);
    }

    public void setMultiMode(SelectMode multiMode, boolean cleanFlag) {
        if(multiMode == null) {
            mMultiMode = SelectMode.NONE;
        } else {
            mMultiMode = multiMode;
        }

        if (SelectMode.NONE == getMultiMode() || cleanFlag) {
            checkedItems.clear();
        }

        onItemSelectedStateChanged();
        notifyDataSetChanged();
    }

    public void cancelSelected() {
        if(isSelectFlag()) {
            checkedItems.clear();
            notifyDataSetChanged();
            onItemSelectedStateChanged();
        }
    }

    public SelectMode getMultiMode() {
        if(mMultiMode == null) {
            mMultiMode = SelectMode.NONE;
        }

        return mMultiMode;
    }

    public boolean isSelectFlag() {
        return getMultiMode() != SelectMode.NONE;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(isSelectFlag()) {
            boolean isHandled = false;
            if (mItemCheckable != null && mItemCheckable.isItemCheckable(position)) {
                isHandled = mItemCheckable.onItemClicked(position, id, isChecked(position));
            }

            if (!isHandled && mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(parent, view, position, id);
            }
        } else if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(parent, view, position, id);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(isSelectFlag()) {
            boolean isHandled = false;

            if (mItemCheckable != null && mItemCheckable.isItemCheckable(position)) {
                isHandled = mItemCheckable.onItemLongClicked(position, id, isChecked(position));
            }

            if (!isHandled && mOnItemLongClickListener != null) {
                return mOnItemLongClickListener.onItemLongClick(parent, view, position, id);
            }
            return isHandled;
        } else if (mOnItemLongClickListener != null) {
            return mOnItemLongClickListener.onItemLongClick(parent, view, position, id);
        } else {
            return false;
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener listener) {
        mOnSelectedChangeListener = listener;
    }

    public void setItemCheckableProxy(ItemCheckableProxy itemcheckable) {
        mItemCheckable = itemcheckable;
    }

    public void setIAdapterNotifyListner(IAdapterNotifyListner listner) {
        mIAdapterNotifyListner = listner;
    }

    public void selectCount(final long totalCount, final long limitCount) {
        if(!isSelectFlag()) {
            return;
        }
        if (getMultiMode() == SelectMode.Single) {
            checkedItems.clear();
            if (totalCount > 0 && limitCount > 0) {
                checkedItems.add(0L);
            }
        } else {
            for (long i = 0; i < totalCount; ++i) {
                if (i >= limitCount) {
                    break;
                }
                checkedItems.add(i);
            }
        }

        if(totalCount > 0 && limitCount > 0) {
            notifyDataSetChanged();
            onItemSelectedStateChanged();
        }
    }

    public Set<Long> getCheckedItemIds() {
        return new HashSet<>(checkedItems);
    }

    public List<Long> getOrderedCheckedItemIds() {
        List<Long> ids = new ArrayList<>(checkedItems);
        Collections.sort(ids);
        return ids;
    }
}
