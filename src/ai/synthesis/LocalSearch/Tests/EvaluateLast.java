package ai.synthesis.LocalSearch.Tests;

import ai.core.AI;
import ai.synthesis.LocalSearch.EvaluateGameState.SimplePlayout;
import ai.synthesis.LocalSearch.LS_CFG.FactoryLS;
import ai.synthesis.LocalSearch.LS_CFG.Node_LS;
import ai.synthesis.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.Synthesis_Base.CFG.Control;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.UnitTypeTable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluateLast {
    public static void main(String[] args) throws Exception {
        int LINES = 50;
        int RUNS = 30;
        String[] maps = new String[1];
        String path_map = "";
        int max = 6000;

        if (args[0].equals("1")) {
            maps[0] = "24x24";
            path_map = "maps/24x24/basesWorkers24x24A.xml";
        } else if (args[0].equals("2")) {
            maps[0] = "64x64";
            path_map = "maps/BroodWar/(4)BloodBath.scmB.xml";
            max = 15000;
        } else if (args[0].equals("3")) {
            maps[0] = "9x8";
            path_map = "maps/NoWhereToRun9x8.xml";
        } else if (args[0].equals("4")) {
            maps[0] = "16x16";
            path_map = "maps/16x16/basesWorkers16x16A.xml";
        } else if (args[0].equals("5")) {
            maps[0] = "32x32";
            path_map = "maps/32x32/basesWorkers32x32A.xml";
        } else if (args[0].equals("6")) {
            maps[0] = "DG";
            path_map = "maps/DoubleGame24x24.xml";
        } else if (args[0].equals("7")) {
            maps[0] = "BW";
            path_map = "maps/BWDistantResources32x32.xml";
        }

        int minLimit = Integer.parseInt(args[1]);
        int maxLimit = Integer.parseInt(args[2]);
        System.out.println("min = " + args[1]);
        System.out.println("max = " + args[2]);
//        String[] algorithms = {"FP", "FP_LLM", "LL", "LL_LLM"};
//        String[] algorithms = {"IBR", "IBR_LLM", "FP", "FP_LLM"};
        String[] algorithms = {"IBR", "FP", "LL", "LL_LLM"};
//        String[] algorithms = {"IBR", "FP", "DO", "LL"};
        HashMap<String, HashMap<String, List<List<String>>>> mapHashMap = new HashMap<>();

        for (String map : maps) {
            HashMap<String, List<List<String>>> algorithmHashMap = new HashMap<>();
            for (String algorithm : algorithms) {
                List<List<String>> strategyList = new ArrayList<>();
                String folderPath = "outs/" + map + "/" + algorithm;
//                System.out.println("outs/" + map + "/" + algorithm);

                File folder = new File(folderPath);
                if (folder.exists() && folder.isDirectory()) {
                    File[] files = folder.listFiles();

                    if (files != null) {
                        for (File file : files) {
                            if (file.isFile() && !file.isHidden()) {
//                                System.out.println();
//                                System.out.println("------------------------");
//                                System.out.println(file.getName());
//                                System.out.println("------------------------");
//                                System.out.println();
                                List<String> strategies = new ArrayList<String>();
                                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                                    String line;
                                    while ((line = br.readLine()) != null) {
//                                        System.out.println(line);
                                        String[] parts = line.split(",");
//                                        System.out.println(parts[1]);
                                        strategies.add(parts[1]);
                                    }
                                    strategyList.add(strategies);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Invalid folder path!");
                }
                algorithmHashMap.put(algorithm, strategyList);
            }
            mapHashMap.put(map, algorithmHashMap);
        }

        UnitTypeTable utt = new UnitTypeTable(2);
        //        String path_map = "maps/8x8/basesWorkers8x8A.xml";
//        String path_map = "maps/24x24/basesWorkers24x24A.xml";
//        String path_map = "maps/32x32/basesWorkers32x32A.xml";
//        String path_map = "maps/16x16/basesWorkers16x16A.xml";
//        String path_map = "maps/BWDistantResources32x32.xml";
//        String path_map = "maps/BroodWar/(4)BloodBath.scmB.xml";
//        String path_map = "maps/8x8/FourBasesWorkers8x8.xml";
//        String path_map = "maps/16x16/TwoBasesBarracks16x16.xml";
//        String path_map = "maps/NoWhereToRun9x8.xml";
//        String path_map = "maps/DoubleGame24x24.xml";

        PhysicalGameState pgs = PhysicalGameState.load(path_map, utt);
        GameState gs2 = new GameState(pgs, utt);

        SimplePlayout playout = new SimplePlayout();
        FactoryLS f = new FactoryLS();

        for (String map : maps) {
            System.out.println();
            System.out.println("--------------------------");
            System.out.println("Map = " + map);
            System.out.println("--------------------------");
            System.out.println();

            HashMap<String, List<String>> finalStrategies = new HashMap<>();

            for (String algorithm : algorithms) {
                List<String> strategies = new ArrayList<String>();
                for (int i = 0; i < RUNS; i++) {
                    strategies.add(mapHashMap.get(map).get(algorithm).get(i).get(LINES - 1));
                }
                finalStrategies.put(algorithm, strategies);
            }

            for (int i = minLimit; i < maxLimit; i++) {
                double[] r = {0.0, 0.0, 0.0, 0.0};
                for (int j = 0; j < RUNS; j++) {
                    List<String> strategies = new ArrayList<String>();
                    List<String> lastStrategies = new ArrayList<String>();
                    for (String algorithm : algorithms) {
//                        System.out.println(mapHashMap.get(map).get(algorithm).get(i).get(j));
                        strategies.add(mapHashMap.get(map).get(algorithm).get(j).get(i));
                        String strategy1 = mapHashMap.get(map).get(algorithm).get(j).get(i);

                        Node_LS pre_ai = (Node_LS) Control.load(strategy1, f);
                        AI ai1 = new Interpreter(utt, pre_ai);
                        double score = 0.0;

                        for (Map.Entry<String,List<String>> stratList : finalStrategies.entrySet()) {
                            if (!stratList.getKey().equals(algorithm)) {
                                for (String strategy2 : stratList.getValue()) {
                                    Node_LS pre_ai2 = (Node_LS) Control.load(strategy2, f);
                                    //translate it to a AI
                                    AI ai2 = new Interpreter(utt, pre_ai2);

                                    double r1 = playout.run(gs2, utt,0, max, ai1, ai2, false).m_a;
                                    double r2 = playout.run(gs2, utt,1, max, ai1, ai2, false).m_a;

                                    score += (r1 + r2) / 2.0;
                                }
                            }
                        }

                        if (!algorithm.equals(algorithms[algorithms.length - 1])) {
                            System.out.print(score + ",");
                        } else {
                            System.out.println(score);
                            System.out.println(strategy1);
                        }
                    }

//                    for (String algorithm : algorithms) {
//                        lastStrategies.add(mapHashMap.get(map).get(algorithm).get(j).get(LINES-1));
//                    }

//                    for (int p = 0; p < strategies.size(); p++) {
////                        List<Integer> scoresList = new ArrayList<Integer>();
//                        String strategy1 = strategies.get(p);
//                        Node_LS pre_ai = (Node_LS) Control.load(strategy1, f);
//                        AI ai1 = new Interpreter(utt, pre_ai);
//                        double score = 0.0;
//
//
//
//                        for (int q = 0; q < lastStrategies.size(); q++) {
//                            String strategy2 = lastStrategies.get(q);
//                            Node_LS pre_ai2 = (Node_LS) Control.load(strategy2, f);
//                            //translate it to a AI
//                            AI ai2 = new Interpreter(utt, pre_ai2);
//
//                            double r1 = playout.run(gs2, utt,0, max, ai1, ai2, false).m_a;
//                            double r2 = playout.run(gs2, utt,1, max, ai1, ai2, false).m_a;
//
//                            score += (r1 + r2) / 2.0;
////                            System.out.println("r1 = " + r1);
////                            System.out.println("r2 = " + r2);
////                            System.out.println();
////
////                            if (r1 == 1.0) {
////                                r[p] += r1;
////                                score++;
////                            } else if (r1 == 0.5) {
////                                r[p] += 0.5;
////                                score += 0.5;
////                            }
////                            else if ((r1 == 0.5 && r2 == 0.5) || (r1 == 1.0 && r2 == 0.5) || (r1 == 0.5 && r2 == 1.0)) {
////                                r[p] += 0.5;
////                                score += 0.5;
////                            }
//                        }
//                        if (p < strategies.size()-1) {
//                            System.out.print(score + ",");
//                        } else {
//                            System.out.println(score);
//                        }
//                    }
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
                }
//                for (double item : r) {
//                    System.out.println("r = " + item);
//                }
                System.out.println("-----------------------");
//                System.out.println();
            }
        }
    }
}

