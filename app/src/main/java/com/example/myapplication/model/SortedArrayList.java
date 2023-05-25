package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortedArrayList<T> extends ArrayList<T> {
    @SuppressWarnings("unchecked")
    public void insertSorted(T value) {
        int i = Collections.binarySearch((List<Comparable<T>>) this, value);
        add(i < 0 ? -i - 1 : i, value);
    }
}