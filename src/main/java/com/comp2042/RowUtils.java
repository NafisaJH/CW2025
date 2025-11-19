package com.comp2042;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class RowUtils {

    private RowUtils() {}

    public static ClearRow clearFullRows(final int[][] matrix) {
        RowFilterResult result = filterRows(matrix);
        int[][] newMatrix = MatrixOperations.rebuildMatrix(matrix, result.getNewRows());
        int scoreBonus = Score.calculateScoreBonus(result.getClearedRows().size());
        return new ClearRow(result.getClearedRows().size(), newMatrix, scoreBonus);
    }

    private static RowFilterResult filterRows(int[][] matrix) {
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (isRowFull(matrix[i])) {
                clearedRows.add(i);
            } else {
                newRows.add(matrix[i].clone());
            }
        }
        return new RowFilterResult(clearedRows, newRows);
    }

    private static boolean isRowFull(int[] row) {
        for (int cell : row) {
            if (cell == 0) return false;
        }
        return true;
    }
}