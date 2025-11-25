package com.comp2042.commands;

import com.comp2042.*;

import java.util.function.Consumer;

public class MoveRightCommand implements Command {
    private final InputEventListener eventListener;
    private final Consumer<ViewData> refresher;

    public MoveRightCommand(InputEventListener eventListener, Consumer<ViewData> refresher) {
        this.eventListener = eventListener;
        this.refresher = refresher;
    }

    @Override
    public void execute() {
        ViewData result = eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
        refresher.accept(result);
    }
}
