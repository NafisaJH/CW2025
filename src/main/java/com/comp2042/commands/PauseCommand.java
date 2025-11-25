package com.comp2042.commands;

public class PauseCommand implements Command {
    private final Runnable pauseAction;

    public PauseCommand(Runnable pauseAction) {
        this.pauseAction = pauseAction;
    }

    @Override
    public void execute() {
        pauseAction.run();
    }
}
