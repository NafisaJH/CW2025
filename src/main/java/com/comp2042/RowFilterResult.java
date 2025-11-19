package com.comp2042;

import java.util.Deque;
import java.util.List;

public final class RowFilterResult {
    private final List<Integer> clearedRows;
    private final Deque<int[]> newRows;

    public RowFilterResult(List<Integer> clearedRows, Deque<int[]> newRows) {
        this.clearedRows = clearedRows;
        this.newRows = newRows;
    }

    public List<Integer> getClearedRows() {
        return clearedRows;
    }

    public Deque<int[]> getNewRows() {
        return newRows;
    }
}
