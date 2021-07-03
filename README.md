# GameMain using MVC paradigm
Original source: https://github.com/deepc94/TicTacToe

GameMain.java is a basic Java implementation of the well-known Tic Tac Toe game using MVC approach.

### Design Choices
#### Model (M) - Contains the current state of the game and logic for checking win or tie
#### View (V) - Contains the graphical user interface for interacting with the game
#### Controller (C) - Contains the interface between View and Model

### Game Flow

The driver class creates instances of `Model`, `View` and `Controller` classes. The game starts with the `View` populating the graphical user interface and adding buttons for control. The `Adapter` then monitors the button for an event, and defines the `actionPerformed` for the `ActionListeners`. The `Adapter` class transfers control to the `Controller` class which notifies the `Model` that a move has been made on the (x,y) coordinate of the tic tac toe grid. The `Model` then updates its board state and accordingly updates the `View`. It also checks after every move if a winning condition has been achieved by a player or whether the game has tied and notifies the `View` with an appropriate message. The `Reset` button allows the user to clear the baord and start a new game.

### Classes and Functionality

#### GameMain class
This is the Driver class for the GameMain game. It creates objects of Model, View and Controller classes and aggregates them.

#### Model Class
The model class is where the current state of the game as well as the winning logic resides. The model class calls the view to update the gui according to the current state of the game.

Members:
```java
private View v
private int playerId
private int movesCount
private char[][] board
private String message
```

Methods (apart from getters and setters:
```java
public Model()
public void registerView(View v)
public void PlayMove(int x, int y)
public boolean isWinner(int x, int y)
public void ResetModel()
```

#### View Class
The View class is responsible for setting up the gui and displaying the state of the game on the gui as informed by the model.

Members:
```java
private Adapter adapter
private JFrame gui
private JButton[][] blocks
private JButton reset
private JTextArea playerturn
```

Methods:
```java
public View() 
public void setActionListener(Controller c)
public void initialize ()
public boolean isReset(ActionEvent e) 
public ArrayList<Integer> getPosition(ActionEvent e) 
public void update(int row, int column, char symbol, String message)
public void isWinner(int row, int column, char symbol, String message)
public void resetGame()
```

#### Controller Class
The Controller class is responsible for requesting the model to update its state whenever there is an event on a button on the game board.

Members:
```java
private Model m
```

Methods:
```java
public void setModel(Model m) 
public void setRequest(ArrayList<Integer> position) 
public void setRequest()
```

#### Adapter Class implements ActionListener
The adapter class acts as an interface between view and controller to allow decoupling. It listens for actions on the buttons and invokes the controller class to take appropriate action.

Members:
```java
private Controller c
private View v
```

Methods:
```java
public Adapter(Controller c, View v)
public void actionPerformed(ActionEvent e)
```

### How to build and test (from Terminal):

1. Make sure that you have Apache Ant installed.

2. Run `ant` in the root directory, which contains the `build.xml` build file.

3. Compiled java classes will be in the `bin` directory.

4. Run `ant test` to run all unit tests.

### How to run (from Terminal):

1. After building the project (i.e., running `ant`), run:
   `java -cp bin GameMain`

### How to clean up (from Terminal):

1. Run `ant clean` to clean the project (i.e., delete all generated files).
