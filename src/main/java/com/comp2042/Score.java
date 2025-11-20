package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);


    private static final int BONUS_MULTIPLIER = 50;

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void addPoints(int points) {
        score.setValue(score.getValue() + points);
    }

    public void reset() {
        score.setValue(0);
    }

    public static int calculateScoreBonus(int clearedCount) {
        return BONUS_MULTIPLIER * clearedCount * clearedCount;
    }
}