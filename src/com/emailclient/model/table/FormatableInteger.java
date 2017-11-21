package com.emailclient.model.table;

import java.util.Comparator;

public class FormatableInteger implements Comparator<FormatableInteger> {

    /**
     * For custom table/row sorting.
     */


    private int size;

    public FormatableInteger(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        String returnValue;

        if (size <= 0) {
            returnValue = "0";
        } else if (size < 1024) {
            returnValue = size + " B";
        } else if (size < 1048576) {
            returnValue = size / 1024 + " kB";
        } else {
            returnValue = size / 1048576 + " mB";
        }
        return returnValue;
    }

    @Override
    public int compare(FormatableInteger o1, FormatableInteger o2) {

        Integer int1 = o1.size;
        Integer int2 = o2.size;

        return int1.compareTo(int2);
    }
}
