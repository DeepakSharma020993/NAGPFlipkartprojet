package com.nagpassignment.flipkart.utils;

import java.util.List;

public class ListSortChecker {

    public static <T extends Comparable<? super T>> boolean isSorted(List<T> list, SortingType sortingType) {
        int size = list.size();

        for (int i = 0; i < size - 1; i++) {
            int comparisonResult = list.get(i).compareTo(list.get(i + 1));
            if ((sortingType == SortingType.ASCENDING && comparisonResult > 0) ||
                (sortingType == SortingType.DESCENDING && comparisonResult < 0)) {
                return false;
            }
        }

        return true;
    }

    public enum SortingType {
        ASCENDING,
        DESCENDING
    }

   
   
}

