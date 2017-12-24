package com.multichoice.decorate;

/**
 * 选择状态改变监听器
 */
public interface OnSelectedChangeListener {
    void onSelectedChanged(SelectMode mode, int selectCount);
}
