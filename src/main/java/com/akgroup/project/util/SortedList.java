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

    @Override
    public E remove(int index) {
        E result = super.remove(index);
        sortList();
        return result;
    }

    public boolean removeObject(E o) {
        boolean result = super.remove(o);
        sortList();
        return result;
    }

    public void removeFirst() {
        remove(0);
    }

    public E getLast() {
        if (this.size() == 0) {
            return null;
        }
        return this.get(this.size() - 1);
    }

    public E getFirst() {
        if (this.size() == 0) {
            return null;
        }
        return this.get(0);
    }

}