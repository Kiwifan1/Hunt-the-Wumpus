
/* Daniel Popa
 * Team: Artesian Code
 *Graphical interface displays what the 
 *user will see and interact with
 *First Created 3/4/2019
 *
 * Date of revision  |  Revision made
 *     3/12/2019     |  Added method headers
 *     3/25/2019     |  Made test window for GP
 *     3/26/2019     |  Made test homescreen with buttons
 *     3/28/2019     |  Did access stuff for goup in javafx, also modded menu methods to return which button was pressed.
 *     4/3/2019      |  Can switch to display scores, still crashes after screen change and functionality isn't complete
 *     4/21/2019     |  Replaced all the main menue code with StdDraw stuff ive been working on in a different file
 *     4/24/2019     | Made navigation a bit easier, no dead ends, added cave selection and return of main menue changed, will duiscus in class
 *     5/26/2019     | Havent been good at documenting edits, main menu is almost completely clean, startng game graphics soon
 */
import java.util.*;
import java.awt.Font;
import java.util.ArrayList;

//hi
public class GraphicalInterface {

	private int BAT;
	private int WUMPUS;
	private int HOLE;
	private BallSprite[] player;
	private FlashingText WARNING;
	private String NAME;
	private static int[] ActiveColor = { 150, 0, 0 };
	private static int[] TextColor = { 250, 250, 250 };
	private static int[] InactiveColor = { 32, 32, 32 };
	private static int[] BackColor = { 0, 0, 0 };
	private static int[] ClosedDoor = { 100, 100, 100 };
	private static boolean scaryTheme = true;

	public GraphicalInterface(int b, int w, int h) {
		BAT = b;
		WUMPUS = w;
		HOLE = h;
		NAME = "";
		resetAndCreateText();
		resetAndCreatePlayer();

	}

	// Changes the themes depending on the boolean scary, scary makes the dark theme
	// if true, or light theme if false
	private static void changeToScary(boolean scary) {

		if (scary) {
			// RED
			ActiveColor = new int[] { 150, 0, 0 };
			// WHITE
			TextColor = new int[] { 250, 250, 250 };
			// GRAY
			InactiveColor = new int[] { 32, 32, 32 };
			// BLACK
			BackColor = new int[] { 0, 0, 0 };
			// Light gray
			ClosedDoor = new int[] { 100, 100, 100 };
		}

		else {
			// Orange
			ActiveColor = new int[] { 240, 110, 0 };
			// WHITE
			TextColor = new int[] { 250, 250, 250 };
			// Blue
			InactiveColor = new int[] { 3, 37, 108 };
			// LightBlue
			BackColor = new int[] { 0, 160, 225 };
			//
			ClosedDoor = new int[] { 0, 139, 178 };
		}

	}

	private static void setBackColor() {
		StdDraw.setPenColor(BackColor[0], BackColor[1], BackColor[2]);
	}

	private static void setActiveColor(int shift) {
		if (ActiveColor[0] + shift > 255) {

			if (ActiveColor[0] + shift > 300) {
				StdDraw.setPenColor(255, ActiveColor[1] - shift / 3, ActiveColor[2]);
			} else {
				StdDraw.setPenColor(255, ActiveColor[1], ActiveColor[2]);
			}
		} else {
			StdDraw.setPenColor(ActiveColor[0] + shift, ActiveColor[1], ActiveColor[2]);
		}
	}

	private static void setActiveColor() {
		StdDraw.setPenColor(ActiveColor[0], ActiveColor[1], ActiveColor[2]);
	}

	private static void setTextColor() {
		StdDraw.setPenColor(TextColor[0], TextColor[1], TextColor[2]);
	}

	private static void setInactiveColor() {
		StdDraw.setPenColor(InactiveColor[0], InactiveColor[1], InactiveColor[2]);
	}

	private static void setClosedDoor() {
		StdDraw.setPenColor(ClosedDoor[0], ClosedDoor[1], ClosedDoor[2]);
	}

	// Opens up the canvas for the game to be displayed on and enables double
	// buffering
	public static void start() {
		StdDraw.enableDoubleBuffering();
		StdDraw.setCanvasSize(1000, 700);
	}

	// returns room or -1 for shoot arrow, -2 for buy a hint and -3 if they player
	// wants to go to main menu
	public int getRoom(int room1, int room2, int room3, int[] danger, int turn, int coins, int arrows) {

		double buttonwidth = 0.1;
		double buttonheight = 0.055;
		double shift = (buttonwidth * 2);
		double buttonleft = 0.2;

		int toreturn;
		double[] ballcords = new double[2];
		StdDraw.clear();
		background();
		room();
		WariningSign(danger);
		ballcords = drawPlayer();
		toreturn = doors(room1, room2, room3, ballcords);

		HUDtext(0.5, 0.175, NAME);
		HUDtext(0.9, 0.8, "Round " + turn);
		HUDtext(0.9, 0.77, "Coins " + coins);
		HUDtext(0.9, 0.74, "Arrows " + arrows);

		// ballcords = drawPlayer();
		if (button(buttonleft, 0.1, buttonwidth, buttonheight, "Buy Item", false)) {
			toreturn = -2;
		}

		if (button(buttonleft + shift * 1, 0.1, buttonwidth, buttonheight, "Shoot Arrow", false)) {
			toreturn = -1;
		}

		if (button(buttonleft + shift * 2, 0.1, buttonwidth, buttonheight, "Main Menu", false)) {
			if (toMainMenu()) {
				toreturn = -3;
			}
		}

		if (button(buttonleft + shift * 3, 0.1, buttonwidth, buttonheight, "Exit", false)) {
			Exit();
		}

		StdDraw.show();
		// System.out.println(toreturn);
		return toreturn;
	}

	// Draws the screen where you shoot an arrow, it loops untill you click on a
	// room or press back
	public int shootArrow(int room1, int room2, int room3, int[] danger, int turn, int coins, int arrows) {
		int toreturn = 0;
		boolean loop = true;

		while (loop) {
			StdDraw.clear();
			background();
			room();
			WariningSign(danger);
			double[] mouse = { StdDraw.mouseX(), StdDraw.mouseY() };
			toreturn = doors(room1, room2, room3, mouse);

			HUDtext(0.5, 0.175, NAME);
			HUDtext(0.9, 0.8, "Round " + turn);
			HUDtext(0.9, 0.77, "Coins " + coins);
			HUDtext(0.9, 0.74, "Arrows " + arrows);

			loop = !button(0.5, 0.1, 0.15, 0.055, "Back", false);

			Font font = new Font("Copperplate Gothic Bold", 0, 40);
			setActiveColor(100);
			StdDraw.setFont(font);
			StdDraw.text(0.5, 0.25, "Click on a Room to Shoot");
			// ballcords = drawPlayer();

			arrowPath(toreturn);
			StdDraw.show();
			// System.out.println(toreturn);

			if (toreturn > 0 && ClickedRelease()) {
				loop = false;
			}
		}

		return toreturn;
	}

	// Draws a line representing the path of the arrow from the center of the screen
	private void arrowPath(int room) {
		setActiveColor();
		if (room > 0) {
			setActiveColor(100);
		}
		StdDraw.setPenRadius(0.02);
		StdDraw.line(0.5, 0.5, StdDraw.mouseX(), StdDraw.mouseY());
		StdDraw.setPenRadius();
	}

	// Displays a waning sign on the top of the screen, takes an int array of the
	// possible hazards from Game control
	private void WariningSign(int[] danger) {
		int dangers = 0;
		int size = 0;
		String message = " | ";

		for (int i = 0; i < danger.length; i++) {
			if (danger[i] > 0) {
				dangers++;
				message += dangerMessage(i) + " | ";
			}
		}

		if (dangers == 3) {
			size = 30;
		}

		else if (dangers == 2) {
			size = 40;
		}

		else if (dangers == 1) {
			size = 60;
		}

		WARNING.draw(message, 0.5, 0.9, size);

	}

	// Returns a string message for a hazard based on the number given in the
	// graphical interface constructor for hazards
	private String dangerMessage(int danger) {
		String toreturn = "";
		if (danger == BAT) {
			toreturn += "There is a bat nearby";
		}

		if (danger == WUMPUS) {
			toreturn += "I smell a wumpus";
		}

		if (danger == HOLE) {
			toreturn += "I feel a draft";
		}

		return toreturn;
	}

	// Returns a string for a hazard based on the number given in the graphical
	// interface constructor for hazards
	private String getDanger(int danger) {
		String toreturn = "";
		if (danger == BAT) {
			toreturn += "BAT";
		}

		if (danger == WUMPUS) {
			toreturn += "WUMPUS";
		}

		if (danger == HOLE) {
			toreturn += "PIT";
		}

		return toreturn;
	}

	// Returns coordinates of the player ball and draws all of the players
	// "particles"
	private double[] drawPlayer() {
		for (int i = 0; i < 499; i++) {
			player[i].draw();
			// System.out.println("draw"+i);
		}
		return player[499].draw();
	}

	// Return room that player chose, or clicked
	private int doors(int room1, int room2, int room3, double[] ballcords) {
		int toreturn = 0;

		if (leftdoor(room1, ballcords)) {
			toreturn = room1;

			resetAndCreatePlayer();
			// System.out.println(room1);
		}

		if (rightdoor(room2, ballcords)) {
			toreturn = room2;

			resetAndCreatePlayer();
			// System.out.println(room2);
		}

		if (topdoor(room3, ballcords)) {
			toreturn = room3;

			resetAndCreatePlayer();

			// System.out.println(room3);
		}

		// System.out.println(toreturn);

		return toreturn;

	}

	// Makes player null, then creates the player again at the center of the screen
	private void resetAndCreatePlayer() {
		player = null;
		player = new BallSprite[500];
		for (int i = 0; i < 500; i++) {
			player[i] = new BallSprite(0.5, 0.5, 0.01, (0.01 / 500) * i, 0.3007, 0.3007);
		}
	}

	// Make stext null and recreates it, this lets it change colors when themes
	// change
	private void resetAndCreateText() {
		WARNING = null;
		WARNING = new FlashingText(350, ActiveColor[0], ActiveColor[1], ActiveColor[2]);
	}

	// Draws a black background
	private static void background() {
		setBackColor();
		StdDraw.filledRectangle(0.5, 0.5, 0.5, 0.5);
	}

	// Draws the large gray square in the center of the screen that is the room
	private static void room() {
		strip(0.5, 0.5, 0.3, 0.3);
	}

	// Draws the left door and returns true if the x y coordinates in the double
	// array are in the doors hitbox
	// It also takes the door number as input
	private static boolean leftdoor(int room, double[] ballcords) {

		setClosedDoor();

		if (room > 0) {
			setTextColor();
			Font lable = new Font("Copperplate Gothic Bold", 0, 20);
			StdDraw.setFont(lable);
			StdDraw.text(0.17, 0.5, "" + room);
			setActiveColor();
		}

		StdDraw.filledRectangle(0.2, 0.5, 0.007, 0.2);

		return inBox(0.15, 0.5, 0.057, 0.2, ballcords[0], ballcords[1]);
	}

	// Draws the right door and returns true if the x y coordinates in the double
	// array are in the doors hitbox
	// It also takes the door number as input
	private static boolean rightdoor(int room, double[] ballcords) {
		setClosedDoor();

		if (room > 0) {
			setTextColor();
			Font lable = new Font("Copperplate Gothic Bold", 0, 20);
			StdDraw.setFont(lable);
			StdDraw.text(0.83, 0.5, "" + room);
			setActiveColor();
		}

		StdDraw.filledRectangle(0.8, 0.5, 0.007, 0.2);
		return inBox(0.85, 0.5, 0.057, 0.2, ballcords[0], ballcords[1]);
	}

	// Draws the top door and returns true if the x y coordinates in the double
	// array are in the doors hitbox
	// It also takes the door number as input
	private static boolean topdoor(int room, double[] ballcords) {
		setClosedDoor();

		if (room > 0) {
			setTextColor();
			Font lable = new Font("Copperplate Gothic Bold", 0, 20);
			StdDraw.setFont(lable);
			StdDraw.text(0.5, 0.83, "" + room);
			setActiveColor();
		}

		StdDraw.filledRectangle(0.5, 0.8, 0.2, 0.007);
		return inBox(0.5, 0.85, 0.2, 0.057, ballcords[0], ballcords[1]);

	}

	// Returns the key typed by the user, or -1 if the delete button was pressed
	private static String getKeyTyped() {
		char typed;
		if (StdDraw.hasNextKeyTyped()) {
			typed = StdDraw.nextKeyTyped();
			// System.out.print((int)(typed));
			if ((int) typed == 8) {
				return "-1";
			}

			return typed + "";
		}

		return "";
	}

	// This deletes the last character in a string and returns that string
	private static String delete(String s) {
		if (s.length() > 0) {
			return s.substring(0, s.length() - 1);
		}

		return "";
	}

	// Returns the name selected by the player, this will not allow the name to be
	// spaces, or empty
	// It will display "Type a name", and will not let you play until you type a
	// name,
	// even if leave your name as "Type a name" it will not let you play
	public String getName() {
		String name = "";
		boolean button = true;
		String typed = "";
		int charLimit = 20;
		boolean priority = true;

		while (button) {
			StdDraw.clear();
			background();

			strip(0.5, 0.5, 0.30, 0.5);

			title(0.5, 0.9, "Type Your Name");
			title(0.5, 0.5, name);
			StdDraw.line(0.3, 0.47, 0.7, 0.47);
			try {
				typed = getKeyTyped();
			} catch (IllegalFormatException e) {
				System.out.println("Error Occured : GetName Method");
				Error();
			}

			if (typed.length() > 0 && name.equals("Type a name")) {
				name = "";
			}

			if (button(0.5, 0.21, 0.15, 0.055, "Play", priority) || enterkey(typed)) {
				button = false;
				typed = "";
			}

			if (inBox(0.5, 0.21, 0.15, 0.055) || inBox(0.5, 0.1, 0.15, 0.055)) {
				priority = false;
			}

			if (typed.equals("-1")) {
				name = delete(name);
			} else if (!((typed.length() > 0 && (((int) (typed.charAt(0)) == 127))))) {
				name += typed;
			}

			if (name.length() > charLimit)// name length limit
			{
				name = delete(name);
			}

			if (button(0.5, 0.1, 0.15, 0.055, "Main Menu", false)) {
				return "";
			}

			if (!button && (name.equals("") || name.equals("Type a name") || !hasChar(name))) {
				name = "Type a name";
				button = true;
			}

			StdDraw.show();
		}

		StdDraw.clear();
		NAME = name;
		return name;
	}

	private static boolean enterkey(String typed) {
		return typed.length() > 0 && ((int) (typed.charAt(0)) == 10);
	}

	// Returns the number of the cave selected by the user, or can go back to main
	// menu by calling main menu
	public int caveSelection(ArrayList<String> caves, ArrayList<String> names, ArrayList<String> scores,
			boolean saved) {
		double x = 0.2;
		double y = 0.5;
		double containerx = 0.15;
		double containery = 0.5;
		double shift = 0.2;
		while (true) {
			StdDraw.clear();
			background();

			strip(x, y, containerx, containery);

			title(containerx + 0.05, 0.9, "Hunt the Wumpus");

			if (menubutton(x, 0.85 - shift, containerx, 0.055, "Cave 1", false))

				return 1;

			if (menubutton(x, 0.74 - shift, containerx, 0.055, "Cave 2", false))
				return 2;

			if (menubutton(x, 0.63 - shift, containerx, 0.055, "Cave 3", false))
				return 3;

			if (menubutton(x, 0.52 - shift, containerx, 0.055, "Cave 4", false))
				return 4;

			if (menubutton(x, 0.41 - shift, containerx, 0.055, "Cave 5", false))
				return 5;

			if (menubutton(x, 0.30 - shift, containerx, 0.055, "Main Menu", false))
				return mainmenu(caves, names, scores, saved);

			StdDraw.show();
		}

	}

	// This reads inputs from the main menu screen and helps the navigation between
	// main menu, high scores, and credits,
	// returns the cave selected or -1 if the player wants to resume their game
	public int mainmenu(ArrayList<String> caves, ArrayList<String> names, ArrayList<String> scores, boolean saved) {
		// System.out.println(scores);
		int select = 0;
		StdDraw.enableDoubleBuffering();
		while (select == 0) {
			StdDraw.clear();
			select = menubuttons(saved);
		}

		if (select == 2) {
			highScores(caves, names, scores);
			return mainmenu(caves, names, scores, saved);

		}

		if (select == 3) {
			teamMessage();
			return mainmenu(caves, names, scores, saved);
		}

		if (select == 4) {
			return 0;
		}

		if (select == 5) {
			setTheme();
			return mainmenu(caves, names, scores, saved);
		}

		StdDraw.clear();
		return caveSelection(caves, names, scores, saved);
	}

	// This displays the main menu screen, returns 0 for no input, 1 for play, 2 for
	// high scores and 3 for credits,
	// this method does NOT loop by itself, if the game has been saved, the boolean
	// tells the main menu that the game was saved and shows the "resume" button
	private static int menubuttons(boolean saved) {
		double x = 0.5;
		double y = 0.5;
		double containerx = 0.15;
		double containery = 0.5;
		int toreturn = 0;
		double buttontop = 0.625;
		double shift = 0.11;

		StdDraw.clear();
		background();

		strip(x, y, containerx, containery);

		title(x, 0.85, "Hunt The Wumpus");

		if (saved) {

			if (menubutton(x, buttontop + shift, containerx, 0.055, "RESUME GAME", false))
				toreturn = 4;
		}

		if (menubutton(x, buttontop, containerx, 0.055, "NEW GAME", false))

			toreturn = 1;

		if (menubutton(x, buttontop - shift, containerx, 0.055, "THEME", false))
			toreturn = 5;

		if (menubutton(x, buttontop - shift * 2, containerx, 0.055, "HIGH SCORES", false))
			toreturn = 2;

		if (menubutton(x, buttontop - shift * 3, containerx, 0.055, "CREDITS", false))
			toreturn = 3;

		if (menubutton(x, buttontop - shift * 4, containerx, 0.055, "HOW TO PLAY", false))
			tutorial();

		if (menubutton(x, buttontop - shift * 5, containerx, 0.055, "EXIT", false))
			Exit();

		StdDraw.show();
		return toreturn;

	}

	// Displays buttons that just turn red when hovering over them or if they have
	// priority
	private static boolean button(double x, double y, double high, double wide, String message, boolean priority) {
		boolean hovering = inBox(x, y, high, wide);
		String typed = getKeyTyped();
		setInactiveColor();
		if (hovering)
			setActiveColor();

		StdDraw.filledRectangle(x, y, high, wide);
		setTextColor();
		Font buttonfont;
		buttonfont = new Font("Copperplate Gothic Bold", 0, 20);
		StdDraw.setFont(buttonfont);
		StdDraw.text(x, y, message);

		if (priority && enterkey(typed)) {
			return true;
		}

		return hovering && ClickedRelease();
	}

	// Displays buttons that get bigger and turn red when hovering over them or if
	// they have priority
	private static boolean menubutton(double x, double y, double high, double wide, String message, boolean priority) {
		double thick = 0.015;
		boolean hovering = inBox(x, y, high, wide);
		String typed = getKeyTyped();
		setInactiveColor();
		StdDraw.filledRectangle(x, y, high, wide);
		if (hovering) {
			setActiveColor();
			StdDraw.filledRectangle(x, y, high + thick, wide);
		}

		setTextColor();
		Font buttonfont = new Font("Copperplate Gothic Bold", 0, 20);
		StdDraw.setFont(buttonfont);
		StdDraw.text(x, y, message);

		if (priority && enterkey(typed)) {
			return true;
		}

		return hovering && ClickedRelease();
	}

	// Displays large text
	private static void title(double x, double y, String message) {
		Font titlefont = new Font("Copperplate Gothic Bold", 0, 27);
		setTextColor();
		StdDraw.setFont(titlefont);
		StdDraw.text(x, y, message);
	}

	// Return true if the mouse is inside the box given by the first for inputs
	private static boolean inBox(double xcenter, double ycenter, double height, double width) {
		double x = StdDraw.mouseX();
		double y = StdDraw.mouseY();
		// System.out.println(x+" "+y);
		if ((x < xcenter - (height) || x > xcenter + (height))) {
			return false;
		}

		if ((y < ycenter - (width) || y > ycenter + (width))) {

			return false;
		}

		// System.out.println("YEE");
		// System.out.println(x+" "+y);
		return true;

	}

	// Return true if the x and y cord inputs are inside the box given by the first
	// for inputs
	private static boolean inBox(double xcenter, double ycenter, double height, double width, double xcord,
			double ycord) {
		double x = xcord;
		double y = ycord;
		// System.out.println(x+" "+y);
		if ((x < xcenter - (height) || x > xcenter + (height))) {
			return false;
		}

		if ((y < ycenter - (width) || y > ycenter + (width))) {

			return false;
		}

		// System.out.println("YEE");
		// System.out.println(x+" "+y);
		return true;

	}

	// Return true if the player clicked and released or pressed enter
	private static boolean ClickedRelease() {

		if (StdDraw.isMousePressed()) {
			while (StdDraw.isMousePressed()) {
				// do nothing
			}
			return true;
		}
		return false;
	}

	// Return true if the player clicked
	private static boolean Clicked() {
		return StdDraw.isMousePressed();
	}

	// This method displays the team highscores, this method takes in scores so that
	// it can work better in the main menu class
	private static void highScores(ArrayList<String> caves, ArrayList<String> names, ArrayList<String> scores) {
		boolean toMain = true;
		boolean priority = true;

		while (toMain) {
			StdDraw.clear();
			background();

			strip(0.5, 0.5, 0.30, 0.5);

			title(0.5, 0.9, "High Scores");

			printScoreRow(caves, 0.3, 0.7, 25, "CAVE");
			printScoreRow(names, 0.5, 0.7, 25, "NAME");
			printScoreRow(scores, 0.7, 0.7, 25, "SCORE");

			toMain = !button(0.5, 0.1, 0.15, 0.055, "Back", priority);

			StdDraw.show();
		}

	}

	// Prints the high score row with a line under the score title
	private static void printScoreRow(ArrayList<String> info, double x, double y, int size, String string) {
		Font scoresfont = new Font("Copperplate Gothic Bold", 0, size);
		setTextColor();
		StdDraw.setFont(scoresfont);

		StdDraw.text(x, y + 0.07, string);

		StdDraw.setPenRadius(0.005);
		StdDraw.line(x + 0.05, y + 0.04, x - 0.05, y + 0.04);
		StdDraw.setPenRadius();
		for (int i = 0; i < Math.min(10, info.size()); i++) {
			StdDraw.text(x, y - (0.05 * i), info.get(i));
		}
	}

	// This method displays the team credits, this method takes in scores so that it
	// can work better in the main menu class

	// Between every turn this screen must display, it loops by itself and stops
	// when you press the next button,
	// It tells the player a hint, the room they are in, coins they have and arrows
	// This loops by itself and is stopped with a next button
	public static void betweenTurns(String hint, int room, int turn, int coins, int arrows, boolean bonusarrow,
			int bonuscoin) {
		boolean waiting = true;
		boolean priority = true;
		ArrayList<String> toprint = new ArrayList<String>();
		toprint = splitUp(hint);

		while (waiting) {

			StdDraw.clear();

			background();

			strip(0.5, 0.5, 0.30, 0.5);

			setTextColor();
			Font title = new Font("Copperplate Gothic Bold", 0, 60);
			StdDraw.setFont(title);
			StdDraw.text(0.5, 0.85, "Round " + turn);

			title(0.5, 0.8, "Room " + room);

			if (bonuscoin > 0) {
				if (bonuscoin == 1) {
					title(0.5, 0.75, "Coins " + (coins - bonuscoin) + " + " + bonuscoin + " bonus coin");
				}

				else {
					title(0.5, 0.75, "Coins " + (coins - bonuscoin) + " + " + bonuscoin + " bonus coins");
				}
			}

			else {
				title(0.5, 0.75, "Coins " + coins);
			}

			if (bonusarrow) {
				title(0.5, 0.7, "Arrows " + (arrows - 1) + " and a bonus arrow");
			} else {
				title(0.5, 0.7, "Arrows " + arrows);
			}

			Font font = new Font("Copperplate Gothic Bold", 0, 30);

			for (int i = 0; i < toprint.size(); i++) {
				setTextColor();
				StdDraw.setFont(font);
				StdDraw.text(0.5, 0.5 - (0.05 * i), toprint.get(i));
			}

			waiting = !button(0.5, 0.1, 0.15, 0.055, "Next", priority);

			StdDraw.show();
		}
	}

	// If the player successfully bought a secret, this creen takes the secret as a
	// string and displayes it it the player
	// this has a next button
	public static void tellSecret(String secret) {
		boolean waiting = true;
		ArrayList<String> toprint = new ArrayList<String>();
		toprint = splitUp(secret);
		boolean priority = false;

		while (waiting) {

			StdDraw.clear();

			background();

			strip(0.5, 0.5, 0.30, 0.5);

			setTextColor();
			Font title = new Font("Copperplate Gothic Bold", 0, 60);
			StdDraw.setFont(title);
			StdDraw.text(0.5, 0.85, "Secret");

			Font font = new Font("Copperplate Gothic Bold", 0, 30);

			for (int i = 0; i < toprint.size(); i++) {
				setTextColor();
				StdDraw.setFont(font);
				StdDraw.text(0.5, 0.5 - (0.05 * i), toprint.get(i));
			}

			waiting = !button(0.5, 0.1, 0.15, 0.055, "Next", priority);
			if (inBox(0.5, 0.1, 0.15, 0.055)) {
				priority = false;
			}
			StdDraw.show();
		}
	}

	// Takes an array of the dangers, using the class variables for BAT WUMPUS an
	// HOLE it displays all the dangers in your room
	// It also displays how many questions you must answer, or if its a bat, that
	// you've been moved to a random room
	public void displayDanger(int[] dangers) {
		boolean waiting = false;
		boolean priority = true;

		for (int i = 0; i < dangers.length; i++) {
			if (dangers[i] > 0) {
				waiting = true;
			}
		}

		while (waiting) {

			StdDraw.clear();
			double shift = 0;
			int questions = 0;
			int correct = 0;

			background();

			strip(0.5, 0.5, 0.30, 0.5);
			WARNING.draw("Danger", 0.5, 0.85, 100);

			setActiveColor(30);
			Font title = new Font("Copperplate Gothic Bold", 0, 45);
			StdDraw.setFont(title);

			StdDraw.text(0.5, 0.7, "You are in a room with:");

			for (int i = 0; i < dangers.length; i++) {
				if (dangers[i] > 0) {
					StdDraw.text(0.5, 0.6 - shift, "A " + getDanger(i));
					shift += 0.1;
					if (i == HOLE) {
						questions += 3;
						correct += 2;
					}

					if (i == WUMPUS) {
						questions += 5;
						correct += 3;
					}
				}
			}

			Font danger = new Font("Copperplate Gothic Bold", 0, 25);
			Font wumpus = new Font("Copperplate Gothic Bold", 0, 20);
			StdDraw.setFont(danger);

			if (dangers[BAT] > 0 && dangers[WUMPUS] <= 0) {
				StdDraw.text(0.5, 0.6 - shift, "A bat has moved you to a random room!");
				shift += 0.04;
			}
			if (dangers[BAT] > 0 && dangers[WUMPUS] > 0)// ;
			{
				StdDraw.text(0.5, 0.6 - shift, "Even if you survive the wumpus, ");
				StdDraw.text(0.5, 0.56 - shift, "the bat will move you to a random room!");
				shift += 0.1;
				StdDraw.setFont(danger);
			}
			if (dangers[WUMPUS] > 0 || dangers[HOLE] > 0) {
				StdDraw.text(0.5, 0.6 - shift, "You must answer " + correct + " out of ");
				StdDraw.text(0.5, 0.56 - shift, questions + " questions correctly.");

				if (dangers[WUMPUS] > 0) {
					StdDraw.setFont(wumpus);
					StdDraw.text(0.5, 0.5 - shift, "If you get them correct, the wumpus will run away.");
					shift += 0.03;
				}

				if (dangers[HOLE] > 0) {
					StdDraw.setFont(wumpus);
					StdDraw.text(0.5, 0.5 - shift, "If you survive,");
					shift += 0.03;
					StdDraw.text(0.5, 0.5 - shift, "you will go back to the first room you were in.");
					shift += 0.05;
				}
			}

			waiting = !button(0.5, 0.1, 0.15, 0.055, "Next", priority);

			StdDraw.show();
		}
		Sounds.movePlayer();
	}

	// Prints out which dangers the player escaped, PIT or Wumpus, it can display
	// both.
	public void escapedDanger(int[] dangers) {
		double distance = 0.05;
		double textx = 0.5;
		double texty = 0.6;
		boolean waiting = false;
		boolean priority = true;
		String survivedWumpus = "You got 3 out of 5 questions correct and made the Wumpus run away. You have escaped the WUMPUS... for now.";
		String survivedPit = "You got 2 out of 3 questions correct and you have climbed out the PIT. Hopefully you don't fall into another one, because you won't be so lucky next time.";

		for (int i = 0; i < dangers.length; i++) {
			if (i == BAT) {
				i++;
			}

			if (dangers[i] > 0) {
				waiting = true;
			}
		}

		while (waiting) {

			StdDraw.clear();
			double shift = 0;

			background();

			strip(0.5, 0.5, 0.30, 0.5);

			setTextColor();
			Font title = new Font("Copperplate Gothic Bold", 0, 45);
			StdDraw.setFont(title);
			StdDraw.text(0.5, 0.85, "You Survived");

			Font danger = new Font("Copperplate Gothic Bold", 0, 25);
			StdDraw.setFont(danger);
			setActiveColor(30);

			for (int i = 0; i < dangers.length; i++) {
				if (dangers[i] > 0 && i != BAT) {
					if (i == WUMPUS) {
						displayList(splitUp(survivedWumpus), textx, texty - shift, distance);
					}

					if (i == HOLE) {
						displayList(splitUp(survivedPit), textx, texty - shift, distance);
					}
					shift += 0.30;

				}
			}

			waiting = !button(0.5, 0.1, 0.15, 0.055, "Next", priority);
			if (inBox(0.5, 0.1, 0.15, 0.055)) {
				priority = false;
			}

			StdDraw.show();
		}
		Sounds.movePlayer();
	}

	private static void strip(double x, double y, double wide, double tall) {
		setInactiveColor();
		StdDraw.filledRectangle(x, y, wide, tall);
	}

	// After each trivial question, using the boolean it tells the user if they got
	// the answer correct,
	// using the questions input it says how many questions have been asked till
	// that point
	// and using answered it says how many have been answered correctly
	public static void postTrivia(boolean correct, int questions, int answered) {
		boolean waiting = true;
		boolean priority = true;

		while (waiting) {

			StdDraw.clear();

			background();

			strip(0.5, 0.5, 0.30, 0.5);

			setTextColor();
			Font title = new Font("Copperplate Gothic Bold", 0, 60);
			StdDraw.setFont(title);
			if (correct) {
				StdDraw.text(0.5, 0.6, "Correct");
			}

			else {
				StdDraw.text(0.5, 0.6, "Incorrect");
			}

			Font sub = new Font("Copperplate Gothic Bold", 0, 25);
			StdDraw.setFont(sub);
			StdDraw.text(0.5, 0.5, "So far you answered " + answered + " out of " + questions + " correct");

			waiting = !button(0.5, 0.1, 0.15, 0.055, "Next", priority);
			StdDraw.show();
		}
	}

	// Error screen in case of an error
	public static void Error() {
		boolean waiting = true;
		boolean priority = false;

		while (waiting) {

			StdDraw.clear();

			background();
			strip(0.5, 0.5, 0.30, 0.5);

			setActiveColor(30);
			Font title = new Font("Copperplate Gothic Bold", 0, 60);
			StdDraw.setFont(title);
			StdDraw.text(0.5, 0.6, "ERROR");

			waiting = !button(0.5, 0.1, 0.15, 0.055, "EXIT", priority);
			if (inBox(0.5, 0.1, 0.15, 0.055)) {
				priority = false;
			}
			StdDraw.show();
		}
		java.lang.System.exit(0);
	}

	// Tells the player that they successfully bought two arrays, and using the
	// input displays how many arrows they have
	public static void boughtArrow(int arrows) {
		boolean waiting = true;
		boolean priority = true;

		while (waiting) {

			StdDraw.clear();

			background();

			strip(0.5, 0.5, 0.30, 0.5);

			setTextColor();
			Font title = new Font("Copperplate Gothic Bold", 0, 45);
			StdDraw.setFont(title);
			StdDraw.text(0.5, 0.6, "You bought 2 arrows");
			StdDraw.text(0.5, 0.50, "You have " + arrows + " arrows");

			waiting = !button(0.5, 0.1, 0.15, 0.055, "Next", priority);
			if (inBox(0.5, 0.1, 0.15, 0.055)) {
				priority = false;
			}
			StdDraw.show();
		}
	}

	// Asks the player if they are sure they want to exit, if yes it exits the game,
	// if not it does nothing
	public static void Exit() {
		boolean waiting = true;
		Sounds.stop();
		while (waiting) {

			StdDraw.clear();
			background();

			strip(0.5, 0.5, 0.30, 0.5);

			setTextColor();
			Font title = new Font("Copperplate Gothic Bold", 0, 45);
			StdDraw.setFont(title);
			StdDraw.text(0.5, 0.6, "Are you sure");
			StdDraw.text(0.5, 0.50, "You want to EXIT?");

			if (button(0.5, 0.21, 0.15, 0.055, "YES", true)) {
				java.lang.System.exit(0);
			}

			waiting = !button(0.5, 0.1, 0.15, 0.055, "NO", false);

			StdDraw.show();
		}
		Sounds.movePlayer();
	}

	public void setTheme() {
		boolean waiting = true;
		boolean themeset = scaryTheme;
		while (waiting) {

			StdDraw.clear();
			background();

			strip(0.5, 0.5, 0.30, 0.5);

			setTextColor();
			Font title = new Font("Copperplate Gothic Bold", 0, 60);
			StdDraw.setFont(title);
			StdDraw.text(0.5, 0.6, "THEME");

			waiting = !button(0.5, 0.1, 0.15, 0.055, "Back", false);

			if (button(0.5, 0.32, 0.15, 0.055, "ominous", false)) {
				changeToScary(true);
				scaryTheme = true;
				Sounds.setTheme(1);
				waiting = false;
			}

			if (button(0.5, 0.21, 0.15, 0.055, "weird", false)) {
				changeToScary(false);
				scaryTheme = false;
				Sounds.setTheme(2);
				waiting = false;
			}

			if (inBox(0.5, 0.21, 0.15, 0.055)) {
				changeToScary(false);
			}

			else if (inBox(0.5, 0.32, 0.15, 0.055)) {
				changeToScary(true);
			}

			else {
				changeToScary(themeset);
			}
			// asdf
			StdDraw.show();
		}

		resetAndCreateText();
	}

	// Asks the player if they are sure they want to go to main menu, if yes it
	// return true, if not it returns false
	public static boolean toMainMenu() {
		Sounds.stop();
		double distance = 0.05;
		double textx = 0.5;
		double texty = 0.4;
		String saveMessage = "Your progress will be saved and you will be able to resume";

		while (true) {

			StdDraw.clear();
			background();

			strip(0.5, 0.5, 0.30, 0.5);

			setTextColor();
			Font title = new Font("Copperplate Gothic Bold", 0, 45);
			StdDraw.setFont(title);
			StdDraw.text(0.5, 0.7, "Are you sure");
			StdDraw.text(0.5, 0.64, "You want to go back");
			StdDraw.text(0.5, 0.58, "to the main menu?");

			Font cansave = new Font("Copperplate Gothic Bold", 0, 30);
			StdDraw.setFont(cansave);
			displayList(splitUp(saveMessage), textx, texty, distance);

			if (button(0.5, 0.21, 0.15, 0.055, "YES", true)) {
				return true;
			}

			if (button(0.5, 0.1, 0.15, 0.055, "NO", false)) {
				Sounds.movePlayer();
				return false;
			}
			StdDraw.show();
		}
	}

	// This method displays how to play the game when the players mouse hovers over
	// a button, buttons don't do anything important other
	// than being drawn
	public static void tutorial() {
		double x = 0.2;
		double y = 0.5;
		double containerx = 0.15;
		double containery = 0.5;
		double topButton = 0.75;
		double shift = 0.11;

		// Messages shown to player, they are added instead of one big screen so that
		// they all fit on one screen
		String wumpusTutorial = "It is your objective to kill the WUMPUS. You can kill the WUMPUS by shooting an arrow into the door that leads to the room the WUMPUS is in. ";
		wumpusTutorial += "If you miss, the WUMPUS might run away, and if you are in the same room as the WUMPUS, you must answer three out of five trivia questions correct or die! ";
		wumpusTutorial += "If you get three out of five questions correct, the wumpus will run away. ";
		wumpusTutorial += "You know the WUMPUS is near because you will see the message \"I smell a WUMPUS\"";

		String batTutorial = "Super BATS are huge monsters that fly through the caves, if you are in a room with a BAT you will see the message \"There is a BAT nearby\". ";
		batTutorial += "If you are in the same room as a BAT, you will be carried by the BAT to a random room.";

		String pitTutorial = "Some rooms in the caves contain PITS, if you are in the same room as a pit you will fall in, and must answer 2 out of three trivia questions right to get out, ";
		pitTutorial += "and go back to where you started the game. If you don't get them right you die. If you are near a PIT you will see the message \"I feel a draft\"";

		String arrowTutorial = "ARROWS are your weapons against the WUMPUS. To shoot an ARROW press the \"SHOOT ARROW\" button, then press the red door you want to shoot the ARROW in. ";
		arrowTutorial += "To buy an ARROW you must answer two out of three trivia questions correctly. You die if you run out of ARROWS.";

		String coinTutorial = "You get COINS by going into another room in the cave. Every trivia question asked, from WUMPUS, PITS, or while buying an item costs one coin. ";
		coinTutorial += "You die if you run out of COINS.";

		String navigationTutorial = "The player is represented by a white ball which follows the mouse. In order to navigate rooms lead the player into the red bars representing the doors. ";
		navigationTutorial += "A new round begins every time you enter a new room.";

		double distance = 0.05;
		double textx = 0.67;
		double texty = 0.5;

		while (true) {
			StdDraw.clear();
			background();
			;

			strip(x, y, containerx, containery);

			title(x, 0.9, "How to Play");
			Font font = new Font("Copperplate Gothic Bold", 0, 30);
			StdDraw.setFont(font);

			if (inBox(x, topButton, containerx, 0.055)) {
				displayList(splitUp(wumpusTutorial), textx, texty, distance);
			}

			if (inBox(x, topButton - shift, containerx, 0.055)) {
				displayList(splitUp(batTutorial), textx, texty, distance);
			}

			if (inBox(x, topButton - shift * 2, containerx, 0.055)) {
				displayList(splitUp(pitTutorial), textx, texty, distance);
			}

			if (inBox(x, topButton - shift * 3, containerx, 0.055)) {
				displayList(splitUp(arrowTutorial), textx, texty, distance);
			}

			if (inBox(x, topButton - shift * 4, containerx, 0.055)) {
				displayList(splitUp(coinTutorial), textx, texty, distance);
			}

			if (inBox(x, topButton - shift * 5, containerx, 0.055)) {
				displayList(splitUp(navigationTutorial), textx, texty, distance);
			}

			button(x, topButton, containerx, 0.055, "Wumpus", false);
			button(x, topButton - shift, containerx, 0.055, "Bats", false);
			button(x, topButton - shift * 2, containerx, 0.055, "Pits", false);
			button(x, topButton - shift * 3, containerx, 0.055, "Arrows", false);
			button(x, topButton - shift * 4, containerx, 0.055, "Coins", false);
			button(x, topButton - shift * 5, containerx, 0.055, "Navigation", false);

			if (button(x, topButton - shift * 6, containerx, 0.055, "Back", false)) {
				return;
			}

			StdDraw.show();
		}
	}

	// If the player answers too many questions wrong to buy an item, this method is
	// called, it loops itself
	public static void goof() {
		boolean priority = true;
		boolean waiting = true;

		while (waiting) {

			StdDraw.clear();

			background();

			strip(0.5, 0.5, 0.30, 0.5);

			setTextColor();
			Font title = new Font("Copperplate Gothic Bold", 0, 60);
			StdDraw.setFont(title);
			StdDraw.text(0.5, 0.6, "Too many wrong");
			StdDraw.text(0.5, 0.50, "to buy item.");

			waiting = !button(0.5, 0.1, 0.15, 0.055, "Next", priority);
			if (inBox(0.5, 0.1, 0.15, 0.055)) {
				priority = false;
			}
			StdDraw.show();
		}
	}

	// displays the multiple choice questions for trivia, and returns a char a,b,c,d
	// of what was answered
	public static char getAnswer(String question, String questionA, String questionB, String questionC,
			String questionD) {
		// yeet
		double x = 0.27;
		double y = 0.5;
		double containerx = 0.27;
		double containery = 0.5;

		ArrayList<String> toprint = new ArrayList<String>();
		toprint = splitUp(question);

		while (true) {
			StdDraw.clear();
			background();

			strip(x, y, containerx, containery);

			title(x, 0.9, "Trivia");

			Font font = new Font("Copperplate Gothic Bold", 0, 25);
			double start = ((toprint.size() * 0.05) / 2) + 0.5;
			double textcenter = 0.77;

			for (int i = 0; i < toprint.size(); i++) {
				setTextColor();
				StdDraw.setFont(font);
				StdDraw.text(textcenter, start - (0.05 * i), toprint.get(i));
			}

			if (button(x, 0.65, containerx, 0.055, questionA, false)) {
				return 'a';
			}

			if (button(x, 0.54, containerx, 0.055, questionB, false)) {
				return 'b';
			}

			if (button(x, 0.43, containerx, 0.055, questionC, false)) {
				return 'c';
			}

			if (button(x, 0.32, containerx, 0.055, questionD, false)) {
				return 'd';
			}

			StdDraw.show();
		}

	}

	// Screen to buy an item 1 for arrow, 2 for secret, 0 to go back
	public static int buyItem(boolean enough) {
		double x = 0.2;
		double y = 0.5;
		double containerx = 0.15;
		double containery = 0.5;

		ArrayList<String> secretDescription = new ArrayList<String>();
		ArrayList<String> arrowDescription = new ArrayList<String>();

		secretDescription = splitUp(
				"You can get a SECRET by getting two out of three trivia questions right. SECRETS can vary from very useful to useless.");
		arrowDescription = splitUp(
				"You can get ARROWS by getting two out of three trivia questions right, these are your weapons against the WUMPUS.");

		while (true) {
			StdDraw.clear();
			background();

			strip(x, y, containerx, containery);

			title(x, 0.9, "Buy Items");

			if (!enough) {
				Font funds = new Font("Copperplate Gothic Bold", 0, 25);
				StdDraw.setFont(funds);
				setActiveColor();
				StdDraw.text(x, 0.85, "NOT ENOUGH COINS");
			}

			if (inBox(x, 0.65, containerx, 0.055)) {
				Font font = new Font("Copperplate Gothic Bold", 0, 30);
				double start = ((arrowDescription.size() * 0.05) / 2) + 0.5;
				double textcenter = 0.7;

				for (int i = 0; i < arrowDescription.size(); i++) {
					setTextColor();
					StdDraw.setFont(font);
					StdDraw.text(textcenter, start - (0.05 * i), arrowDescription.get(i));
				}
			}

			if (inBox(x, 0.54, containerx, 0.055)) {
				Font font = new Font("Copperplate Gothic Bold", 0, 30);
				double start = ((secretDescription.size() * 0.05) / 2) + 0.5;
				double textcenter = 0.7;

				for (int i = 0; i < secretDescription.size(); i++) {
					setTextColor();
					StdDraw.setFont(font);
					StdDraw.text(textcenter, start - (0.05 * i), secretDescription.get(i));
				}
			}
			// yeet
			if (button(x, 0.65, containerx, 0.055, "Arrow", false) && enough) {
				return 1;
			}

			if (button(x, 0.54, containerx, 0.055, "Secret", false) && enough) {
				return 2;
			}

			if (button(x, 0.43, containerx, 0.055, "Back", false)) {
				return 0;
			}

			StdDraw.show();
		}

	}

	// Splits a string into an array list with each part only being 30 characters
	// long, it splits by sentences
	// this is used to display sentences that need t be shown on multiple rows, used
	// with displayList
	private static ArrayList<String> splitUp(String split) {
		ArrayList<String> splitarray = new ArrayList<String>();
		String temp = split;
		int masterlength = 30;
		int length = masterlength;
		// System.out.println(temp);

		while (temp.length() > masterlength) {
			length = masterlength;
			// System.out.println("test");
			while (temp.charAt(length) != ' ' && length >= 0) {
				// System.out.println("Toooost");
				length--;
			}
			// System.out.println(splitarray);
			splitarray.add(temp.substring(0, length + 1));
			temp = temp.substring(length + 1);
		}

		if (temp.length() != 0) {
			splitarray.add(temp);
		}

		return splitarray;

	}

	// Returns true if the string has a character in it
	private static boolean hasChar(String name) {

		for (int i = 0; i < name.length(); i++) {
			if (!name.substring(i, i + 1).equals(" ")) {
				return true;
			}
		}

		return false;
	}

	// Takes a string of what caused you to die, or if you one, score, and if you
	// got on the leaderboard, this loops by itself and has a next button
	public static void endGame(String reason, int score, boolean leaderboard) {
		boolean waiting = true;

		String wumpus = "You answered 3 out of 5 incorrect and were killed by the Wumpus.";
		String pit = "You answered 2 out of 3 incorrect and are stuck in a pit forever, where you die.";
		String coins = "\"You died because you're poor, even though you're rich in your heart\" - Mehar Gulati";
		String arrows = "You ran out of arrows and died because you had no way to kill the wumpus.";
		String highscore = "Your score was good enough to make it on the leaderboard! Check it out in the main menu";
		double distance = 0.05;
		boolean priority = true;

		while (waiting) {

			StdDraw.clear();

			background();

			strip(0.5, 0.5, 0.30, 0.5);

			setTextColor();
			Font font = new Font("Copperplate Gothic Bold", 0, 60);
			StdDraw.setFont(font);
			StdDraw.text(0.5, 0.85, "GAME OVER");

			setTextColor();
			Font title = new Font("Copperplate Gothic Bold", 0, 30);
			StdDraw.setFont(title);
			if (reason.equals("won")) {
				Font win = new Font("Copperplate Gothic Bold", 0, 50);
				StdDraw.setFont(win);
				StdDraw.text(0.5, 0.6, "You have slain");
				StdDraw.text(0.5, 0.53, "the wumpus");
				StdDraw.text(0.5, 0.47, "you won");
			}

			else {
				StdDraw.text(0.5, 0.75, "You have lost");

				if (reason.equals("wumpus")) {
					displayList(splitUp(wumpus), 0.5, 0.55, distance);

				} else if (reason.equals("pits")) {
					displayList(splitUp(pit), 0.5, 0.55, distance);

				} else if (reason.equals("coins")) {
					displayList(splitUp(coins), 0.5, 0.55, distance);

				} else if (reason.equals("arrows")) {
					displayList(splitUp(arrows), 0.5, 0.55, distance);

				}
			}

			setTextColor();
			Font highscorefont = new Font("Copperplate Gothic Bold", 0, 30);
			StdDraw.setFont(highscorefont);
			StdDraw.text(0.5, 0.7, "Your score: " + score);
			if (leaderboard) {
				displayList(splitUp(highscore), 0.5, 0.3, distance);
			}

			waiting = !button(0.5, 0.1, 0.15, 0.055, "Next", priority);
			if (inBox(0.5, 0.1, 0.15, 0.05)) {
				priority = false;
			}
			StdDraw.show();
		}
	}

	// This method takes a boolean on if the player hit the wumpus and the arrows
	// left, and displays them, this loops by itself and has a next button
	public static void arrowHit(boolean hit, int arrows) {
		boolean waiting = true;
		boolean priority = true;

		while (waiting) {

			StdDraw.clear();

			background();

			strip(0.5, 0.5, 0.30, 0.5);

			setTextColor();
			Font title = new Font("Copperplate Gothic Bold", 0, 30);
			StdDraw.setFont(title);

			if (hit) {
				StdDraw.text(0.5, 0.6, "You hit the Wumpus");
			}

			else {
				StdDraw.text(0.5, 0.6, "You missed");
				StdDraw.text(0.5, 0.55, "You have " + arrows + " arrows left");
			}

			waiting = !button(0.5, 0.1, 0.15, 0.055, "Next", priority);
			if (inBox(0.5, 0.1, 0.15, 0.055)) {
				priority = false;
			}
			StdDraw.show();
		}
	}

	// Method displays the team message saying thanks for playing, with the team
	// names, loops on its own and has a next button
	public static void teamMessage() {
		double x = 0.2;
		double y = 0.5;
		double containerx = 0.15;
		double containery = 0.5;
		double topButton = 0.75;
		double shift = 0.11;

		// Messages shown to player, they are added instead of one big screen so that
		// they all fit on one screen
		String Daniel = "Graphical Interface";

		String Saihaj = "Player, Game Control, and Project Manager";

		String Josh = "Game Control and Project Manager";

		String Brian = "Trivia and High Scores";

		String Raj = "Game Locations and Sound";

		String Hans = "Cave Maps and Code";

		String teammessage = "Thanks for playing Tesla Stem's Artesian Code team Hunt the Wumpus Game!";

		double distance = 0.05;
		double textx = 0.67;
		double texty = 0.5;

		while (true) {
			StdDraw.clear();
			background();
			boolean noButton = true;

			strip(x, y, containerx, containery);

			title(x, 0.9, "CREDITS");
			Font font = new Font("Copperplate Gothic Bold", 0, 30);
			StdDraw.setFont(font);

			if (inBox(x, topButton, containerx, 0.055)) {
				displayList(splitUp(Daniel), textx, texty, distance);
				noButton = false;
			}

			if (inBox(x, topButton - shift, containerx, 0.055)) {
				displayList(splitUp(Saihaj), textx, texty, distance);
				noButton = false;
			}

			if (inBox(x, topButton - shift * 2, containerx, 0.055)) {
				displayList(splitUp(Josh), textx, texty, distance);
				noButton = false;
			}

			if (inBox(x, topButton - shift * 3, containerx, 0.055)) {
				displayList(splitUp(Brian), textx, texty, distance);
				noButton = false;
			}

			if (inBox(x, topButton - shift * 4, containerx, 0.055)) {
				displayList(splitUp(Raj), textx, texty, distance);
				noButton = false;
			}

			if (inBox(x, topButton - shift * 5, containerx, 0.055)) {
				displayList(splitUp(Hans), textx, texty, distance);
				noButton = false;
			}

			if (noButton) {
				displayList(splitUp(teammessage), textx, texty, distance);
			}

			button(x, topButton, containerx, 0.055, "DANIEL POPA", false);
			button(x, topButton - shift, containerx, 0.055, "Saihaj Gulati", false);
			button(x, topButton - shift * 2, containerx, 0.055, "Joshua Venable", false);
			button(x, topButton - shift * 3, containerx, 0.055, "Brian Yang", false);
			button(x, topButton - shift * 4, containerx, 0.055, "Raj Sunku", false);
			button(x, topButton - shift * 5, containerx, 0.055, "Hans Koduri", false);

			if (button(x, topButton - shift * 6, containerx, 0.055, "Back", false)) {
				return;
			}

			StdDraw.show();
		}
	}

	// Takes a list and displays it centered on the parameters, each new string in
	// the array is a new line
	private static void displayList(ArrayList<String> messagearray, double textcenterx, double textcentery,
			double distance) {
		double start = ((messagearray.size() * distance) / 2) + textcentery;

		for (int i = 0; i < messagearray.size(); i++) {
			setTextColor();
			StdDraw.text(textcenterx, start - (distance * i), messagearray.get(i));
		}
	}

	// Displays small text shown at the side of the screen while playing the game
	private static void HUDtext(double x, double y, String text) {
		Font hudfont = new Font("Copperplate Gothic Bold", 0, 20);
		setTextColor();
		StdDraw.setFont(hudfont);
		StdDraw.text(x, y, text);

	}

}