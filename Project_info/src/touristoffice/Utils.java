package touristoffice;

import java.util.Set;

import javax.swing.JTextField;

public class Utils {
  private static void error(String msg) {
	  throw new IllegalArgumentException("Fehler: "+msg+"!");
  }
  
  public static void checkStringNotEmpty(String s, String msg) {
	  if (s == null || s.isEmpty())
		  error(msg);
  }
  
  public static void checkIntPositive(int i, String msg) {
	  if (i<=0)
		  error(msg);
  }
  
  public static void checkUniqueName(String name, Set<String> allNames, String msg) {
	  if (allNames.contains(name))
		  error(msg);
  }
  
  public static int readInteger(JTextField txt, String msg) {
	  try {
			return Integer.valueOf(txt.getText());
	  }
	  catch (NumberFormatException ex) {
			error(msg);
	  }
	  return 0; //cannot reach this line
  }
}
