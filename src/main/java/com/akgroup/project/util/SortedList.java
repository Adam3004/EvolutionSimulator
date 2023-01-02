package com.akgroup.project.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SortedList<E> extends ArrayList<E> {
    private final Comparator<E> comparator;

    public SortedList(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public void sortList() {
        this.sort(Collections.reverseOrder(this.comparator));
    }

    @Override
    public boolean add(E e) {
        boolean result = super.add(e);
        sortList();
        return result;
    }

}