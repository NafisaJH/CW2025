package com.comp2042.commands;

public class NewGameCommand implements Command {
    private final Runnable newGameAction;

    public NewGameCommand(Runnable newGameAction) {
        this.newGameAction = newGameAction;
    }

    @Override
    public void execute() {
        newGameAction.run();
    }
}

