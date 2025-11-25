package com.comp2042.commands;

import com.comp2042.EventSource;
import com.comp2042.EventType;
import com.comp2042.MoveEvent;
import com.comp2042.InputEventListener;
import java.util.function.Consumer;

public class MoveDownCommand implements Command {
    private final Consumer<MoveEvent> moveDown;

    public MoveDownCommand(Consumer<MoveEvent> moveDown) {
        this.moveDown = moveDown;
    }

    @Override
    public void execute() {
        moveDown.accept(new MoveEvent(EventType.DOWN, EventSource.USER));
    }
}

