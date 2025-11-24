package com.comp2042;

import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

public final class MatrixOperations {

    private MatrixOperations() {}
    
    public static int[][] copy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

    public static int[][] rebuildMatrix(int[][] matrix, Deque<int[]> newRows) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) tmp[i] = row;
        }
        return tmp;
    }

    public static boolean intersect(int[][] matrix, int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                if (brick[i][j] != 0) {
                    int targetX = x + j;
                    int targetY = y + i;
                    if (isOutOfBounds(matrix, targetX, targetY) || matrix[targetY][targetX] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int[][] mergeBrickIntoMatrix(int[][] matrix, int[][] brick, int x, int y) {
        int[][] copy = MatrixOperations.copy(matrix);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                if (brick[i][j] != 0) {
                    copy[y + i][x + j] = brick[i][j];
                }
            }
        }
        return copy;
    }

    private static boolean isOutOfBounds(int[][] matrix, int x, int y) {
        return x < 0 || y < 0 || y >= matrix.length || x >= matrix[y].length;
    }

}