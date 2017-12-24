package com.multichoice.demo.date;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/24.
 */

public class ListDates {
    public static void loadTestDates(List<String> mDates) {
        for (int i = 0; i < 50; i++) {
            mDates.add("item:" + i);
        }
    }
}
