import java.util.Scanner;

public class App {
    private static Scanner arbuz = new Scanner(System.in);
    private static int razmerDoski = 3;
    
    public static void main(String[] args) {
        mainGameLoop();
        arbuz.close();
        System.out.println("Vihid z programi...");
    }

    private static void mainGameLoop() {
        boolean codeisrunning = true;
        while (codeisrunning) {
            displayMainMenu();
            if (!arbuz.hasNextLine()) {
                System.out.println("Nepravilniy vvid.");
                continue;
            }
            
            char choice = arbuz.nextLine().charAt(0);
            switch (choice) {
                case '1' -> handleGameMenu();
                case '2' -> handleSettingsMenu();
                case '3' -> codeisrunning = handleExitMenu();
                default -> System.out.println("Pomilka v zapiti, spobuyte she raz.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("""
                ===Welcome to Main Menu===
                 ==Vas vitae Pes Patron==
                1. Start game
                2. Settings
                3.Exit""");
    }

    private static char[][] createGameBoard(int rows, int cols) {
        char[][] displayBoard = new char[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                displayBoard[i][j] = ' ';
            }
        }
        
        for (int i = 0; i < razmerDoski; i++) {
            displayBoard[0][i * 4 + 2] = (char) ('1' + i);
            displayBoard[i * 2 + 2][0] = (char) ('1' + i);
        }
        
        for (int i = 1; i < rows; i += 2) {
            for (int j = 1; j < cols; j++) {
                displayBoard[i][j] = '-';
            }
        }
        
        for (int i = 0; i < rows; i++) {
            for (int j = 4; j < cols; j += 4) {
                displayBoard[i][j - 1] = '|';
            }
        }
        
        return displayBoard;
    }

    private static void displayBoard(char[][] board) {
        for (char[] rowArray : board) {
            System.out.println(rowArray);
        }
    }

    private static boolean isValidMove(int row, int col, char[][] board) {
        return row >= 1 && row <= razmerDoski && 
               col >= 1 && col <= razmerDoski && 
               board[(row - 1) * 2 + 2][(col - 1) * 4 + 2] == ' ';
    }

    private static boolean checkWin(char[][] board, char igrok, int rows, int cols) {
        for (int i = 2; i < rows; i += 2) {
            int count = 0;
            for (int j = 2; j < cols; j += 4) {
                if (board[i][j] == igrok) {
                    count++;
                    if (count == 3) return true;
                } else {
                    count = 0;
                }
            }
        }

        for (int j = 2; j < cols; j += 4) {
            int count = 0;
            for (int i = 2; i < rows; i += 2) {
                if (board[i][j] == igrok) {
                    count++;
                    if (count == 3) return true;
                } else {
                    count = 0;
                }
            }
        }

        for (int startI = 2; startI <= rows - 4; startI += 2) {
            for (int startJ = 2; startJ <= cols - 8; startJ += 4) {
                int count = 0;
                for (int k = 0; k < 3 && startI + k*2 < rows && startJ + k*4 < cols; k++) {
                    if (board[startI + k*2][startJ + k*4] == igrok) count++;
                }
                if (count == 3) return true;
            }
        }

        for (int startI = 2; startI <= rows - 4; startI += 2) {
            for (int startJ = cols - 2; startJ >= 10; startJ -= 4) {
                int count = 0;
                for (int k = 0; k < 3 && startI + k*2 < rows && startJ - k*4 >= 2; k++) {
                    if (board[startI + k*2][startJ - k*4] == igrok) count++;
                }
                if (count == 3) return true;
            }
        }

        return false;
    }

    private static boolean checkDraw(char[][] board, int rows, int cols) {
        for (int i = 2; i < rows; i += 2) {
            for (int j = 2; j < cols; j += 4) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }

    private static void handleGameMenu() {
        boolean inGameMenu = true;
        while (inGameMenu) {
            System.out.println(": Board size: " + razmerDoski + "x" + razmerDoski);
            System.out.println("Are you ready?(1) Yes! (2)Go back to main menu");
            
            char choice = arbuz.nextLine().charAt(0);
            if (choice == '2') {
                inGameMenu = false;
            } else if (choice == '1') {
                playGame();
                inGameMenu = false;
            } else {
                System.out.println("pomilka v zapiti, spobuyte she raz.");
            }
        }
    }

    private static void playGame() {
        int rows = razmerDoski * 2 + 1;
        int cols = razmerDoski * 4 - 1;
        char[][] board = createGameBoard(rows, cols);
        char currentPlayer = 'X';
        boolean isGameOver = false;

        while (!isGameOver) {
            System.out.println("\nZaraz shturmuye: " + currentPlayer);
            displayBoard(board);

            int[] move = getPlayerMove(board);
            if (move[0] == 0) {
                break;
            }

            int displayRow = (move[0] - 1) * 2 + 2;
            int displayCol = (move[1] - 1) * 4 + 2;
            board[displayRow][displayCol] = currentPlayer;

            if (checkWin(board, currentPlayer, rows, cols)) {
                System.out.println("POTUZHNA PEREMOGA " + currentPlayer + "!!!");
                isGameOver = true;
            } else if (checkDraw(board, rows, cols)) {
                System.out.println("Nichya nachalnika!");
                isGameOver = true;
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        System.out.println("\nItogove pole:");
        displayBoard(board);
    }
    private static int[] getPlayerMove(char[][] board) { 
        while (true) {
            System.out.println("Vvedit ryad (1-" + razmerDoski + ", or 0 to exit):");
            String input = arbuz.nextLine();
            if (input.isEmpty()) continue;
            int row = input.charAt(0) - '0';
            
            if (row == 0) return new int[]{0, 0};

            System.out.println("Vvedit colonku (1-" + razmerDoski + "):");
            input = arbuz.nextLine();
            if (input.isEmpty()) continue;
            int col = input.charAt(0) - '0';

            if (isValidMove(row, col, board)) { 
                return new int[]{row, col};
            }
            System.out.println("Kabinka zaynyata! Sprobuyte she raz."); 
        }
    }

    private static void handleSettingsMenu() {
        boolean inSettingsMenu = true;
        while (inSettingsMenu) {
            System.out.println("""
                Vibir rozmiru doshki:
                1. 3x3
                2. 5x5
                3. 7x7
                4. 9x9
                0. Povernutisya nazad""");
            
            char choice = arbuz.nextLine().charAt(0);
            switch (choice) {
                case '1' -> { razmerDoski = 3; inSettingsMenu = false; }
                case '2' -> { razmerDoski = 5; inSettingsMenu = false; }
                case '3' -> { razmerDoski = 7; inSettingsMenu = false; }
                case '4' -> { razmerDoski = 9; inSettingsMenu = false; }
                case '0' -> inSettingsMenu = false;
                default -> System.out.println("pomilka v zapiti, spobuyte she raz.");
            }
            if (choice >= '1' && choice <= '4') {
                System.out.println("Vstanovleno rozmir " + razmerDoski + "x" + razmerDoski);
            }
        }
    }

    private static boolean handleExitMenu() {
        System.out.println("Are you sure bra? ( enter 1(Yep) or 2(NUH UH)");
        char choice = arbuz.nextLine().charAt(0);
        if (choice == '1') {
            return false;
        } else if (choice == '2') {
            System.out.println("Deltuyemo v golovne menu.");
            return true;
        } else {
            System.out.println("Pomidka v zapiti, spobuyte she raz.");
            return true;
        }
    }
}