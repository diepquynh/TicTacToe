package utils;

import components.HistoryNode;
import model.GameModel;

import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.StringJoiner;
import java.util.stream.IntStream;

public class DatabaseUtils {
    public static void createNewDatabase() throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(Constants.DB_URL);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void createHistoryTable() throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        try {
            resetResumeDatabase();
            conn = DriverManager.getConnection(Constants.DB_URL);
            String sql = "CREATE TABLE IF NOT EXISTS gameHistory (\n"
                    + "	gameId INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "	playDate DATETIME DEFAULT (DATETIME()),\n"
                    + "	gameMode INT DEFAULT 4,\n"
                    + "	firstPlayer NVARCHAR(20) NOT NULL,\n"
                    + "	secondPlayer NVARCHAR(20) NOT NULL,\n"
                    + "	score NVARCHAR(10) NOT NULL\n"
                    + ");";

            stmt = conn.createStatement();
            stmt.execute(sql);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static HistoryNode[] loadHistoryFromDatabase() throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        HistoryNode[] nodes;
        int numNodes;
        int count = 0;

        try {
            createHistoryTable();
            conn = DriverManager.getConnection(Constants.DB_URL);
            stmt = conn.createStatement();
            String sql = "SELECT COUNT(*) AS count FROM gameHistory";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            numNodes = Integer.parseInt(rs.getString("count"));
            if (numNodes == 0) {
                rs.close();
                return null;
            }

            nodes = new HistoryNode[numNodes];
            sql = "SELECT * FROM gameHistory";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                nodes[count] = new HistoryNode();
                Timestamp ts = new Timestamp(rs.getLong("playDate"));
                nodes[count].setDate(ts.toString());
                nodes[count].setGameMode(
                        rs.getInt("gameMode") == Constants.GameModes.MODE_HUMAN ? "Human" : "Computer");
                nodes[count].setFirstPlayer(rs.getString("firstPlayer"));
                nodes[count].setSecondPlayer(rs.getString("secondPlayer"));
                nodes[count].setScore(rs.getString("score"));
                count++;
            }
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return nodes;
    }

    public static void saveHistoryToDatabase(GameModel m) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            createHistoryTable();
            conn = DriverManager.getConnection(Constants.DB_URL);
            String insert = "INSERT INTO gameHistory " +
                    "(playDate, gameMode, firstPlayer, secondPlayer, score) VALUES (?, ?, ?, ?, ?);";

            stmt = conn.prepareStatement(insert);
            stmt.setTimestamp(1, new Timestamp(Calendar.getInstance().getTime().getTime()));
            stmt.setInt(2, m.getGameMode());
            stmt.setString(3, m.getFirstPlayerName());
            stmt.setString(4, m.getSecondPlayerName());

            StringJoiner sj = new StringJoiner("-");
            IntStream.of(m.getScores()).forEach(x -> sj.add(String.valueOf(x)));
            stmt.setString(5, sj.toString());

            stmt.execute();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void resetResumeDatabase() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(Constants.DB_URL);

            String truncate = "DELETE FROM previousGame";
            stmt = conn.prepareStatement(truncate);
            stmt.execute();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void createGameDataTable() throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(Constants.DB_URL);
            String sql = "CREATE TABLE IF NOT EXISTS previousGame (\n"
                    + "	currPlayerId INT NOT NULL,\n"
                    + "	movesCount INT NOT NULL,\n"
                    + "	board NVARCHAR(4000) NOT NULL,\n"
                    + "	boardSize INT DEFAULT 3,\n"
                    + "	gameMode INT DEFAULT 4,\n"
                    + "	scores NVARCHAR(10) NOT NULL,\n"
                    + "	firstPlayer NVARCHAR(50) NOT NULL,\n"
                    + "	secondPlayer NVARCHAR(50) NOT NULL,\n"
                    + "	CONSTRAINT pk_currPlayerId PRIMARY KEY (currPlayerId)\n"
                    + ");";

            stmt = conn.createStatement();
            stmt.execute(sql);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void saveGameDataToDatabase(GameModel m) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            createGameDataTable();
            resetResumeDatabase();
            conn = DriverManager.getConnection(Constants.DB_URL);
            String insert = "INSERT INTO previousGame VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            StringBuilder gameBoard = new StringBuilder();

            stmt = conn.prepareStatement(insert);
            stmt.setInt(1, m.getCurrPlayerId());
            stmt.setInt(2, m.getMovesCount());
            for (int i = 0; i < m.getBoardSize(); i++) {
                for (int j = 0; j < m.getBoardSize(); j++) {
                    gameBoard.append(m.getBoard()[i][j]).append(",");
                }
            }
            stmt.setString(3, gameBoard.toString());
            stmt.setInt(4, m.getBoardSize());
            stmt.setInt(5, m.getGameMode());

            StringJoiner sj = new StringJoiner("-");
            IntStream.of(m.getScores()).forEach(x -> sj.add(String.valueOf(x)));
            stmt.setString(6, sj.toString());

            stmt.setString(7, m.getFirstPlayerName());
            stmt.setString(8, m.getSecondPlayerName());

            stmt.execute();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static GameModel loadGameFromDatabase() throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        GameModel gm;

        try {
            createGameDataTable();
            conn = DriverManager.getConnection(Constants.DB_URL);
            stmt = conn.createStatement();
            String sql = "SELECT COUNT(*) AS count FROM previousGame";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();

            if (Integer.parseInt(rs.getString("count")) == 0) {
                rs.close();
                return null;
            }

            sql = "SELECT * FROM previousGame";
            rs = stmt.executeQuery(sql);
            gm = new GameModel();
            while (rs.next()) {
                gm.setCurrPlayerId(rs.getInt("currPlayerId"));
                gm.setMovesCount(rs.getInt("movesCount"));
                gm.setBoardSize(rs.getInt("boardSize"));

                int boardSize = gm.getBoardSize();
                String[] data = rs.getString("board").split(",");
                char[][] board = new char[boardSize][boardSize];
                for (int i = 0; i < boardSize; i++) {
                    for (int j = 0; j < boardSize; j++) {
                        board[i][j] = data[i * boardSize + j].toCharArray()[0];
                    }
                }
                gm.setBoard(board);
                gm.setGameMode(rs.getInt("gameMode"));
                gm.setScores(Arrays.stream(rs.getString("scores").split("-")).mapToInt(Integer::parseInt).toArray());
                gm.setFirstPlayerName(rs.getString("firstPlayer"));
                gm.setSecondPlayerName(rs.getString("secondPlayer"));
            }
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return gm;
    }
}
