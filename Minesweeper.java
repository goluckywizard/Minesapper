package ru.nsu.fit.minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    final int SIG_NEWGAME = 1;
    final int SIG_EXITGAME = 0;
    MineState[][] mineField;
    CellState[][] userField;
    Integer[][] userMineField;
    Integer minesCount = 10;
    Integer lengthField = 10;
    Integer countKnown = 0;
    int isEnd = 0;
    //CellState[][] gameField;
    public Integer[][] getUserMineField() {
        userMineField = new Integer[lengthField][lengthField];
        for (int i = 0; i < lengthField; i++) {
            for (int j = 0; j < lengthField; j++) {
                if (userField[i][j] == CellState.FLAG)
                    userMineField[i][j] = -1;
                if (userField[i][j] == CellState.KNOWN) {
                    int count = 0;
                    for (int i_1 = -1; i_1 <= 1; ++i_1)
                        for (int j_1 = -1; j_1 <= 1; ++j_1)
                            if ((i+i_1 >= 0) && (i+i_1 < lengthField) && (j+j_1 >= 0) && (j+j_1 < lengthField))
                                if (mineField[i+i_1][j+j_1] == MineState.MINE)
                                    count++;
                    userMineField[i][j] = count;
                }
                if (userField[i][j] == CellState.UNKNOWN) {
                    userMineField[i][j] = -5;
                }
                if (isEnd == 1 && mineField[i][j] == MineState.MINE)
                    userMineField[i][j] = -10;
            }
        }
        return userMineField;
    }
    public Integer getLengthField() {
        return lengthField;
    }

    public Integer getMinesCount() {
        return minesCount;
    }
    public Integer getCountKnown() {
        return countKnown;
    }
    private int[] randomNumbersWithoutRepeat() {
        int start = 0, end = lengthField*lengthField - 1;
        Random rand = new Random();
        int count = minesCount;
        int[] result = new int[count];
        int cur = 0;
        int remaining = end - start;
        for (int i = start; i < end && count > 0; ++i) {
            double probability = rand.nextDouble();
            if (probability < ((double) count) / (double) remaining) {
                count--;
                result[cur++] = i;
            }
            remaining--;
        }
        return result;
    }
    public void generateField() {
        //MineState[][] gameField;
        mineField = new MineState[lengthField][lengthField];
        for (int i = 0; i < lengthField; ++i) {
            for (int j = 0; j < lengthField; ++j) {
                mineField[i][j] = MineState.NOT_MINE;
            }
        }
        int[] minesLocation = randomNumbersWithoutRepeat();
        /*for (int a : minesLocation) {
            System.out.println(a);
        }*/
        for (int a : minesLocation) {
            //System.out.println();
            mineField[a / lengthField][a % lengthField] = MineState.MINE;
        }
        //return mineField;
    }


    public int Open(String arg) {
        //System.out.println(arg);
        final int SIG_OK = 0;
        final int SIG_LOSE = 1;
        final int SIG_ALREADY = 2;
        int x = 0, y = 0;
        x = (arg.charAt(0) - 'a');
        y = (arg.charAt(1) - '0');
        if (userField[x][y] == CellState.UNKNOWN) {
            if (mineField[x][y] == MineState.NOT_MINE) {
                userField[x][y] = CellState.KNOWN;
                countKnown = countKnown + 1;
                //System.out.println(countKnown);
                int countNear = 0;
                for (int i = -1; i <= 1; ++i)
                    for (int j = -1; j <= 1; ++j)
                        if ((x+i >= 0) && (x+i < lengthField) && (y+j >= 0) && (y+j < lengthField))
                            if (mineField[x+i][y+j] == MineState.MINE)
                                countNear++;
                if (countNear == 0) {
                    for (int i = -1; i <= 1; ++i)
                        for (int j = -1; j <= 1; ++j)
                            if ((x+i >= 0) && (x+i < lengthField) && (y+j >= 0) && (y+j < lengthField)) {
                                    char turn[] = new char[2];
                                    turn[0] = (char) (x+i + 'a');
                                    turn[1] = (char) (y+j + '0');
                                    Open(new String(turn));
                                }
                }
            }
            if (mineField[x][y] == MineState.MINE) {
                //System.out.println("You lose!\n");
                isEnd = 1;
                return SIG_LOSE;
            }
        }
        else
            if (userField[x][y] == CellState.KNOWN || userField[x][y] == CellState.FLAG) {
                //System.out.println("This is already pointed point! Choose another\n");
                return SIG_ALREADY;
            }
        return SIG_OK;
    }
    public void Flag(String arg) {
        int x = 0, y = 0;
        x = (arg.charAt(0) - 'a');
        y = (arg.charAt(1) - '0');
        if (userField[x][y] == CellState.KNOWN) {
            System.out.println("It's no use");
        } else if (userField[x][y] == CellState.FLAG) {
            userField[x][y] = CellState.UNKNOWN;
        } else {
            userField[x][y] = CellState.FLAG;
        }
    }
    /*int doGame() {

    }*/
    public CellState[][] generateUserField() {
        userField = new CellState[lengthField][lengthField];
        for (int i = 0; i < lengthField; i++) {
            for (int j = 0; j < lengthField; j++) {
                userField[i][j] = CellState.UNKNOWN;
            }
        }
        return userField;
    }
    public CellState[][] getUserField() {
        return userField;
    }

    public MineState[][] getMineField() {
        return mineField;
    }

    public void setMinesCount(Integer minesCount) {
        this.minesCount = minesCount;
    }

    public void setLengthField(Integer lengthField) {
        this.lengthField = lengthField;
    }
    void ISEND() {
        if (getCountKnown() < lengthField*lengthField - minesCount)
            isEnd = 1;
        isEnd = 0;
    }
}
