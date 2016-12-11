package algorithm;

import java.util.Scanner;

/**
 * 中国象棋中马的走法
 * <p/>
 * Created by Billin on 2016/12/8.
 */
public class Chess {

    private static final int X = 6;

    private static final int Y = 8;

    // debug
    private static long count = 0;

    public static void main(String[] args) {

        boolean[][] chess = new boolean[X][Y];
        int x;
        int y;

        Scanner scanner = new Scanner(System.in);
        x = scanner.nextInt();
        y = scanner.nextInt();
        chess[x][y] = true;

        System.out.println(horse(chess, x, y));
    }

    private static int horse(boolean[][] chessBoard, int x, int y) {
        return horse(chessBoard, x, y, x, y);
    }

    private static int horse(boolean[][] chessBoard, int startX, int startY, int endX, int endY) {

        int count = 0;

        count += handle(chessBoard, startX + 2, startY + 1, endX, endY);
        count += handle(chessBoard, startX - 2, startY - 1, endX, endY);
        count += handle(chessBoard, startX - 2, startY + 1, endX, endY);
        count += handle(chessBoard, startX - 1, startY - 2, endX, endY);
        count += handle(chessBoard, startX - 1, startY + 2, endX, endY);
        count += handle(chessBoard, startX + 1, startY - 2, endX, endY);
        count += handle(chessBoard, startX + 1, startY + 2, endX, endY);
        count += handle(chessBoard, startX + 2, startY - 1, endX, endY);
        /*
        if (count > Chess.count) {
            Chess.count = count;
            System.out.println(count);
        }*/

        return count;
    }

    private static int handle(boolean[][] chessBoard, int checkX, int checkY, int endX, int endY) {

        if (checkValidate(chessBoard, checkX, checkY)) {
            setChessBoard(chessBoard, checkX, checkY, true);
            int count = horse(chessBoard, checkX, checkY, endX, endY);
            setChessBoard(chessBoard, checkX, checkY, false);
            return count;
        } else if (checkX == endX && checkY == endY) {
            return 1;
        }

        return 0;
    }

    private static void setChessBoard(boolean[][] chessBoard, int x, int y, boolean tof) {
        chessBoard[x][y] = tof;
    }

    private static boolean checkValidate(boolean[][] chessBoard, int x, int y) {
        return checkBound(x, y) && !chessBoard[x][y];
    }

    private static boolean checkBound(int x, int y) {
        return x < X && y < Y && x >= 0 && y >= 0;
    }

}
