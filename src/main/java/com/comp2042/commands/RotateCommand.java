package com.comp2042.commands;

import com.comp2042.*;

import java.util.function.Consumer;

public class RotateCommand implements Command {
    private final InputEventListener eventListener;
    private final Consumer<ViewData> refresher;

    public RotateCommand(InputEventListener eventListener, Consumer<ViewData> refresher) {
        this.eventListener = eventListener;
        this.refresher = refresher;
    }

    @Override
    public void execute() {
        ViewData result = eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        refresher.accept(result);
    }
}

