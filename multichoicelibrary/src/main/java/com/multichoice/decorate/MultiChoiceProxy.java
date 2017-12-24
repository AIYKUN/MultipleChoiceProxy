package com.multichoice.decorate;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 多选-单选-适配器
 */
public class MultiChoiceProxy<TDate> extends BaseAdapter
        implements ItemCheckableProxy,
        IAdapterNotifyListner {

    protected Context mContext;
    private BaseAdapter mBaseAdapter;
    private ItemCheckableProxy mItemCheckableProxy = null;
    protected final MultiChoiceHelper helper;
    private final LayoutInflater mLayoutInflater;
    private boolean useUserViewProduce = true;

    public MultiChoiceProxy(Context context) {
        this.mContext = context;
        helper = new MultiChoiceHelper();
        helper.setIAdapterNotifyListner(this);
        helper.setItemCheckableProxy(this);
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 使用默认选中样式的代理方法
     * @param baseAdapter 被代理的适配器
     * @param adapterView 被代理的列表控件
     */
    public final void adapterProxy(BaseAdapter baseAdapter,
                        AdapterView<? super BaseAdapter> adapterView) {
        adapterProxy(baseAdapter, adapterView, true);
    }

    /**
     * @param baseAdapter 被代理的适配器
     * @param adapterView 被代理的列表控件
     * @param useProxy true 使用默认的选择Item作为外部嵌套, false 使用代理的适配器管理View
     */
    public final void adapterProxy(BaseAdapter baseAdapter,
                                    AdapterView<? super BaseAdapter> adapterView,
                                    boolean useProxy) {
        if(baseAdapter == null) {
            throw new RuntimeException("baseAdapter cannot be null!");
        }
        if(adapterView == null) {
            throw new RuntimeException("adapterView cannot be null!");
        }

        useUserViewProduce = useProxy;
        mBaseAdapter = baseAdapter;
        if(mBaseAdapter instanceof ItemCheckableProxy) {
            setItemCheckable((ItemCheckableProxy) mBaseAdapter);
        } else {
            setItemCheckable(null);
        }
        helper.setAdapterView(adapterView);
        adapterView.setAdapter(this);
    }

    /**
     * @param itemCheckable 点击代理接口，如果设置了该接口，优先执行改接口，如果没有设置该接口，执行代理的默认方法
     */
    public final void setItemCheckable(ItemCheckableProxy itemCheckable) {
        mItemCheckableProxy = itemCheckable;
    }

    ///=========================================================================
    /**
     * 保持选择器的状态
     * @param bundle 序列化对象
     */
    public final void saveState(Bundle bundle) {
        helper.saveState(bundle);
    }

    /**
     * 恢复选择器的状态;
     * 尽量在{@link MultiChoiceProxy#adapterProxy(BaseAdapter, AdapterView)}之后调用，以便刷新界面
     * @param bundle 序列化对象
     */
    public final void restoreState(Bundle bundle) {
        helper.restoreState(bundle);
    }

    /**
     *
     * List中的项目点击监听器
     * @param listener 点击监听器
     */
    public final void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        helper.setOnItemClickListener(listener);
    }

    /**
     * List中的项目长按监听器
     * @param listener 长按监听器
     */
    public final void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        helper.setOnItemLongClickListener(listener);
    }

    /**
     * 切换选择模式
     * @param multiMode 选择模式， 默认清空所选项， 特别的 当multiMode的值为 {@link SelectMode#NONE} 也会清空所选项
     */
    public final void setMultiMode(SelectMode multiMode) {
        helper.setMultiMode(multiMode);
    }

    /**
     * 切换选择模式
     * @param multiMode 选择模式
     * @param cleanFlag 切换时清空已选择项，特别的 当multiMode的值为 {@link SelectMode#NONE} 也会清空所选项
     */
    public final void setMultiMode(SelectMode multiMode, boolean cleanFlag) {
        helper.setMultiMode(multiMode, cleanFlag);
    }

    public final void setItemChecked(long handle, boolean select) {
        helper.setItemChecked(handle, select);
    }

    /**
     * 是否第handle项被选中
     * @param handle 列表中的顺序
     * @return
     */
    public final boolean isChecked(long handle) {
        return helper.isChecked(handle);
    }

    /**
     * @return true 选择模式， false 非选择模式
     */
    public final boolean isSelectFlag() {
        return helper.isSelectFlag();
    }

    /**
     * 获取已选择的数量
     * @return 已选择的数量
     */
    public final int getCheckedItemCount() {
        return helper.getCheckedItemCount();
    }

    /**
     * 设置状态监听器
     * @param listener 状态监听器
     */
    public final void setOnSelectedChangeListener(OnSelectedChangeListener listener) {
        helper.setOnSelectedChangeListener(listener);
    }

    /**
     * 全选
     */
    public final void selectAll() {
        final int count = getCount();
        helper.selectCount(count, count);
    }

    /**
     * 选择前limit项
     * @param limit 限制选择的大小
     */
    public final void selectCount(int limit) {
        helper.selectCount(getCount(), limit);
    }

    /**
     * 取消已选择项目
     */
    public final void cancelAll() {
        helper.cancelSelected();
    }

    /**
     * @return 返回无序的已选择的项目
     */
    public final List<TDate> getSelectedItems() {
        List<TDate> tDateList = new ArrayList<>();
        Set<Long> mItemsIds = helper.getCheckedItemIds();
        if(!mItemsIds.isEmpty()) {
            for (Long index : mItemsIds) {
                if (index != null) {
                    tDateList.add(getItem(index.intValue()));
                }
            }
        }
        return tDateList;
    }

    /**
     * @return 按照列表的顺序返回已选择的项目
     */
    public final List<TDate> getOrderedSelectedItems() {
        List<TDate> tDateList = new ArrayList<>();
        List<Long> mItemsIds = helper.getOrderedCheckedItemIds();
        if(!mItemsIds.isEmpty()) {
            for (Long index : mItemsIds) {
                if (index != null) {
                    tDateList.add(getItem(index.intValue()));
                }
            }
        }
        return tDateList;
    }

    ///=========================================================================
    /**
     * @param position List中的位置
     * @return true 该项可点击, false 该项不可点击
     */
    @Override
    public boolean isItemCheckable(int position) {
        if(mItemCheckableProxy != null) {
            return mItemCheckableProxy.isItemCheckable(position);
        }

        return true;
    }

    /**
     * @param position List中的位置
     * @param id view的
     * @param currentStatus 点击之前，该项的选中状态
     * @return true 设置选中后，true表示已经处理, false 设置选中后，false表示可以后续处理
     */
    @Override
    public boolean onItemClicked(int position, long id, boolean currentStatus) {
        if(mItemCheckableProxy != null) {
            return mItemCheckableProxy.onItemClicked(position, id, currentStatus);
        }

        helper.setItemChecked(position, !currentStatus);
        return true;
    }

    /**
     * @param position List中的位置
     * @param id view的
     * @param currentStatus 点击之前，该项的选中状态
     * @return true 设置选中后，true表示已经处理, false 设置选中后，false表示可以后续处理
     */
    @Override
    public boolean onItemLongClicked(int position, long id, boolean currentStatus) {
        if(mItemCheckableProxy != null) {
            return mItemCheckableProxy.onItemLongClicked(position, id, currentStatus);
        }

        helper.setItemChecked(position, !currentStatus);
        return true;
    }

    ///=========================================================================
    @Override
    public boolean hasStableIds() {
        return mBaseAdapter.hasStableIds();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mBaseAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mBaseAdapter.unregisterDataSetObserver(observer);
    }

    public boolean isAdapterPrepared() {
        return mBaseAdapter != null;
    }

    @Override
    public void notifyDataSetChanged() {
        mBaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        mBaseAdapter.notifyDataSetInvalidated();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mBaseAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return mBaseAdapter.isEnabled(position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return mBaseAdapter.getDropDownView(position, convertView, parent);
    }

    @Override
    public int getItemViewType(int position) {
        return mBaseAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mBaseAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return mBaseAdapter.isEmpty();
    }

    @Override
    public int getCount() {
        return mBaseAdapter.getCount();
    }

    public TDate convert(int position, Object orgItem) {
        return (TDate) orgItem;
    }

    @Override
    public TDate getItem(int position) {
        return convert(position, mBaseAdapter.getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return mBaseAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(useUserViewProduce) {
            return getDefaultView(position, convertView, parent);
        } else {
            return mBaseAdapter.getView(position, convertView, parent);
        }
    }

    protected int getLayoutResId() {
        return R.layout.layout_checkbox_container;
    }

    protected final View getDefaultView(int position, View convertView, ViewGroup parent) {
        AttacheHolder tAttacheHolder = null;
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(getLayoutResId(), parent, false);
            tAttacheHolder = new AttacheHolder(convertView);
            convertView.setTag(tAttacheHolder);
        } else {
            tAttacheHolder = (AttacheHolder) convertView.getTag();
        }

        if(tAttacheHolder == null) {
            tAttacheHolder = new AttacheHolder(convertView);
            convertView.setTag(tAttacheHolder);
        }
        tAttacheHolder.setAttachView(mBaseAdapter.getView(position, tAttacheHolder.getAttachView(), tAttacheHolder.getViewContainer()));
        if(helper.isSelectFlag()) {
            tAttacheHolder.checkBox.setVisibility(View.VISIBLE);
            if (helper.isChecked(position)) {
                tAttacheHolder.checkBox.setChecked(true);
            } else {
                tAttacheHolder.checkBox.setChecked(false);
            }
        } else {
            tAttacheHolder.checkBox.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class AttacheHolder {
        View innerView;
        FrameLayout viewContainer;
        CheckBox checkBox;

        public AttacheHolder(View convertView) {
            viewContainer = (FrameLayout) convertView.findViewById(R.id.ll_inner_view_container);
            checkBox = (CheckBox) convertView.findViewById(android.R.id.checkbox);
        }

        public void setAttachView(View view) {
            if(view != innerView) {//和已经加入的一样，那么，清空添加的布局，添加该布局
                viewContainer.removeAllViews();

                ViewGroup.LayoutParams params = view.getLayoutParams();
                if(params == null) {
                    params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                }
                viewContainer.addView(view, params);
                innerView = view;
            }
//            innerView = view;
//            if(innerView != null) {
//                int childCount = viewContainer.getChildCount();
//                View childView = null;
//                if (childCount > 1) {
//                    viewContainer.removeAllViews();
//                } else if (childCount == 1) {
//                    childView = viewContainer.getChildAt(0);
//                }
//
//                if (childView != innerView) {
//                    viewContainer.removeAllViews();
//
//                    ViewGroup.LayoutParams params = innerView.getLayoutParams();
//                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                    viewContainer.addView(view, params);
//                }
//            }
        }

        public FrameLayout getViewContainer() {
            return viewContainer;
        }

        public View getAttachView() {
            return innerView;
        }
    }
}
