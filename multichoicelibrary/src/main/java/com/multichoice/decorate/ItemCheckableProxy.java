package com.multichoice.decorate;

/**
 * Item点击过滤接口
 */
public interface ItemCheckableProxy {
    boolean isItemCheckable(int position);

    boolean onItemClicked(int position, long id, boolean currentStatus);

    boolean onItemLongClicked(int position, long id, boolean currentStatus);
}
