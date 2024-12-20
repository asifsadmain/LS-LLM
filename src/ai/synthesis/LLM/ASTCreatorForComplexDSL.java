package ai.synthesis.LLM;

/**
 *
 * @author quazi
 */


import ai.synthesis.ComplexDSL.LS_Actions.*;
import ai.synthesis.ComplexDSL.LS_CFG.*;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.*;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG_Condition.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Command2 {
    String command;

    Command2(String command) {
        this.command = command.trim();
    }

    @Override
    public String toString() {
        return command;
    }
}

class Segment2 {
    String name;

    S_LS s;
    B_LS b;
    List<Object> segmentContent = new ArrayList<>();

    Segment2(String name) {
        this.name = name;
    }

    public void add(Object obj) {
        segmentContent.add(obj);
    }
}

public class ASTCreatorForComplexDSL {
  public static void main(String[] args) {
    String expr = "for(Unit u){\n" +
      "  u.attack(Closest)\n" +
      "  for(Unit u){\n" +
      "    for(Unit u){\n" +
      "      u.idle()\n" +
      "      u.harvest(6)\n" +
      "    }\n" +
      "   u.train(Worker,Left,25)\n" +
      "   u.moveToUnit(Ally,Closest)\n" +
      "  }\n" +
      "  for(Unit u){\n" +
      "   u.moveToUnit(Enemy,Weakest)\n" +
      "   u.train(Worker,Left,25)\n" +
      "  }\n" +
      "}";

    String expr2 = "for(Unit u) {\n" +
      "  if(u.canHarvest()) {\n" +
      "    u.harvest(2)\n" +
      "  }\n" +
      "  else {\n" +
      "    u.attack(Closest)\n" +
      "  }\n" +
      "}";

    String expr3 = "for(Unit u){\n" +
      "  if(u.CanAttack) then {\n" +
      "    u.attack(Closest)\n" +
      "  }\n" +
      "  if(u.HasNumberOfWorkersHarvesting(0)) then {\n" +
      "// Test comment\n" +
      "    u.harvest(5)\n" +
      "  }\n" +
      "  if(u.HasNumberOfWorkersHarvesting(1)) then {\n" +
      "    u.build(Barracks, EnemyDir, 6)\n" +
      "  }\n" +
      "  if(u.HasNumberOfWorkersHarvesting(2)) then {\n" +
      "    u.train(Light, Left, 8)\n" +
      "  }\n" +
      "  if(u.HasNumberOfWorkersHarvesting(3)) then {\n" +
      "    u.train(Heavy, Up, 5)\n" +
      "  }\n" +
      "  if(u.HasNumberOfWorkersHarvesting(4)) then {\n" +
      "    u.train(Ranged, EnemyDir, 10)\n" +
      "  }\n" +
      "  if(u.HasNumberOfWorkersHarvesting(5)) then {\n" +
      "    u.train(Ranged, EnemyDir, 50)\n" +
      "  }\n" +
      "  if(u.HasNumberOfWorkersHarvesting(6)) then {\n" +
      "    u.train(Ranged, EnemyDir, 100)\n" +
      "  }\n" +
      "  if(u.HasNumberOfWorkersHarvesting(7)) then {\n" +
      "    u.train(Ranged, EnemyDir, 8)\n" +
      "  }\n" +
      "  if(u.HasNumberOfWorkersHarvesting(8)) then {\n" +
      "    u.train(Ranged, EnemyDir, 25)\n" +
      "  }\n" +
      "}";

    String expr4 = "for(Unit u){\n" +
      "    u.harvest(1)\n" +
      "    u.train(Worker, EnemyDir, 8)\n" +
      "    if(u.isBuilder()) then {\n" +
      "            u.train(Ranged, EnemyDir, 7)\n" +
      "        } else if (u.canHarvest()) then {\n" +
      "            u.attack(LessHealthy)\n" +
      "        } else if (u.canAttack()) then {\n" +
      "            u.attack(Closest)\n" +
      "        }\n" +
      "}\n" +
      "\n" +
      "for(Unit u){\n" +
      "    u.build(Barracks, EnemyDir, 6)\n" +
      "}\n" +
      "\n" +
      "for(Unit u){\n" +
      "    u.moveToUnit(Enemy, LessHealthy)\n" +
      "    u.moveToUnit(Enemy, Farthest)\n" +
      "    for(Unit u){\n" +
      "        u.harvest(50)\n" +
      "        for(Unit u){\n" +
      "            u.train(Light, Left, 6)\n" +
      "        }\n" +
      "        for(Unit u){\n" +
      "            u.idle()\n" +
      "        }\n" +
      "    }\n" +
      "}";

    String expr5 = "for(Unit u){\n" +
      "  if(u.canHarvest()) then {\n" +
      "    u.attack(Closest)\n" +
      "    if(u.opponentHasNumberOfUnits(Ranged, 5)) then {\n" +
      "      u.build(Ranged, Up, 6)\n" +
      "    }\n" +
      "  }\n" +
      "  for(Unit u){\n" +
      "    for(Unit u){\n" +
      "      u.idle()\n" +
      "      u.harvest(6)\n" +
      "    }\n" +
      "   u.train(Worker,Left,25)\n" +
      "   u.moveToUnit(Ally,Closest)\n" +
      "  }\n" +
      "}";

//    S_LS AST1 = createAST(expr);
//    System.out.println(AST1.translateIndentation(2));
//    System.out.println("####################################");
//    S_LS AST2 = createAST(expr2);
//    System.out.println(AST2.translateIndentation(2));
    System.out.println("####################################");
    S_LS AST3 = createAST(expr);
    System.out.println(AST3.translateIndentation(1));
    System.out.println("####################################");
//    S_LS AST4 = createAST(expr4);
//    System.out.println(AST4.translateIndentation(2));
//    System.out.println("####################################");
//    S_LS AST5 = createAST(expr5);
//    System.out.println(AST5.translateIndentation(2));
  }

    public static List<String> extractContentInBrackets(String str, boolean shouldMap) {
        // Find the starting and ending indices of the brackets
        int start = str.indexOf('(');
        int end = str.lastIndexOf(')');

        // If there are no brackets or they are not properly paired, return an empty list
        if (start == -1 || end == -1 || end < start) {
            return Collections.emptyList();
        }

        // Extract the string inside the brackets
        String insideBrackets = str.substring(start + 1, end);

        // Split the string by comma and add each part to the list
        String[] parts = insideBrackets.split(",\\s*");

        HashMap<String, String> dslMap = new HashMap<String, String>();

        dslMap.put("t1", "Base");
        dslMap.put("t2", "Barracks");
        dslMap.put("t3", "Ranged");
        dslMap.put("t4", "Heavy");
        dslMap.put("t5", "Light");
        dslMap.put("t6", "Worker");

        dslMap.put("d1", "EnemyDir");
        dslMap.put("d2", "Up");
        dslMap.put("d3", "Down");
        dslMap.put("d4", "Right");
        dslMap.put("d5", "Left");

        dslMap.put("op1", "Strongest");
        dslMap.put("op2", "Weakest");
        dslMap.put("op3", "Closest");
        dslMap.put("op4", "Farthest");
        dslMap.put("op5", "LessHealthy");
        dslMap.put("op6", "MostHealthy");

        dslMap.put("tp1", "Ally");
        dslMap.put("tp2", "Enemy");

        if (shouldMap) {
            for (int i = 0; i < parts.length; i++) {
                if (dslMap.containsKey(parts[i])) {
//                    System.out.println(parts[i] + "=>" + dslMap.get(parts[i]));
                    parts[i] = dslMap.get(parts[i]);
                }
            }
        }

        return Arrays.asList(parts);
    }

    public static B_LS createBooleanNode(String condition) {
        List<String> parameters = extractContentInBrackets(condition, true);        // True if encrypted DSL
        B_LS b = new B_LS();

        if (condition.startsWith("hasNumberOfUnits") || condition.startsWith("b1")) {
            b = new B_LS(new HasNumberOfUnits(new Type(parameters.get(0)), new N(parameters.get(1))));
        } else if (condition.startsWith("opponentHasNumberOfUnits") || condition.startsWith("b2")) {
            b = new B_LS(new OpponentHasNumberOfUnits(new Type(parameters.get(0)), new N(parameters.get(1))));
        } else if (condition.startsWith("hasLessNumberOfUnits") || condition.startsWith("b3")) {
            b = new B_LS(new HasLessNumberOfUnits(new Type(parameters.get(0)), new N(parameters.get(1))));
        } else if (condition.startsWith("haveQtdUnitsAttacking") || condition.startsWith("b4")) {
            b = new B_LS(new HaveQtdUnitsAttacking(new N(parameters.get(0))));
        } else if (condition.startsWith("hasUnitWithinDistanceFromOpponent") || condition.startsWith("b5")) {
            b = new B_LS(new HasUnitWithinDistanceFromOpponent(new N(parameters.get(0))));
        } else if (condition.startsWith("hasNumberOfWorkersHarvesting") || condition.startsWith("b6")) {
            b = new B_LS(new HasNumberOfWorkersHarvesting(new N(parameters.get(0))));
        } else if (condition.startsWith("isBuilder") || condition.startsWith("b8")) {
            b = new B_LS(new Is_Builder());
        } else if (condition.startsWith("is") || condition.startsWith("b7")) {
            b = new B_LS(new is_Type(new Type(parameters.get(0))));
        } else if (condition.startsWith("canAttack") || condition.startsWith("b9")) {
            b = new B_LS(new CanAttack());
        } else if (condition.startsWith("hasUnitThatKillsInOneAttack") || condition.startsWith("b10")) {
            b = new B_LS(new HasUnitThatKillsInOneAttack());
        } else if (condition.startsWith("opponentHasUnitThatKillsUnitInOneAttack") || condition.startsWith("b11")) {
            b = new B_LS(new OpponentHasUnitThatKillsUnitInOneAttack());
        } else if (condition.startsWith("hasUnitInOpponentRange") || condition.startsWith("b12")) {
            b = new B_LS(new HasUnitInOpponentRange());
        } else if (condition.startsWith("opponentHasUnitInPlayerRange") || condition.startsWith("b13")) {
            b = new B_LS(new OpponentHasUnitInPlayerRange());
        } else if (condition.startsWith("canHarvest") || condition.startsWith("b14")) {
            b = new B_LS(new CanHarvest());
        } else {
            return null;
        }

        return b;
    }

    public static S_LS createCommandNode(String command) {
        List<String> parameters = extractContentInBrackets(command, true);  // true if encrypted DSL
        S_LS s = new S_LS();

        if (command.startsWith("u.build") || command.startsWith("u.c1")) {
            s = new S_LS(new C_LS(new Build_LS(new Type(parameters.get(0)), new Direction(parameters.get(1)), new N(parameters.get(2)))));
        } else if (command.startsWith("u.train") || command.startsWith("u.c2")) {
            s = new S_LS(new C_LS(new Train_LS(new Type(parameters.get(0)), new Direction(parameters.get(1)), new N(parameters.get(2)))));
        } else if (command.startsWith("u.moveToUnit") || command.startsWith("u.c3")) {
            s = new S_LS(new C_LS(new moveToUnit_LS(new TargetPlayer(parameters.get(0)), new OpponentPolicy(parameters.get(1)))));
        } else if (command.startsWith("u.attack") || command.startsWith("u.c4")) {
            s = new S_LS(new C_LS(new Attack_LS(new OpponentPolicy(parameters.get(0)))));
        } else if (command.startsWith("u.harvest") || command.startsWith("u.c5")) {
            s = new S_LS(new C_LS(new Harvest_LS(new N(parameters.get(0)))));
        } else if (command.startsWith("u.attackIfInRange")  || command.startsWith("u.idle") || command.startsWith("u.c6")) {
            s = new S_LS(new C_LS(new Idle_LS()));
        } else if (command.startsWith("u.moveAway") || command.startsWith("u.c7")) {
            s = new S_LS(new C_LS(new MoveAway_LS()));
        } else if (command.startsWith("empty")) {
            s = new S_LS(new Empty_LS());
        } else {
            return null;
        }

        return s;
    }

    public static void iterateSegments(Segment2 segment) {
        // create a new List to hold the commands in the current segment
        List<String> commands = new ArrayList<>();

        // If the segment is empty, return
        if (segment.segmentContent.isEmpty()) {
            return;
        }

        // If the segment has contents
        for (Object obj : segment.segmentContent) {
            if (obj instanceof Command2) {
                // If the object is a command, add it to the list of commands
                commands.add(((Command2) obj).command);
            } else if (obj instanceof Segment2) {
                // If the object is a segment, recursively iterate over its contents
                iterateSegments((Segment2) obj);
            }
        }

//    // Print the commands in the current segment after all nested segments
//    for (String command : commands) {
//      System.out.println(command);
//    }
    }



    public static String getBooleanCondition (String expr, int i) { // TODO: bracket na thaka case, first letter uppercase case
        StringBuilder condition = new StringBuilder();
        int j = i;

        while (expr.charAt(j) != '\n') {
            j--;
            if (j == 0)
                break;
        }

        String ifStatement = expr.substring(j, i);
        if ((ifStatement.charAt(ifStatement.lastIndexOf(')')-1)) != ')') {
            ifStatement = ifStatement.substring(0, ifStatement.lastIndexOf(')')) + "()" + ifStatement.substring(ifStatement.lastIndexOf(')'));
        }
//    System.out.println("If Statement: " + ifStatement);

        Pattern pattern = Pattern.compile("(?<=if\\(|\\.)\\w+\\(.*?\\)");
        Matcher matcher = pattern.matcher(ifStatement);

        if (matcher.find()) {
//      System.out.println(matcher.group());
            String condt = matcher.group();
            return Character.toLowerCase(condt.charAt(0)) + condt.substring(1);
        } else {
            return null;
        }
    }

    public static String getBlockName(String expr, int i) {
        int j = i;
        while (expr.charAt(j) != '\n') {
            j--;
            if (j == 0)
                break;
        }

        if (expr.substring(j, i).contains("for")) {
            return "for";
        } else if (expr.substring(j, i).contains("if")) {
            return "if";
        } else if (expr.substring(j, i).contains("else")) {
            return "else";
        }

        return "";
    }

    public static boolean isElseBlock (String expr, int i) {
        int j = i;

        while (expr.charAt(j) != '\n') {
            j++;
            if (j == expr.length() - 1)
                break;
        }

        if (expr.substring(i, j).contains("else")) {
            return true;
        }

        return false;
    }

    public static S_LS createAST(String expr) {
        Stack<Segment2> stack = new Stack<>();
        int commandStart = 0;
        Segment2 segments = new Segment2("strategy");
        S_LS finalS = new S_LS();
        S_LS elseS = null;  // null if no else segment
        List<S_LS> allSegments = new ArrayList<S_LS>(); // A new list to hold all outermost segments
        boolean inComment = false;  // Flag to track if we're in a comment

        for (int i = 0; i < expr.length(); i++) {
            if (inComment) {
                if (expr.charAt(i) == '\n') {
                    inComment = false;
                    commandStart = i + 1;
                }
                continue;
            }

            if (i < expr.length() - 1 && expr.charAt(i) == '/' && expr.charAt(i + 1) == '/') {
                inComment = true;
                continue;
            }

            if (expr.charAt(i) == '{') {
                if (getBlockName(expr, i).equals("if")) {
                    Segment2 newSegment = new Segment2("if");
                    commandStart = i + 1;
                    String condition = getBooleanCondition(expr, i);
//          System.out.println(">>>>>>>>>>>>>>>>>>" + condition + "<<<<<<<<<<<<<<<<<<<");
                    newSegment.b = createBooleanNode(condition);

                    stack.push(newSegment);
                } else if (getBlockName(expr, i).equals("else")) {
                    stack.push(new Segment2("else"));
                    commandStart = i + 1;
                } else if (getBlockName(expr, i).equals("for")) {
                    stack.push(new Segment2("for"));
                    commandStart = i + 1;
                }
            } else if (expr.charAt(i) == '}') {
                S_LS prevS;

                if ((i+6 < expr.length()) && (isElseBlock(expr, i))) {
                    int startIdx, endIdx = 0, curlyBraceStartIdx = 0, curlyBraceEndIdx;
                    int k = i;
                    int curlyBraceCount = 0;
                    while (expr.charAt(k) != 'e') {
                        k++;
                    }
                    startIdx = k;

                    for (int j = startIdx; j < expr.length(); j++) {
                        if (expr.charAt(j) == '{') {
                            curlyBraceCount++;
                            curlyBraceStartIdx = j;
                            break;
                        }
                    }

                    for (int j = curlyBraceStartIdx + 1; j < expr.length(); j++) {
                        if (expr.charAt(j) == '{') {
                            curlyBraceCount++;
                        } else if (expr.charAt(j) == '}') {
                            curlyBraceCount--;
                        }

                        if (curlyBraceCount == 0) {
                            curlyBraceEndIdx = j + 1;
                            endIdx = curlyBraceEndIdx;
                            break;
                        }
                    }

                    String elseExpr = expr.substring(startIdx, endIdx);
                    elseS = createAST(elseExpr);
                }

                if (!stack.isEmpty()) {
                    String command = expr.substring(commandStart, i).replaceAll("\\n\\s+", "\n").trim();
                    if (!command.isEmpty()) {
                        for (String cmd : command.split("\n")) {
                            stack.peek().add(new Command2(cmd));
                        }
                    }

                    Segment2 finishedSegment = stack.pop();
                    List<S_LS> listOfSegments = new ArrayList<S_LS>();
//          System.out.println("-------" + finishedSegment.name + "--------");
//
//          for (Object obj : finishedSegment.segmentContent) {
//            System.out.println(obj);
//          }
//          System.out.println();

                    for (Object obj : finishedSegment.segmentContent) {
                        S_LS s = new S_LS();
                        if (obj instanceof Command2) { // TODO: Might need to fix later
                            String cmd = obj.toString().substring(0, 2) + Character.toLowerCase(obj.toString().charAt(2)) + obj.toString().substring(3);
//              System.out.println(">>>>>>>>>" + cmd + "<<<<<<<<<");
                            s = createCommandNode(cmd);
                        } else if (obj instanceof Segment2 innerSegment) {
                            s = ((Segment2) obj).s;
                        }
                        listOfSegments.add(s);
                    }

                    prevS = listOfSegments.get(0);

                    if (listOfSegments.size() > 1) {
                        for (int j = 1; j < listOfSegments.size(); j++) {
                            prevS = new S_LS(new S_S_LS(prevS, listOfSegments.get(j)));
                        }
                    }

                    if (finishedSegment.name.equals("for")) {
                        finalS = new S_LS(new For_S_LS(prevS));
                        finishedSegment.s = finalS;
                    } else if (finishedSegment.name.equals("if")) {
                        if (elseS == null) {
                            finalS = new S_LS(new If_B_then_S_LS(finishedSegment.b, prevS));
                        } else {
                            finalS = new S_LS(new If_B_then_S_else_S_LS(finishedSegment.b, prevS, elseS));
                        }
                        finishedSegment.s = finalS;
                    } else if (finishedSegment.name.equals("else") && elseS == null) {
                        finalS = prevS;
                        finishedSegment.s = finalS;
                    } else {
                        finishedSegment.s = new S_LS(new Empty_LS());
//            System.out.println(finishedSegment.s.getChild().getName());
                    }

                    if (!stack.isEmpty()) {
                        stack.peek().add(finishedSegment);
                        commandStart = i + 1;
                    } else {
                        segments.add(finishedSegment);
                        allSegments.add(finalS); // When we finish a top level segment, add it to our list of allSegments
                    }
                }
            } else if (expr.charAt(i) == '\n') {
                String command = expr.substring(commandStart, i).trim();
                if (!command.isEmpty() && !stack.isEmpty()) {
                    stack.peek().add(new Command2(command));
                }
                commandStart = i + 1;
            }
        }

        if (inComment && commandStart < expr.length() - 1) {
            String command = expr.substring(commandStart).trim();
            if (!command.isEmpty() && !stack.isEmpty()) {
                stack.peek().add(new Command2(command));
            }
        }

        S_LS AST = allSegments.get(0);

        if (allSegments.size() > 1) {
            for (int i = 1; i < allSegments.size(); i++) {
                AST = new S_LS(new S_S_LS(AST, allSegments.get(i)));
            }
        }

        return AST;
    }
}
