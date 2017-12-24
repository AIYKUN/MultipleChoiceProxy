package com.multichoice.decorate;

/**
 * 适配器通知接口
 */
public interface IAdapterNotifyListner {
    boolean isAdapterPrepared();

    void notifyDataSetChanged();
}
