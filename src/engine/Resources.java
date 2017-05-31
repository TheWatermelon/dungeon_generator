package engine;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class Resources {
	private static Resources res;
	
	private static String[] playerPrefix = { "Anu", "Ara", "Ash", "Cath", "Cer", "Chi", "Dio", "Esh", "Fau", "Fer", "Flo", "Goth", "Ha", "Heph", "Her", "Hog", "Io", "Iri", "Jan", "Jun", "Kya", "Kri", "Kuv", "Lak", "Log", "Lok", "Let", "Mith", "Mor", "Ogu", "Par", "Pah", "Rash", "Ram", "Shak", "Sham", "Sed", "Tar", "Ten", "Ven", "Vic", "Vul", "Wel", "Wot", "Yam", "Yog" };
	private static String[] playerSuffix = { "asu", "bis", "bog", "can", "cha", "chen", "dan", "dir", "dum", "duk", "gard", "gorn", "gun", "gus", "han", "hen", "kar", "ken", "ki", "lord", "man", "mer", "mon", "mora", "nar", "no", "num", "nus", "pai", "pala", "phyn", "pheus", "qi", "qun", "ros", "rion", "sha", "shi", "sta", "thor", "tri", "ten", "van", "uru", "zyr" };
		
	private static String[] monsterName = { "alphyn",	"bunny",	"chima",	"dandan",	"elemal",	"fachen",	"goblin",	"hog",		"imp",		"jest",		"koalad",	"linum",	"mummy",	"nyan",		"olos", 	"pata", 	"qi", 	"rat", 		"snake", "troll", "uxie", 	"vermillion", 	"wasp", 	"xylopod", 	"yolocamph", 	"zilly" };
	private static String[] monsterName1= { "Alpha", 	"Bunner", 	"Chimera", 	"Daran", 	"Elemental","Fraken", 	"Goblord", 	"Hedgard", 	"Imperos", 	"Jester", 	"Koamad", 	"Lordum", 	"Mastras", 	"Ninja", 	"Olopus", 	"Partara", 	"Quim", "Routard", 	"Snake", "Troll", "Uximer", "Vizir", 		"Werewolf", "Xinus", 	"Yopor", 		"Zebra" };
	
	private static String[] equipementName = { "void", "Wooden", "Copper", "Iron", "Silver", "Master" };
	
	private static String[] helmetName = { "void", "Childish", "Beginner", "Apprentice", "Expert", "Master" };
	private static String[] helmetType = { "Cap", "Hat", "Helmet" };
	
	public static Color white = Color.WHITE;
	public static Color yellow = Color.YELLOW;
	public static Color darkYellow = new Color(0xA9, 0x96, 0x2D);
	public static Color darkerYellow = new Color(0xA9, 0x96, 0x2D, 126);
	public static Color lightOrange = new Color(0xFF, 0x66, 0x00);
	public static Color orange = Color.ORANGE;
	public static Color brown = new Color(0xA5, 0x68, 0x2A);
	public static Color darkBrown = new Color(0x45, 0x20, 0x00);
	public static Color lightGray = Color.GRAY;
	public static Color gray = new Color(0x60, 0x60, 0x60);
	public static Color darkGray = new Color(0x30, 0x30, 0x30);
	public static Color darkerGray = new Color(0x15, 0x15, 0x15);
	public static Color green = new Color(0x00, 0x80, 0x00);
	public static Color darkGreen = new Color(0x00, 0x33, 0x00);
	public static Color coolRed = new Color(0xEF, 0x3F, 0x23);
	public static Color red = Color.RED;
	public static Color darkRed = new Color(0x7F, 0x00, 0x00);
	public static Color blue = Color.BLUE;
	public static Color darkBlue = new Color(0x00, 0x00, 0x7F);
	public static Color cyan = Color.CYAN;
	public static Color magenta = Color.MAGENTA;
	public static Color pink = Color.PINK;
	
	public Color theme = lightGray;
	
	public int difficulty=1;
	
	public int resolution=30;
	
	public boolean commandsHelp = true;
	
	public enum Commands {
		Up('z'),
		Right('d'),
		Down('s'),
		Left('q'),
		Take('f'),
		Inventory('i'),
		QuickAction1('a'),
		QuickAction2('e'),
		Pause('p'),
		Restart('r');
		
		private Commands(char s) { key = s; }
		
		public char getKey() { return key; }
		public void setKey(char k) { key = k; }
		
		private char key;
	};
	
	private Resources() {
		
	}
	
	public static Resources getInstance() {
		if(res==null) {
			res = new Resources();
		}
		return res;
	}
	
	public static String generatePlayerName() {
		Random rnd = new Random();
		return playerPrefix[rnd.nextInt(playerPrefix.length)]+
				playerSuffix[rnd.nextInt(playerSuffix.length)];
	}
	
	public static char getLetter() {
		Random rnd = new Random();
		return monsterName[rnd.nextInt(monsterName.length)].charAt(0);
	}
	
	public static char getLetterAt(int index) {
		return monsterName[index].charAt(0);
	}
	
	public static char getCapitalLetterAt(int index) {
		return monsterName1[index].charAt(0);
	}
	
	public static String getName() {
		Random rnd = new Random();
		return monsterName[rnd.nextInt(monsterName.length)];
	}
	
	public static String getNameAt(int index) {
		return monsterName[index];
	}
	
	public static String getCapitalNameAt(int index) {
		return monsterName1[index];
	}
	
	public static String getEquipementAt(int index) {
		return equipementName[index];
	}
	
	public static String getHelmetNameAt(int index) {
		return helmetName[index];
	}
	
	public static String getHelmetType() {
		return helmetType[(new Random()).nextInt(helmetType.length)];
	}
	
	public static int[][] drawCircle(int size) {
		int[][] matrix = new int[size][size];

		double midPoint=(matrix.length-1)/2.0;
		
		for(int j=0; j<matrix.length; j++) {
			int[] row = new int[matrix.length];
			double yy = j - midPoint;
			for(int i=0; i<row.length; i++) {
				double xx = i - midPoint;
				if(Math.sqrt(xx*xx+yy*yy)<=midPoint) {
					row[i]=1;
				}
			}
			matrix[j]=row;
		}
		
		return matrix;
	}
	
	public static int[][] drawLine(int x1, int y1, int x2, int y2, int octant) {
		int dx = x2 - x1;
		int dy = y2 - y1;
		int[][] matrix;
		if(octant==1 || octant==2 || octant==5 || octant==6) {
			matrix = new int[dx*2+1][dy*2+1];
		} else {
			matrix = new int[dy*2+1][dx*2+1];
		}
		
		int D = 2*dy - dx;
		int y = y1;
		
		for(int x=x1; x<=x2; x++) {
			Point p = switchFromOctantZeroTo(octant, x, y);
			if(octant==1 || octant==2 || octant==5 || octant==6) {
				matrix[p.y+dx][p.x+dy]=1;
			} else {
				matrix[p.y+dy][p.x+dx]=1;
			}
			if(D>0) {
				y++;
				D-=dx;
			}
			D+=dy;
		}
		
		return matrix;
	}
	
	public static Point switchFromOctantZeroTo(int octant, int x, int y) {
		switch(octant) {
			case 0:
				return new Point(x, y);
			case 1:
				return new Point(y, x);
			case 2:
				return new Point(-y, x);
			case 3:
				return new Point(-x, y);
			case 4:
				return new Point(-x, -y);
			case 5:
				return new Point(-y, -x);
			case 6:
				return new Point(y, -x);
			case 7:
				return new Point(x, -y);
			default:
				return new Point(x, y);
		}
	}
	
	public static Point switchToOctantZeroFrom(int octant, int x, int y) {
		switch(octant) {
		case 0:
			return new Point(x, y);
		case 1:
			return new Point(y, x);
		case 2:
			return new Point(y, -x);
		case 3:
			return new Point(-x, y);
		case 4:
			return new Point(-x, -y);
		case 5:
			return new Point(-y, -x);
		case 6:
			return new Point(-y, x);
		case 7:
			return new Point(x, -y);
		default:
			return new Point(x, y);
	}
}
}
