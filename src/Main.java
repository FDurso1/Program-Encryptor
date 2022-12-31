import java.util.ArrayList;
import java.util.*;

public class Main {

  public static boolean tmp;
  public static boolean shave;
  public static boolean printing;


  public static ArrayList<String> getVariables(ArrayList<String> input) {
    boolean tmp = false;
    int varCount = 0;
    ArrayList<String> output = new ArrayList<>();
    HashMap<String, String> varMap = new HashMap<>();

    for (String in : input) {
      if (tmp || varMap.containsKey(in)) {
        if (!varMap.containsKey(in)) {
          varMap.put(in, Character.toString((char) ('A' + varCount++)));
        }
        output.add(varMap.get(in));
        tmp = false;
      } else {
        output.add(in);
        if (in.equals("int") || in.equals("double") || in.equals("String") || in.equals("boolean")) {
          tmp = true;
        }
      }
    }
    return output;
  }

  public static void steralizeHelper(StringBuffer corrected, String in) {
    if (tmp) {
      try {
        String varName = in.substring(0, in.indexOf('='));
        String value = in.substring(in.indexOf('=') +1);
        corrected.append(varName).append(" = ").append(value);
        tmp = false;
        return;
      } catch (StringIndexOutOfBoundsException ex) {
        if (in.endsWith(";")) {
          corrected.append(in, 0, in.indexOf(";")).append(" ").append(";");
        } else {
          corrected.append(in).append(" ");
        }
        tmp = false;
        return;
      }
    } else if (shave) {
      in = in.substring(1);
      shave = false;
    } else if (printing) {
      if (in.contains("+")) {
        String word = in.substring(0, in.indexOf('+'));
        corrected.append(word).append(" + ");
        steralizeHelper(corrected, in.substring(in.indexOf('+')+1));
      } else {
        String leftOver = in.substring(0, in.indexOf(')')); //TODO: in will not contain the necessary ) for this to work
        corrected.append(leftOver).append(" ); ");
        printing = false;
      }
      return;
    }
    if (in.equals("int") || in.equals("double") || in.equals("String") || in.equals("boolean")) {
      tmp = true;
    }

    if (in.charAt(0) == '=' && in.length() > 1) {
      corrected.append(" = ").append(in.substring(1));
    } else if (in.equals("for")) {
      corrected.append("for( ");
      shave = true;
    } else if (in.length() > 2 && (in.startsWith("++", 1) || in.startsWith("--", 1))) {
      corrected.append(in.charAt(0)).append(" ").append(in.substring(1));
    } else if (in.startsWith("System.out.print")) {
      String inner = in.substring(in.indexOf('(')+1);
      corrected.append(in, 0, in.indexOf('(')+1).append(" ");
      printing = true;
      steralizeHelper(corrected, inner);
    }
    else {
      corrected.append(" ").append(in).append(" ");
    }
  }

  public static String steralizeInput(String input) {
    Scanner strIn = new Scanner(input);
    StringBuffer corrected = new StringBuffer();
    while (strIn.hasNext()) {
      String in = strIn.next();
      steralizeHelper(corrected, in);

    }
    return corrected.toString();

  }

  //Will reduce all white space inside of quotes to 0
  public static void main (String[] args) {
    double mag=1.0;
    String string ="hello";
    boolean bool;
    bool= true;
    int temp = 1;
    temp += 3;
    for( int i = 4; i < 7; i ++ ) {
      System.out.print(i+" "+temp+" hi ");
    }
    System.out.println(mag);
    System.out.println(string);
    System.out.println(bool);

    ArrayList<String> list = new ArrayList<>();
    String str = steralizeInput("double mag=1.0;\n" +
            "    String string =\"hello\";\n" +
            "    boolean bool;\n" +
            "    bool= true;\n" +
            "    int temp = 1;\n" +
            "    temp += 3;\n" +
            "    for( int i = 4; i < 7; i ++ ) {\n" +
            "      System.out.print(i+\" \"+temp+\" hi \");\n" +
            "    }\n" +
            "    System.out.println(mag);\n" +
            "    System.out.println(string);\n" +
            "    System.out.println(bool);");
    Scanner strIn = new Scanner(str);
    while (strIn.hasNext()) {
      list.add(strIn.next());
    }

    ArrayList<String> output = getVariables(list);
    System.out.println();
    for (String out : output) {
      System.out.print(out + " ");
      if (out.endsWith(";") || out.endsWith("}")) {
        System.out.println();
      }
    }

    System.out.println("And now the results");
    System.out.println();



  }
}
