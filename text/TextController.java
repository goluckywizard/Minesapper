package ru.nsu.fit.minesweeper.text;

import ru.nsu.fit.minesweeper.Minesweeper;
import ru.nsu.fit.minesweeper.*;

import java.util.Scanner;

public class TextController {
    final int SIG_EXITGAME = 0;
    final int SIG_NEWGAME = 1;
    /*public void showMineField(int lengthField, MineState[][] mineField) {
        for (int i = 0; i < lengthField; i++) {
            for (int j = 0; j < lengthField; j++) {
                System.out.print((mineField[i][j] == MineState.MINE) ? "*":0);
            }
            System.out.println();
        }
    }*/

    public void showGameField(int lengthField, CellState[][] userField, Integer[][] userMineField) {
        for (int i = 0; i < lengthField; ++i) {
            for (int j = 0; j < lengthField; ++j) {
                if (userMineField[i][j] == -5)
                    System.out.println("#");
                if (userMineField[i][j] == -1)
                    System.out.println("f");
                if (userMineField[i][j] >= 0 && userMineField[i][j] <= 9)
                    System.out.println(userMineField[i][j]);
            }
        }
    }

    int doGame(Minesweeper model) {
        int lengthField = model.getLengthField();
        int minesCount = model.getMinesCount();
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter length of field: ");
        lengthField = scan.nextInt();
        System.out.println("Enter count of mines: ");
        minesCount = scan.nextInt();
        model.setLengthField(lengthField);
        model.setMinesCount(minesCount);

        model.generateField();
        Integer[][] userMineField= model.getUserMineField();
       // showMineField(lengthField, mineField);
        CellState[][] userField = model.generateUserField();

        //System.out.println("aaaaaaaaaaaaa\n");
        while (model.getCountKnown() < lengthField*lengthField - minesCount) {
            userField = model.getUserField();
            //System.out.println("count known"+countKnown+"\n");
            showGameField(lengthField, userField, userMineField);
            System.out.println("Enter your command");
            String turn = scan.nextLine();
            if (turn.equals("exit")) {
                return SIG_EXITGAME;
            }
            if (turn.equals("New game")) {
                return SIG_NEWGAME;
            }
            if (turn.equals("help")) {
                System.out.println("help \t -- \t show command list\n");
                System.out.println("exit \t -- exit game\t \n");
                System.out.println("open + field(a1) \t -- \t turn with open a cell\n");
                System.out.println("flag + field(a1) \t -- \t turn with flag a cell\n");
                System.out.println("New game \t -- \t start a new game\n");
            }
            String[] comArgs;
            String delimeter = " ";
            comArgs = turn.split(delimeter);
            if (comArgs[0].equals("open")) {
                int open_status = 0;
                open_status = model.Open(comArgs[1]);
                if (open_status == 1) {
                    return SIG_EXITGAME;
                }
                if (open_status == 2) {
                    System.out.println("This is already pointed point! Choose another command\n");
                }
            }
            if (comArgs[0].equals("flag")) {
                model.Flag(comArgs[1]);
            }
            //System.out.println("count known after"+countKnown+"\n");
        }
        return SIG_EXITGAME;

    }
    public TextController() {
        Minesweeper model = new Minesweeper();
        int exit_status;
        do {
            exit_status = doGame(model);
        } while (exit_status != 0);
        if (model.getCountKnown() >= model.getLengthField()*model.getLengthField() - model.getMinesCount()) {
            System.out.println("Congratulations!");
        }
    }
}
