import java.util.Scanner;

/*
 * Program that allows two people to play Connect Four.
 */


public class ConnectFour {

    public static final int NUM_PLAYERS = 2;
    public static final int NUM_OF_ROWS = 6;
    public static final int NUM_OF_COLUMNS = 7;
    public static final int CHECK_IF_EVEN_NUM = 2;
    public static final int CONSEC_PIECES_TO_WIN = 4;
    public static final int POSSIBLE_DIAG_ROW_INDEX = 2;
    public static final int POSSIBLE_DIAG_COL_INDEX = 3;
    public static final int DRAW = 42;
    public static final String EMPTY_SPACE = ".";
    public static final String RED_PIECE = "r";
    public static final String BLACK_PIECE = "b";

    public static void main(String[] args) {
        intro();

        Scanner key = new Scanner(System.in);
        String[] getNames = playerNames(key);
        printWhilePlaying();
        String[][] getBoard = gameBoard();
        int[] currentTurn = turnCount();
        boolean[] gameStatus = gameState();
        loopTurns(getNames, key, getBoard, currentTurn, gameStatus);
        printResults(getBoard, getNames, currentTurn, gameStatus);
    
        key.close();
    }


    // Show the introduction.
    public static void intro() {
        System.out.println("This program allows two people to play the");
        System.out.println("game of Connect four. Each player takes turns");
        System.out.println("dropping a checker in one of the open columns");
        System.out.println("on the board. The columns are numbered 1 to 7.");
        System.out.println("The first player to get four checkers in a row");
        System.out.println("horizontally, vertically, or diagonally wins");
        System.out.println("the game. If no player gets fours in a row and");
        System.out.println("and all spots are taken the game is a draw.");
        System.out.println("Player one's checkers will appear as r's and");
        System.out.println("player two's checkers will appear as b's.");
        System.out.println("Open spaces on the board will appear as .'s.\n");
    }


    /* Asks for the names of the 2 players and stores them into an array.
     * @param key Scanner that reads in user input.
     * @return names Array that contains player 1's name as the first element and player 2's     
     * element as the second element.
    */
    public static String[] playerNames(Scanner key) {
        String[] names = new String[NUM_PLAYERS];
        System.out.print("Player 1 enter your name: ");
        names[0] = key.nextLine();
        System.out.print("\nPlayer 2 enter your name: ");
        names[1] = key.nextLine();
        return names;
    }


    /* Prints out the string "current board" for when the game is still going on, and calls the  
     * method to print the column numbers.
    */
    public static void printWhilePlaying() {
        System.out.println("\nCurrent Board");
        printColNums();
    }

   
    // Prints out the column numbers from 1-7.
    public static void printColNums() {
        for (int colNum = 1; colNum <= NUM_OF_COLUMNS; colNum++) {
            System.out.print(colNum);
            System.out.print(" ");
        }
        System.out.println(" column numbers");
    }


    /* Creates an empty game board that is 6 rows and 7 columns long.
     * @return board 2D array of the game board.
    */
    public static String[][] gameBoard() {
        String[][] board = new String[NUM_OF_ROWS][NUM_OF_COLUMNS];
        int numRows = board.length;
        int numCols = board[0].length;
        for (int row = 0; row < numRows; row++) {
            board[row][0] = ".";
            System.out.print(board[row][0]);
            for (int col = 1; col < numCols; col++) {
                System.out.print(" ");
                board[row][col] = ".";
                System.out.print(board[row][col]);
            }
            System.out.println(" ");
        }
        return board;
    }


    /* Returns an array of turn related variables, where the first element is the turn number, and
     * the second element is the number of pieces that have been dropped in the game.
     * @return turnStatistics Array that holds how many turns have been played and how many pieces  * have been dropped in the game.
    */
    public static int[] turnCount() {
        int[] turnStatistics = {1, 0};
        return turnStatistics;
    }


    /* Returns an array of state-of-the-game variables, where the first element is who's turn it is 
     * (true = player 1, false = player 2), and the second element is whether the game is still 
     * going on (true = still playing, false = game is over).
     * @return gameConditions Array that holds who's turn it currently is and whether the game is
     * still going on or not.
    */
    public static boolean[] gameState() {
        boolean[] gameConditions = {true, true};
        return gameConditions;
    }


    /* Depending on who's turn it is, asks that player to enter the column they want to drop at.
     * @param getNames Gets the names of the players from an array.
     * @param currentTurn Gets the current turn number to decide who's turn it is.
     * @param gameStatus Gets the value of who's turn it currently is.
     * @return prompt Asks the corresponding user to pick a column to drop (repeated if they enter 
     * bad input).
    */
    public static String askToDrop(String[] getNames, int[] currentTurn, boolean[] gameStatus) {
        String prompt = "";
        if (currentTurn[0] % CHECK_IF_EVEN_NUM == 0) {
            gameStatus[0] = false;
        } else {
            gameStatus[0] = true;
        }
        if (gameStatus[0]) {
            prompt = (getNames[0] + ", enter the column to drop your checker: ");
        } else {
            prompt = (getNames[1] + ", enter the column to drop your checker: ");
        }
        return prompt;
    }


    /* Prints out who's turn it is and what pieces they are. Runs through error-checking before 
     * answer is returned.
     * @param getNames Gets the names of the players from an array.
     * @param key Scanner that reads in user input.
     * @param getPrompt Gets the statement that asks the corresponding user to enter a column to 
     * drop in.
     * @param getBoard Gets the current state of the game board.
     * @param gameStatus Gets the value of who's turn it currently is.
     * @return choice Column number that the user wants to drop their piece in.
    */
    public static int playerChoice(String[] getNames, Scanner key, String getPrompt, String[][] getBoard, boolean[] gameStatus) {
        int choice = 0;
        if (gameStatus[0]) {
            System.out.println("\n" + getNames[0] + " it is your turn.");
            System.out.println("Your pieces are the r's.");
            System.out.print(getPrompt);
            choice = errorCheckInput(choice, getPrompt, key, getBoard);
        } else {
            System.out.println("\n" + getNames[1] + " it is your turn.");
            System.out.println("Your pieces are the b's.");
            System.out.print(getPrompt);
            choice = errorCheckInput(choice, getPrompt, key, getBoard);
        }
        return choice;
    }


    /* Puts an "r" or a "b" depending on who's turn it is into the column that the player  
     * picked. Loops through the rows for the first empty spot in the column. Increments turns    
     * and pieces played.
     * @param getBoard Gets the current state of the game board.
     * @param getChoice Gets the choice of column that the player picked.
     * @param currentTurn Gets the current turn number to decide who's turn it is.
     * @param gameStatus Gets the value of who's turn it currently is.
    */
    public static void dropChips(String[][] getBoard, int getChoice, int[] currentTurn, boolean[]gameStatus) {
        int rowIndex = NUM_OF_ROWS - 1;
        if (gameStatus[0]) {
            while (getBoard[rowIndex][getChoice - 1] != EMPTY_SPACE && rowIndex >= 0) {
                rowIndex--;
            }
            getBoard[rowIndex][getChoice - 1] = RED_PIECE;
        } else {
            while (getBoard[rowIndex][getChoice - 1] != EMPTY_SPACE && rowIndex >= 0) {
                rowIndex--;
            }
            getBoard[rowIndex][getChoice - 1] = BLACK_PIECE;
        }
        currentTurn[0]++;
        currentTurn[1]++;
    }


    /* Checks the board for a win by 4 pieces vertically in a row. Games ends if a win is found
     * this way.
     * @param getBoard Gets the current state of the game board.
     * @param getChoice Gets the choice of column that the player picked.
     * @param gameStatus Gets the boolean value for if the game is still going on.
    */
    public static void checkVertically(String[][] getBoard, int getChoice, boolean[] gameStatus) {
        int consecutiveVerticalRed = 0;
        for (int row = 0; row < NUM_OF_ROWS; row++) {
            if (getBoard[row][getChoice - 1] == RED_PIECE) {
                consecutiveVerticalRed++;
            } else {
                consecutiveVerticalRed = 0;
            }
            if (consecutiveVerticalRed >= CONSEC_PIECES_TO_WIN) {
                gameStatus[1] = false;
            }
        }
        int consecutiveVerticalBlack = 0;
        for (int row = 0; row < NUM_OF_ROWS; row++) {
            if (getBoard[row][getChoice - 1] == BLACK_PIECE) {
                consecutiveVerticalBlack++;
            } else {
                consecutiveVerticalBlack = 0;
            }
            if (consecutiveVerticalBlack >= CONSEC_PIECES_TO_WIN) {
                gameStatus[1] = false;
            }
        }
    }


    /* Checks the board for a win by 4 pieces horizontally in a row. Games ends if a win is found
     * this way.
     * @param getBoard Gets the current state of the game board.
     * @param gameStatus Gets the boolean value for if the game is still going on.
    */
    public static void checkHorizontally(String[][] getBoard, boolean[] gameStatus) {
        int consecutiveHorizontalRed = 0;
        for (int row = 0; row <= NUM_OF_ROWS - 1; row++) {
            for (int col = 0; col < NUM_OF_COLUMNS; col++) {
                if (getBoard[row][col] == RED_PIECE) {
                    consecutiveHorizontalRed++;
                } else {
                    consecutiveHorizontalRed = 0;
                }
                if (consecutiveHorizontalRed >= CONSEC_PIECES_TO_WIN) {
                    gameStatus[1] = false;
                }
            }
        }
        int consecutiveHorizontalBlack = 0;
        for (int row = 0; row <= NUM_OF_ROWS - 1; row++) {
            for (int col = 0; col < NUM_OF_COLUMNS; col++) {
                if (getBoard[row][col] == BLACK_PIECE) {
                    consecutiveHorizontalBlack++;
                } else {
                    consecutiveHorizontalBlack = 0;
                }
                if (consecutiveHorizontalBlack >= CONSEC_PIECES_TO_WIN) {
                    gameStatus[1] = false;
                }
            }
        }   
    }


    /* Checks the board for a win by 4 pieces diagonally (south-east direction) in a row. Games  
     * ends if a win is found this way.
     * @param getBoard Gets the current state of the game board.
     * @param gameStatus Gets the boolean value for if the game is still going on.
    */
    public static void checkDiagonalSE(String[][] getBoard, boolean[] gameStatus) {
        for (int row = 0; row <= POSSIBLE_DIAG_ROW_INDEX; row++) {
            for (int col = 0; col <= POSSIBLE_DIAG_COL_INDEX; col++) {
                if (getBoard[row][col] == RED_PIECE && getBoard[row + 1][col + 1] == RED_PIECE && getBoard[row + POSSIBLE_DIAG_ROW_INDEX][col + POSSIBLE_DIAG_ROW_INDEX] == RED_PIECE && getBoard[row + POSSIBLE_DIAG_COL_INDEX][col + POSSIBLE_DIAG_COL_INDEX] == RED_PIECE) {
                    gameStatus[1] = false;
                }     
            }
        }
        for (int row = 0; row <= POSSIBLE_DIAG_ROW_INDEX; row++) {
            for (int col = 0; col <= POSSIBLE_DIAG_COL_INDEX; col++) {
                if (getBoard[row][col] == BLACK_PIECE && getBoard[row + 1][col + 1] == BLACK_PIECE && getBoard[row + POSSIBLE_DIAG_ROW_INDEX][col + POSSIBLE_DIAG_ROW_INDEX] == BLACK_PIECE && getBoard[row + POSSIBLE_DIAG_COL_INDEX][col + POSSIBLE_DIAG_COL_INDEX] == BLACK_PIECE) {
                    gameStatus[1] = false;
                }    
            }
        }
    }


    /* Checks the board for a win by 4 pieces diagonally (south-west direction) in a row. Games  
     * ends if a win is found this way.
     * @param getBoard Gets the current state of the game board.
     * @param gameStatus Gets the boolean value for if the game is still going on.
    */
    public static void checkDiagonalSW(String[][] getBoard, boolean[] gameStatus) {
        for (int row = 0; row <= POSSIBLE_DIAG_ROW_INDEX; row++) {
            for (int col = NUM_OF_COLUMNS - 1; col >= POSSIBLE_DIAG_COL_INDEX; col--) {
                if (getBoard[row][col] == RED_PIECE && getBoard[row + 1][col - 1] == RED_PIECE && getBoard[row + POSSIBLE_DIAG_ROW_INDEX][col - POSSIBLE_DIAG_ROW_INDEX] == RED_PIECE && getBoard[row + POSSIBLE_DIAG_COL_INDEX][col - POSSIBLE_DIAG_COL_INDEX] == RED_PIECE) {
                    gameStatus[1] = false;
                }    
            }
        }
        for (int row = 0; row <= POSSIBLE_DIAG_ROW_INDEX; row++) {
            for (int col = NUM_OF_COLUMNS - 1; col >= POSSIBLE_DIAG_COL_INDEX; col--) {
                if (getBoard[row][col] == BLACK_PIECE && getBoard[row + 1][col - 1] == BLACK_PIECE && getBoard[row + POSSIBLE_DIAG_ROW_INDEX][col - POSSIBLE_DIAG_ROW_INDEX] == BLACK_PIECE && getBoard[row + POSSIBLE_DIAG_COL_INDEX][col - POSSIBLE_DIAG_COL_INDEX] == BLACK_PIECE) {
                    gameStatus[1] = false;
                }    
            }
        }
    }


    /* Updates the game board every round after a piece has been dropped.
     * @param getBoard Gets the current state of the game board.
    */
    public static void updateBoard(String[][] getBoard) {
        int numRows = getBoard.length;
        int numCols = getBoard[0].length;
        for (int row = 0; row < numRows; row++) {
            System.out.print(getBoard[row][0]);
            for (int col = 1; col < numCols; col++) {
                System.out.print(" ");
                System.out.print(getBoard[row][col]);
            }
            System.out.println(" ");
        }
    }


    /* Loops through rounds, so long as the game hasn't ended and a draw hasn't happened.
     * @param getNames Gets the names of the players from an array.
     * @param key Scanner that reads in user input.
     * @param getBoard Gets the current state of the game board.
     * @param currentTurn Gets the current turn number to decide who's turn it is.
     * @param gameStatus Gets the boolean value for if the game is still going on.
    */
    public static void loopTurns(String[] getNames, Scanner key, String[][] getBoard, int[] currentTurn, boolean[] gameStatus) {
        while (gameStatus[1] && currentTurn[1] != DRAW) {
            String getPrompt = askToDrop(getNames, currentTurn, gameStatus);
            int getChoice = playerChoice(getNames, key, getPrompt, getBoard, gameStatus);
            dropChips(getBoard, getChoice, currentTurn, gameStatus);
            checkVertically(getBoard, getChoice, gameStatus);
            checkHorizontally(getBoard, gameStatus);
            checkDiagonalSE(getBoard, gameStatus);
            checkDiagonalSW(getBoard, gameStatus);
            if (gameStatus[1] && currentTurn[1] != DRAW) {
                printWhilePlaying();
                updateBoard(getBoard);
            }
        }
    }


    /* Prints the end results of who won or if it was a draw and the final board.
     * @param getBoard Gets the current state of the game board.
     * @param getNames Gets the names of the players from an array.
     * @param currentTurn Gets the current turn number to decide who's turn it is.
     * @param gameStatus Gets the value of who's turn it currently is.
    */
    public static void printResults(String[][] getBoard, String[] getNames, int[] currentTurn, boolean[] gameStatus) {
        if (currentTurn[1] == DRAW) {
            System.out.println("\nThe game is a draw.\n");
            System.out.println("Final Board");
            printColNums();
            updateBoard(getBoard);
        } else {
            if (gameStatus[0]) {
                System.out.println("\n" + getNames[0] + " wins!!\n");
                System.out.println("Final Board");
            } else {
                System.out.println("\n" + getNames[1] + " wins!!\n");
                System.out.println("Final Board");
            }
            printColNums();
            updateBoard(getBoard);
        }
    }


    /* Prompt the user for an int. The String prompt will
     * be printed out. key must be connected to System.in.
     * @param key Scanner that reads in user input.
     * @param getPrompt Gets the statement that asks the corresponding user to enter a column to 
     * drop in.
     * @return Player's column choice returned if it was an integer.
    */
    public static int getInt(Scanner key, String getPrompt) {
        while(!key.hasNextInt()) {
            String notAnInt = key.nextLine();
            System.out.println("\n" + notAnInt + " is not an integer.");
            System.out.print(getPrompt);
        }
        int choice = key.nextInt();
        key.nextLine();
        return choice;
    }


    /* Error checks the current player's input for if they entered a valid column number or if they 
     * picked a column that was full. If not, method for checking for an integer value is called 
     * again.
     * @param choice Players column choice if it passed through the integer check method (getInt).
     * @param getPrompt Gets the statement that asks the corresponding user to enter a column to 
     * drop in.
     * @param key Scanner that reads in user input.
     * @param getBoard Gets the current state of the game board.
     * @return choice Returns the player's column choice if it passed all error checks and is a 
     * valid input.
    */
    public static int errorCheckInput(int choice, String getPrompt, Scanner key, String[][] getBoard) {
        choice = getInt(key, getPrompt);
        while (choice - 1 < 0 || choice - 1 >= NUM_OF_COLUMNS || getBoard[0][choice - 1] != EMPTY_SPACE) {
            if (choice - 1 < 0 || choice - 1 >= NUM_OF_COLUMNS) {
                System.out.println("\n" + choice + " is not a valid column.");
                System.out.print(getPrompt);
            } else if (getBoard[0][choice - 1] != EMPTY_SPACE) {
                System.out.println("\n" + choice + " is not a legal column. That column is full");
                System.out.print(getPrompt);
            }
            choice = getInt(key, getPrompt);
        }
        return choice;
    }

}