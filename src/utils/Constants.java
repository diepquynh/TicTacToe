package utils;

public interface Constants {
    // UI interface
    public interface UIConstants {
        public static final int UI_MENU = 0;
        public static final int UI_SELECTION = 1;
        public static final int UI_GAME = 2;
        public static final int UI_HISTORY = 3;
    }

    // Game modes
    public interface GameModes {
        public static final int MODE_COMPUTER = 0;
        public static final int MODE_COMPUTER_EASY = 1;
        public static final int MODE_COMPUTER_MEDIUM = 2;
        public static final int MODE_COMPUTER_HARD = 3;
        public static final int MODE_HUMAN = 4;
    }

    // Board size
    public static final int BOARD_SIZE_DEFAULT = 3;

    // Database configuration
    public static final String DB_URL = "jdbc:sqlite:tictactoe.db";
}
