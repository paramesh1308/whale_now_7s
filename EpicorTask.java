
    import java.io.*;
    import java.net.URL;
    import java.nio.file.Files;
    import java.nio.file.Paths;
    import java.util.*;
    import java.util.stream.Collectors;
    import java.util.Scanner;
    import java.util.Set;

    public class EpicorTask {

        private static final Set<String> stopWords = Set.of( "aboard", "about", "above", "across", "after", "against", "along", "amid", "among", "around", "as", "at", "before",
                "behind", "below", "beneath", "beside", "besides", "between", "beyond", "but", "by", "concerning", "considering",
                "despite", "down", "during", "except", "excluding", "following", "for", "from", "in", "inside", "into", "like",
                "minus", "near", "next", "of", "off", "on", "onto", "opposite", "outside", "over", "past", "per", "plus",
                "regarding", "round", "save", "since", "than", "through", "throughout", "till", "to", "toward", "towards",
                "under", "underneath", "unlike", "until", "up", "upon", "versus", "via", "with", "within", "without", "i",
                "me", "you", "he", "him", "she", "her", "it", "we", "us", "they", "them", "mine", "yours", "his", "hers",
                "ours", "theirs", "myself", "yourself", "himself", "herself", "itself", "ourselves", "yourselves", "themselves",
                "this", "that", "these", "those", "anyone", "everybody", "nobody", "somebody", "someone", "everyone", "none",
                "all", "few", "many", "several", "some", "who", "whom", "whose", "which", "what", "the", "a", "an", "and",
                "or", "nor", "so", "yet", "although", "because", "even", "if", "once", "though", "unless", "when", "where",
                "while", "whereas", "whether", "provided", "either", "neither", "not", "only", "also", "both", "rather", "is", "was", "s");

        private static String inputPath = null;
        private static String outputPath = null;
        public static void main(String[] args) {
            long startTime = System.currentTimeMillis();
            Scanner scanner = new Scanner(System.in);

            // Read the file URL from user
           /* System.out.println("Enter file URL :: ");
            String fileUrl = scanner.nextLine();
            System.out.println("Enter files saved directory :: ");
            String fileDir = scanner.nextLine();*/

            // Input as static
            String fileUrl = "https://courses.cs.washington.edu/courses/cse390c/22sp/lectures/moby.txt";
            String fileDir = "C:\\Epicor";

            // Download and Read File
            try {
                importFile(fileUrl, fileDir);
                List<String> words = processFile(inputPath);
                analyze(words);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Track Execution Time
            long endTime = System.currentTimeMillis();
            long processingTime = (endTime - startTime) / 1000;
            System.out.println("Processing Time: " + processingTime + " seconds");
        }

        // Downloads file from URL
        private static void importFile(String fileUrl, String fileDir) throws IOException {
            File directory = new File(fileDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create the file inside the directory
            long currtimemillis = System.currentTimeMillis();
            inputPath = new File(fileDir + "/" + String.valueOf(currtimemillis) + "_input.txt").getPath();
            outputPath = new File(fileDir + "/" + String.valueOf(currtimemillis) + "_output.txt").getPath();
            System.out.println(inputPath);
            try (InputStream in = new URL(fileUrl).openStream()) {
                Files.copy(in, Paths.get(inputPath));
                System.out.println("File downloaded successfully in :: " + inputPath);
            }
        }

        // Reads and processes the file content
        private static List<String> processFile(String filePath) throws IOException {
            String text = new String(Files.readAllBytes(Paths.get(filePath)));

            // Normalize and split into words
            text = text.toLowerCase().replaceAll("[^a-zA-Z\\s]", " "); // Remove punctuation
            List<String> words = Arrays.asList(text.split("\\s+"));

            // Filter out stop words and remove possessive's
            return words.stream()
                    .filter(word -> !stopWords.contains(word) && !word.endsWith("'s"))
                    .collect(Collectors.toList());
        }

        // Analyze and print word statistics
        private static void analyze(List<String> words) throws IOException {
            // Total word count
            System.out.println("Total word count : " + words.size());

            Map<String, Long> wordFreq = words.stream()
                    .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

            // Get Top 5 most used words
            List<Map.Entry<String, Long>> topWords = wordFreq.entrySet().stream()
                    .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                    .limit(5)
                    .toList();

            System.out.println("\nTop 5 most frequent words : ");
            topWords.forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue() + " times "));

            // Top 50 unique words sorted alphabetically
            List<String> uniqueWords = wordFreq.keySet().stream()
                    .sorted()
                    .limit(50)
                    .toList();

            System.out.println("\nTop 50 unique words (Alphabetical Order) : ");
            uniqueWords.forEach(System.out::println);

            // Save output to a file
            saveOutput(words.size(), topWords, uniqueWords);
        }

        // Save analysis output to a file
        private static void saveOutput(int totalCount, List<Map.Entry<String, Long>> topWords, List<String> uniqueWords) throws IOException {
            try (PrintWriter writer = new PrintWriter(outputPath)) {
                writer.println("Total word count : " + totalCount);
                writer.println("\nTop 5 most frequent words : ");
                topWords.forEach(entry -> writer.println(entry.getKey() + " : " + entry.getValue()));

                writer.println("\nTop 50 unique words (Alphabetical Order) : ");
                uniqueWords.forEach(writer::println);
            }
            System.out.println("\nOutput successfully saved in :: " + outputPath);
        }
    }