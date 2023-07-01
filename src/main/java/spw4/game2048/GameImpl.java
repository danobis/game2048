package spw4.game2048;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameImpl implements Game {

    private int[][] board;
    private int moves;
    private int score;
    public static Random rand = new Random();

    public GameImpl() {
        initialize();
    }

    @Override
    public int getMoves() {
        return moves;
    }

    @Override
    public int getScore() { return score; }

    @Override
    public int getValueAt(int row, int col) {
        return board[row][col];
    }

    @Override
    public boolean isOver() {
        // check if game is already won
        if(isWon())
            return true;

        // check if board is not full
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if(board[x][y] == 0)
                    return false;
            }
        }
        // check if values can be merged
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 3; x++) {
                if(board[x][y] == board[x+1][y])
                    return false;
            }
        }
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 3; y++) {
                if(board[x][y] == board[x][y+1])
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean isWon() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if(board[x][y] == 2048)
                    return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Moves: ").append(getMoves()).append("   ");
        sb.append("Score: ").append(getScore()).append("\n");
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                int val = getValueAt(x, y);
                String tileString = val == 0 ? "." : String.valueOf(val);
                sb.append(String.format("%-4s", tileString));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void initialize() {
        board = new int[4][4];
        moves = 0;
        score = 0;
        createNewTile();
        createNewTile();
    }

    public void createNewTile() {
        List<Point> emptyPoints = new ArrayList<>();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if(board[x][y] == 0)
                    emptyPoints.add(new Point(x,y));
            }
        }
        int pos = rand.nextInt(emptyPoints.size());
        Point p = emptyPoints.get(pos);
        board[p.x][p.y] = getNewValue();
    }

    public int getNewValue(){
        int randomNum = rand.nextInt(10);
        return randomNum < 9 ? 2 : 4;
    }


    @Override
    public void move(Direction direction) {
        int[][] current = new int[4][4];
        for (int x = 0; x < 4; x++) {
            System.arraycopy(board[x], 0, current[x], 0, 4);
        }
        switch (direction) {
            case right -> moveRight();
            case left -> moveLeft();
            case up -> moveUp();
            case down -> moveDown();
        }
        if(!Arrays.deepEquals(current, board)) {
            createNewTile();
            moves++;
        }
    }

    private void moveRight(){
        // check for combination
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 1; x--) {
                for (int oldX = x-1; oldX >= 0; oldX--) {
                    if(board[oldX][y] != 0 && board[x][y] != board[oldX][y])
                        break;
                    if (board[x][y] == board[oldX][y]) {
                        board[x][y] *= 2;
                        board[oldX][y] = 0;
                        score += board[x][y];
                        break;
                    }
                }
            }
        }
        // move to right side
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                if(board[x][y] == 0) {
                    for (int oldX = x-1; oldX >= 0; oldX--) {
                        if(board[oldX][y] != 0){
                            board[x][y] = board[oldX][y];
                            board[oldX][y] = 0;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void moveLeft(){
        // check for combination
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int oldX = x+1; oldX < 4; oldX++) {
                    if(board[oldX][y] != 0 && board[x][y] != board[oldX][y])
                        break;
                    if (board[x][y] == board[oldX][y]) {
                        board[x][y] *= 2;
                        board[oldX][y] = 0;
                        score += board[x][y];
                        break;
                    }
                }
            }
        }
        // move to left side
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if(board[x][y] == 0) {
                    for (int oldX = x+1; oldX  < 4; oldX++) {
                        if(board[oldX][y] != 0){
                            board[x][y] = board[oldX][y];
                            board[oldX][y] = 0;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void moveUp(){
        // check for combination
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int oldY = y+1; oldY < 4; oldY++) {
                    if(board[x][oldY] != 0 && board[x][y] != board[x][oldY])
                        break;
                    if (board[x][y] == board[x][oldY]) {
                        board[x][y] *= 2;
                        board[x][oldY] = 0;
                        score += board[x][y];
                        break;
                    }
                }
            }
        }
        // move up
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if(board[x][y] == 0) {
                    for (int oldY = y+1; oldY < 4; oldY++) {
                        if(board[x][oldY] != 0){
                            board[x][y] = board[x][oldY];
                            board[x][oldY] = 0;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void moveDown(){
        // check for combination
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int oldY = y-1; oldY >= 0; oldY--) {
                    if(board[x][oldY] != 0 && board[x][y] != board[x][oldY])
                        break;
                    if (board[x][y] == board[x][oldY]) {
                        board[x][y] *= 2;
                        board[x][oldY] = 0;
                        score += board[x][y];
                        break;
                    }
                }
            }
        }
        // move up
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                if(board[x][y] == 0) {
                    for (int oldY = y-1; oldY >= 0; oldY--) {
                        if(board[x][oldY] != 0){
                            board[x][y] = board[x][oldY];
                            board[x][oldY] = 0;
                            break;
                        }
                    }
                }
            }
        }
    }
}