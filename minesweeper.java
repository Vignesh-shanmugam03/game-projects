
/* Author: Vignesh S
   Date: 09-08-2022
   Program for Minesweeper Game */

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class minesweeper {
    private int[][] hiddenField = new int[8][8];
    private String[][] visibleField = new String[8][8];
    private int count = 0, size = 8;
    private boolean flag = true, isFlag;
    private ArrayList<Integer> aList = new ArrayList<>();
    private int[][] adjacent = { { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { 1, 0 }, { -1, 0 }, { -1, -1 }, { -1, 1 } };

    // Constructor
    public minesweeper() {
        System.out.println("Welcome to Minesweeper");
        StartGame();
    }

    private void StartGame() {

        DisplayField();
        int choice = 10;
        Scanner sc = new Scanner(System.in);

        while (flag && count < 54) {
            isFlag = false;
            if (count != 0) {
                System.out.println("Enter 1 to set flag\nEnter 2 to open field");
                choice = sc.nextInt();
            }
            if (choice == 1) {
                isFlag = true;
            }
            System.out.println("Enter row and column:");
            int row = sc.nextInt();
            int col = sc.nextInt();
            row--;
            col--;
            if (isFlag == true) {
                setFlag(row, col);
                continue;
            } else if (count == 0) {
                initialize(row, col);
                SetBombs();
            }
            if (isOpened(row, col)) {
                continue;
            } else {
                openPosition(row, col);
            }

            DisplayField();
        }
        if (count == 54) {
            System.out.println("**** You Won ****");
        }
        sc.close();
    }

    // Setting first position as always zero
    private void initialize(int row, int col) {
        int num = (row * 10) + col;
        aList.add(num);
        for (int i = 0; i < 8; i++) {
            int row1 = row + adjacent[i][0];
            int col1 = col + adjacent[i][1];
            if (row1 >= 0 && row1 < size && col1 >= 0 && col1 < size) {
                num = (row1 * 10) + col1;
                aList.add(num);
            }
        }
        hiddenField[row][col] = 0;
    }

    // Code for marking flags in the location of Bombs
    private void setFlag(int row, int col) {
        visibleField[row][col] = " ~ ";
        DisplayField();
    }

    // Code for setting Bombs
    private void SetBombs() {
        int BombCount = 0;
        int flag;
        while (BombCount < 15) {
            Random random = new Random();
            int i = random.nextInt(8);
            int j = random.nextInt(8);
            int num = (i * 10) + j;
            while (aList.contains(num)) {
                flag = 0;
                for (int k = 0; k < size; k++) {
                    i = (i + k) % size;
                    for (int l = 1; l < size; l++) {
                        j = (j + l) % size;
                        num = (i * 10) + j;
                        if (aList.contains(num)) {
                            continue;
                        } else {
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 1) {
                        break;
                    }
                }
            }
            aList.add(num);
            BombCount++;
            hiddenField[i][j] = 100;
            SetField(i, j);
        }
    }

    // Code for setting values around the locations of Bombs
    private void SetField(int row, int col) {
        for (int i = 0; i < 8; i++) {
            int row1 = row + adjacent[i][0];
            int col1 = col + adjacent[i][1];
            if (row1 >= 0 && row1 < size && col1 >= 0 && col1 < size && hiddenField[row1][col1] != 100) {
                hiddenField[row1][col1] += 1;
            }
        }
    }

    // Checking if the position is already opened
    private boolean isOpened(int row, int col) {
        if (visibleField[row][col] == null || visibleField[row][col].equals(" ~ ")) {
            return false;
        }
        return true;
    }

    // Opening the position player entered
    private void openPosition(int row, int col) {
        if (hiddenField[row][col] == 100) {
            flag = false;
            return;
        }
        if (hiddenField[row][col] == 0) {
            visibleField[row][col] = " 0 ";

            for (int i = 0; i < 8; i++) {
                int row1 = row + adjacent[i][0];
                int col1 = col + adjacent[i][1];
                if (row1 >= 0 && row1 < size && col1 >= 0 && col1 < size) {
                    if (isOpened(row1, col1)) {
                        continue;
                    } else {
                        openPosition(row1, col1);
                    }
                }
            }
        }
        visibleField[row][col] = " " + hiddenField[row][col] + " ";
        count++;
    }

    // Code for displaying the field
    private void DisplayField() {
        if (count == 0) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    System.out.print(" _ ");
                }
                System.out.println("");
            }
        } else {
            if (flag == false) {
                System.out.println("***** Game over *****");
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (hiddenField[i][j] == 100) {
                            System.out.print(" X ");
                        } else if (visibleField[i][j] == null) {
                            System.out.print(" _ ");
                        } else {
                            if (visibleField[i][j].equals(" ~ ") && hiddenField[i][j] != 100) {
                                System.out.print(" ? ");
                            } else {
                                System.out.print(visibleField[i][j]);
                            }
                        }
                    }
                    System.out.println("");
                }
            } else {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (visibleField[i][j] == null) {
                            System.out.print(" _ ");
                        } else {
                            System.out.print(visibleField[i][j]);
                        }
                    }
                    System.out.println("");
                }
            }
        }
    }
}