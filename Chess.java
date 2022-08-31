/* Author: Vignesh S
   Date: 31-08-2022
   Program for Chess Game */

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        start();
    }

    private static String[][] Board = { { "WR1", "WN1", "WB1", "WK", "WQ", "WB2", "WN2", "WR2" },
            { "WP1", "WP2", "WP3", "WP4", "WP5", "WP6", "WP7", "WP8" },
            { "_", "_", "_", "_", "_", "_", "_", "_" },
            { "_", "_", "_", "_", "_", "_", "_", "_" },
            { "_", "_", "_", "_", "_", "_", "_", "_" },
            { "_", "_", "_", "_", "_", "_", "_", "_" },
            { "BP1", "BP2", "BP3", "BP4", "BP5", "BP6", "BP7", "BP8" },
            { "BR1", "BN1", "BB1", "BK", "BQ", "BB2", "BN2", "BR2" }
    };
    private static String coin, pos;
    private static boolean result = true, isCheck, isObstacle, isIllegalMove, isSameCoin, isMade, inAttack,
            isWCastling = true, kingEscaping, kingMove,
            isBCastling = true, isCastled = false, checkForMate, checkingForMate, canAvoid = true, isMate = false;
    private static char[] dest;
    private static int row, col, flag, wchance = 3, bchance = 3;
    private static ArrayList<String> whites = new ArrayList<>();
    private static ArrayList<String> blacks = new ArrayList<>();
    private static ArrayList<Integer[]> avoidCheckMate = new ArrayList<>();

    private static void SetWhiteList() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                whites.add(Board[i][j]);
            }
        }
    }

    private static void SetBlackList() {
        for (int i = 6; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                blacks.add(Board[i][j]);
            }
        }
    }

    private static void setBooleanValues() {
        isCheck = false;
        isIllegalMove = false;
        isObstacle = false;
        isSameCoin = false;
        isMade = false;
        inAttack = false;
        isCastled = false;
        checkForMate = false;
        checkingForMate = false;
        canAvoid = false;
        kingEscaping = false;
        kingMove = false;
    }

    public static void start() {
        SetBlackList();
        SetWhiteList();
        int toggle = -1;

        while (result) {
            setBooleanValues();
            toggle *= -1;
            Display();
            System.out.println("-----------------------------------");
            if (toggle == 1) {
                System.out.println("White Move");
                GetDetails();
                if (!whites.contains(coin) && isCastled == false) {
                    System.out.println("No such coin available");
                    toggle *= -1;
                    continue;
                }
                if (isMade == false) {
                    Execute();
                }
            } else {
                System.out.println("Black Move");
                GetDetails();
                if (!blacks.contains(coin) && isCastled == false) {
                    System.out.println("No such coin available");
                    toggle *= -1;
                    continue;
                }
                if (isMade == false) {
                    Execute();
                }
            }
            if (flag == 0 || isSameCoin == true) {
                toggle *= -1;
                continue;
            }
            isMade = false;
            isMate = isCheckmate(coin.charAt(0));
            if (isMate == true) {
                String color = "";
                System.out.println("-----------------------------------");
                System.out.println("*** CheckMate ***");
                if (coin.charAt(0) == 'W') {
                    color = "White";
                } else {
                    color = "Black";
                }
                System.out.println(color + " won the game");
                System.out.println("-----------------------------------");
                break;
            }
        }
    }

    private static void GetDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Select coin:");
        coin = sc.nextLine();
        if ((coin.equals("BK") && isBCastling == true && Board[7][1] == "_"
                && Board[7][2] == "_")
                || (coin.equals("WK") && isWCastling == true && Board[0][1] == "_"
                        && Board[0][2] == "_")) {
            System.out.println("1.Castling 2.Move King");
            int opt = sc.nextInt();
            if (opt == 1) {
                castling();
                isMade = true;
                return;
            }
        }
        System.out.print("Select position:");
        pos = sc.nextLine();
        // sc.close();
    }

    private static void Execute() {
        FindPosition(coin);
        dest = pos.toCharArray();
        int i = dest[0] - 'A';
        int j = dest[1] - '1';
        FindCoinType(i, j);
    }
    
    // To find the position of the coin on the Board
    private static void FindPosition(String newCoin) {
        for (int k = 0; k < 8; k++) {
            for (int m = 0; m < 8; m++) {
                if (Board[k][m].equals(newCoin)) {
                    row = k;
                    col = m;
                    break;
                }
            }
        }
    }

    // Finding the type of coin and passing the destination location
    private static void FindCoinType(int i, int j) {
        switch (coin.charAt(1)) {
            case 'P':
                PawnMoves(i, j);
                break;
            case 'K':
                Kmoves(i, j);
                break;
            case 'Q':
                Qmoves(i, j);
                break;
            case 'B':
                BishopMoves(i, j);
                break;
            case 'N':
                NMoves(i, j);
                break;
            case 'R':
                RookMoves(i, j);
                break;
        }
    }

    private static void Display() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(Board[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    // Possible moves of all coins
    public static void Kmoves(int i, int j) {
        int[][] moves = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { 1, 1 }, { 1, -1
        }, { -1, -1 }, { -1, 1 } };
        if (kingEscaping == true && kingMove == false) {
            moveKing(moves);
        } else
            MakeMove(moves, i, j, 8);
    }

    public static void Qmoves(int i, int j) {
        int[][] moves = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { 1, 1 }, { 1, -1
        }, { -1, -1 }, { -1, 1 } };
        MakeMove(moves, i, j, 8);
    }

    public static void BishopMoves(int i, int j) {
        int[][] moves = { { 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };
        MakeMove(moves, i, j, 4);
    }

    public static void RookMoves(int i, int j) {
        int[][] moves = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        MakeMove(moves, i, j, 4);
    }

    public static void NMoves(int i, int j) {
        int[][] moves = { { -2, 1 }, { -2, -1 }, { 1, -2 }, { -1, 2 }, { 1, 2 }, {
                -1, -2 }, { 2, -1 }, { 2, 1 } };
        MakeMove(moves, i, j, 8);
    }

    public static void PawnMoves(int i, int j) {
        int attack = 0;
        if (coin.charAt(0) == 'W') {
            if (Board[i][j].charAt(0) == 'B') {
                attack = 1;
                inAttack = true;
                int[][] attackPos = { { 1, -1 }, { 1, 1 } };
                MakeMove(attackPos, i, j, 2);
            }
        } else {
            if (Board[i][j].charAt(0) == 'W') {
                attack = 1;
                inAttack = true;
                int[][] attackPos = { { -1, -1 }, { -1, 1 } };
                MakeMove(attackPos, i, j, 2);
            }
        }
        if (attack == 0) {
            int[][] moves = new int[1][2];
            moves[0][1] = 0;
            if (coin.charAt(0) == 'W') {
                moves[0][0] = 1;
            } else {
                moves[0][0] = -1;
            }
            MakeMove(moves, i, j, 1);
        }
        if (isMade == true && (i == 0 || i == 7)) {
            getPower(i, j);
        }
    }

    private static void MakeMove(int[][] moves, int i, int j, int size) {
        int row1, col1;
        for (int n = 0; (n < size && isSameCoin == false && isIllegalMove == false && isMade == false
                && canAvoid == false); n++) {
            if (checkForMate == true && checkingForMate == false) {
                avoidCheckMate.clear();
                Integer[] currentPos = { row, col };
                avoidCheckMate.add(currentPos);
            }
            flag = 0;
            row1 = row + moves[n][0];
            col1 = col + moves[n][1];
            isObstacle = false;
            if (coin.charAt(1) == 'P') {
                int loop = 1;
                if (((coin.charAt(0) == 'W' && row == 1) || (coin.charAt(0) == 'B'
                        && row == 6)) && inAttack == false) {
                    loop = 0;
                }
                while (loop < 2 && flag != 20 && (row1 < 8 && col1 < 8 && row1 >= 0 && col1 >= 0)
                        && canAvoid == false) {
                    Check(row1, col1, i, j);
                    row1 = row1 + moves[n][0];
                    col1 = col1 + moves[n][1];
                    loop++;
                }
            } else if (coin.charAt(1) == 'N' || coin.charAt(1) == 'K') {

                if (row1 < 8 && col1 < 8 && row1 >= 0 && col1 >= 0) {
                    Check(row1, col1, i, j);
                }
                if (coin.charAt(1) == 'K' && isMade == true) {
                    if (coin.charAt(0) == 'W') {
                        isWCastling = false;
                    } else {
                        isBCastling = false;
                    }
                }
            } else {
                while (row1 < 8 && col1 < 8 && row1 >= 0 && col1 >= 0 && isObstacle == false && isSameCoin == false
                        && isIllegalMove == false && isMade == false && canAvoid == false) {

                    Check(row1, col1, i, j);
                    row1 = row1 + moves[n][0];
                    col1 = col1 + moves[n][1];
                }
            }
        }
        if (flag == 0 && isCheck == false) {
            System.out.println("-----------------------------------");
            System.out.println("*** Not a valid position ***");
        }
    }

    private static void Check(int row1, int col1, int i, int j) {

        if (row1 == i && col1 == j) {
            if (kingEscaping == true) {
                kingMove = false;
                isObstacle = true;
                return;
            }
            if (checkingForMate == true) {
                canAvoid = true;
                return;
            }
            if (checkForMate == true) {
                canAvoidCheck(i, j);
                return;
            } else if (isCheck == true) {
                System.out.println("-----------------------------------");
                System.out.println("* Illegal Move * King in Check");
                System.out.println("-----------------------------------");
                isIllegalMove = true;
                return;
            }
            String tempDes;
            flag = 1;
            if (Board[i][j].charAt(0) == coin.charAt(0)) {
                System.out.println(Board[i][j] + " " + coin);
                System.out.println("-----------------------------------");
                System.out.println("*** Not a valid position ***");
                System.out.println("-----------------------------------");
                isSameCoin = true;
                return;
            } else if (Board[i][j].equals("_")) {
                Board[i][j] = coin;
                tempDes = "_";
            } else {
                tempDes = Board[i][j];
                if (coin.charAt(0) == 'W') {
                    blacks.remove(Board[i][j]);
                } else {
                    whites.remove(Board[i][j]);
                }
                Board[i][j] = coin;
            }
            Board[row][col] = "_";
            IfKingInCheck(tempDes);
            isMade = true;
        } else if (Board[row1][col1].equals("_")) {
            if (checkForMate == true && checkingForMate == false) {
                Integer[] currentPos = { row1, col1 };
                avoidCheckMate.add(currentPos);
            }
            return;
        } else {
            if (checkForMate == true && checkingForMate == false) {
                Integer[] currentPos = { row1, col1 };
                avoidCheckMate.add(currentPos);
            }
            isObstacle = true;
            if (kingEscaping == true && Board[row1][col1].charAt(1) == 'K') {
                isObstacle = false;
            }
        }
    }
    
    // Checking if king is in check to avoid illegal move
    private static void IfKingInCheck(String tempDes) {
        isCheck = true;

        int tempRow = row, tempCol = col;
        String tempCoin = coin;
        flag = 100;
        if ((coin.charAt(0) == 'W' || tempDes.equals("WK")) && !tempDes.equals("BK")) {
            FindPosition("WK");
            int i = row, j = col;
            for (int x = 0; x < blacks.size() && checkingForMate == false; x++) {
                coin = blacks.get(x);
                FindPosition(coin);
                FindCoinType(i, j);
            }
        } else {
            FindPosition("BK");
            int i = row, j = col;
            for (int x = 0; x < whites.size() && checkingForMate == false; x++) {
                coin = whites.get(x);
                FindPosition(coin);
                FindCoinType(i, j);
            }
        }
        if (isIllegalMove == true) {
            if (isCastled == true) {
                undoCastling(tempDes);
            } else {
                Undo(tempRow, tempCol, tempCoin, tempDes);
            }
        } else {
            flag = 1;
        }
        coin = tempCoin;
    }

    // Undo the move if king is in check
    private static void Undo(int tempRow, int tempCol, String tempCoin, String tempDes) {
        row = tempRow;
        col = tempCol;
        coin = tempCoin;
        Board[row][col] = coin;
        if (coin.charAt(0) == 'W') {
            wchance--;
        } else {
            bchance--;
        }
        if (tempDes.charAt(0) == 'B') {
            blacks.add(tempDes);
        } else if (tempDes.charAt(0) == 'W') {
            whites.add(tempDes);
        }
        int i = dest[0] - 'A';
        int j = dest[1] - '1';
        Board[i][j] = tempDes;
        checkResult();
    }

    // Undo the move if king is in check and castling is done
    private static void undoCastling(String newCoin) {
        if (newCoin.charAt(0) == 'W') {
            Board[0][1] = "_";
            Board[0][2] = "_";
            Board[0][0] = "WR1";
            Board[0][3] = "WK";
            isWCastling = true;
        } else {
            Board[7][1] = "_";
            Board[7][2] = "_";
            Board[7][0] = "BR1";
            Board[7][3] = "BK";
            isBCastling = true;
        }
    }

    // When pawn reached last row of the Board
    private static void getPower(int i, int j) {
        String oldCoin = Board[i][j];
        String newCoin = "";
        if (oldCoin.charAt(0) == 'B') {
            newCoin = addNewCoin('B');
            blacks.add(newCoin);
            blacks.remove(oldCoin);
        } else {
            newCoin = addNewCoin('W');
            whites.add(newCoin);
            whites.remove(oldCoin);
        }
        Board[i][j] = newCoin;
    }

    // Replacing a new coin with the pawn
    private static String addNewCoin(char color) {
        Scanner nc = new Scanner(System.in);
        System.out.print("Select type of coin:");
        String newCoin = nc.nextLine();
        switch (newCoin) {
            case "QUEEN":
                newCoin = color + "Q3";
                break;
            case "ROOK":
                newCoin = color + "R3";
                break;
            case "BISHOP":
                newCoin = color + "B3";
                break;
            case "KNIGHT":
                newCoin = color + "N3";
                break;
            default:
                System.out.println("* Enter valid coin *");
                newCoin = addNewCoin(color);
        }
        return newCoin;
    }

    private static void castling() {
        if (coin.charAt(0) == 'W') {
            Board[0][1] = "WK";
            Board[0][2] = "WR1";
            Board[0][0] = "_";
            Board[0][3] = "_";
            isWCastling = false;
        } else if (coin.charAt(0) == 'B') {
            Board[7][1] = "BK";
            Board[7][2] = "BR1";
            Board[7][0] = "_";
            Board[7][3] = "_";
            isBCastling = false;
        }
        isCastled = true;
        IfKingInCheck(coin);
    }

    // Checking if the game ended
    private static boolean isCheckmate(char color) {
        checkForMate = true;
        if (color == 'W') {
            IfKingInCheck("BK");
        } else {
            IfKingInCheck("WK");
        }
        if (checkingForMate == false) {
            return false;
        }
        if (canAvoid) {
            return false;
        }
        return true;
    }

    private static void canAvoidCheck(int i, int j) {
        checkingForMate = true;
        if (Board[i][j].charAt(0) == 'W') {
            for (int x = 0; x < whites.size() && canAvoid == false; x++) {
                coin = whites.get(x);
                FindPosition(coin);
                if (coin.charAt(1) == 'K') {
                    kingEscaping = true;
                    Kmoves(i, j);
                } else {
                    for (Integer[] k : avoidCheckMate) {
                        FindCoinType(k[0], k[1]);
                        if (canAvoid == true) {
                            break;
                        }
                    }
                }
            }
        } else {
            for (int x = 0; x < blacks.size() && canAvoid == false; x++) {
                coin = blacks.get(x);
                FindPosition(coin);
                if (coin.charAt(1) == 'K') {
                    kingEscaping = true;
                    Kmoves(i, j);
                } else {
                    for (Integer[] k : avoidCheckMate) {
                        FindCoinType(k[0], k[1]);
                        if (canAvoid == true) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private static void moveKing(int[][] moves) {
        int row1, col1, n;
        int row2 = row, col2 = col;
        String piece = coin;
        for (n = 0; n < 8 && canAvoid == false; n++) {
            coin = piece;
            row1 = row2 + moves[n][0];
            col1 = col2 + moves[n][1];
            if (row1 < 8 && col1 < 8 && row1 >= 0 && col1 >= 0 && canAvoid == false
                    && Board[row1][col1].charAt(0) != coin.charAt(0)) {
                moving(row1, col1);
            }
        }
        coin = piece;
    }

    private static void moving(int i, int j) {
        char c = coin.charAt(0);
        kingMove = true;
        if (c == 'W') {
            for (int x = 0; x < blacks.size() && kingMove == true; x++) {
                coin = blacks.get(x);
                FindPosition(coin);
                FindCoinType(i, j);
            }
        } else {
            for (int x = 0; x < whites.size() && kingMove == true; x++) {
                coin = whites.get(x);
                FindPosition(coin);
                FindCoinType(i, j);
            }
        }
        if (kingMove == true) {
            canAvoid = true;
        }
    }

    // Displaying result
    private static void checkResult() {
        if (wchance < 1) {
            System.out.println("Black won the game");
            result = false;
        } else if (bchance < 1) {
            System.out.println("White won the game");
            result = false;
        }
    }
}
