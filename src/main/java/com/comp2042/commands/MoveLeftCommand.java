package com.comp2042.commands;

import com.comp2042.*;

import java.util.function.Consumer;

public class MoveLeftCommand implements Command {
    private final InputEventListener eventListener;
    private final Consumer<ViewData> refresher;

    public MoveLeftCommand(InputEventListener eventListener, Consumer<ViewData> refresher) {
        this.eventListener = eventListener;
        this.refresher = refresher;
    }

    @Override
    public void execute() {
        ViewData result = eventListener.onLeftEvent(
                new MoveEvent(EventType.LEFT, EventSource.USER)
        );
        refresher.accept(result);
    }

}

