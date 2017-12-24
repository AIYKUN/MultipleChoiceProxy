package com.multichoice.decorate;

/**
 * 选择模式
 */
public enum SelectMode {
    NONE(0, "普通模式"),
    Multi(1, "多选模式"),
    Single(2, "单选模式");

    int code;

    SelectMode(int code, String desc) {
        this.code = code;
    }

    public static SelectMode findSelectMode(int code) {
        SelectMode[] tSelectMode = values();
        for(SelectMode mode : tSelectMode) {
            if(mode.code == code) {
                return mode;
            }
        }
        return SelectMode.Multi;
    }
}
