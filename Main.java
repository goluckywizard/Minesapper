package ru.nsu.fit.minesweeper;

import ru.nsu.fit.minesweeper.gui.GUIController;
import ru.nsu.fit.minesweeper.text.TextController;

public class Main {
    public static void main(String[] args) {
        GUIController b = new GUIController();
        b.game(args);
        //TextController a = new TextController();
    }
}
