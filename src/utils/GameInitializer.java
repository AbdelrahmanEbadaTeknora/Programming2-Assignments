//package utils;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//
//public class GameInitializer {
//
//    public static void initializeGameStructure() {
//        System.out.println("Initializing game structure...");
//
//        // Create folders
//        String[] folders = {
//                Constants.GAMES_FOLDER,
//                Constants.GAMES_FOLDER + File.separator + Constants.EASY_FOLDER,
//                Constants.GAMES_FOLDER + File.separator + Constants.MEDIUM_FOLDER,
//                Constants.GAMES_FOLDER + File.separator + Constants.HARD_FOLDER,
//                Constants.GAMES_FOLDER + File.separator + Constants.INCOMPLETE_FOLDER
//        };
//
//        for (String folder : folders) {
//            File dir = new File(folder);
//            if (!dir.exists()) {
//                boolean created = dir.mkdirs();
//                System.out.println("Created folder " + folder + ": " + (created ? "SUCCESS" : "FAILED"));
//            } else {
//                System.out.println("Folder already exists: " + folder);
//            }
//        }
//
//        // Create sample game files if folders are empty
//        createSampleGamesIfNeeded();
//    }
//
//    private static void createSampleGamesIfNeeded() {
//        String[] difficulties = {Constants.EASY_FOLDER, Constants.MEDIUM_FOLDER, Constants.HARD_FOLDER};
//        String sampleGame =
//                "1 2 0 4 5 6 7 8 9\n" +
//                        "4 5 6 7 8 9 1 2 3\n" +
//                        "7 8 9 1 2 3 4 5 6\n" +
//                        "2 3 4 5 6 7 8 9 1\n" +
//                        "5 6 7 8 9 1 2 3 4\n" +
//                        "8 9 1 2 3 4 5 6 7\n" +
//                        "3 4 5 6 7 8 9 1 2\n" +
//                        "6 7 8 9 1 2 3 4 5\n" +
//                        "9 1 2 3 4 5 6 7 8\n";
//
//        for (String difficulty : difficulties) {
//            String folderPath = Constants.GAMES_FOLDER + File.separator + difficulty;
//            File folder = new File(folderPath);
//
//            // Check if folder has any .txt files
//            File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
//
//            if (files == null || files.length == 0) {
//                // Create a sample game file
//                try {
//                    File gameFile = new File(folderPath + File.separator + "sample_game.txt");
//                    FileWriter writer = new FileWriter(gameFile);
//                    writer.write(sampleGame);
//                    writer.close();
//                    System.out.println("Created sample game in " + difficulty + ": " + gameFile.getAbsolutePath());
//                } catch (IOException e) {
//                    System.err.println("Failed to create sample game for " + difficulty + ": " + e.getMessage());
//                }
//            } else {
//                System.out.println(difficulty + " folder already has " + files.length + " game file(s)");
//            }
//        }
//    }
//
//    public static void printFolderStructure() {
//        System.out.println("\nCurrent folder structure:");
//        printFolder(new File(Constants.GAMES_FOLDER), 0);
//    }
//
//    private static void printFolder(File folder, int depth) {
//        if (!folder.exists()) {
//            System.out.println(getIndent(depth) + folder.getName() + " [MISSING]");
//            return;
//        }
//
//        System.out.println(getIndent(depth) + folder.getName() + "/");
//
//        File[] files = folder.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isDirectory()) {
//                    printFolder(file, depth + 1);
//                } else {
//                    System.out.println(getIndent(depth + 1) + file.getName() + " (" + file.length() + " bytes)");
//                }
//            }
//        }
//    }
//
//    private static String getIndent(int depth) {
//        return "  ".repeat(Math.max(0, depth));
//    }
//}