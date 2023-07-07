import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class WordCount {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        Map<String, Long> wordsCount = countWords("src/main/resources/data.txt");
        System.out.println("Output for sequential processing :: " + wordsCount);
        long end = System.currentTimeMillis();
        System.out.println("Total execution time in milli seconds for sequential processing: "+ (end - start));
        //wordsCount.entrySet().forEach(System.out::println);

        long start1 = System.currentTimeMillis();
        Map<String, Long> wordsCount1 = countWordsParallel("src/main/resources/data.txt");
        System.out.println("Output for parallel processing :: " + wordsCount1);
        long end1 = System.currentTimeMillis();
        System.out.println("Total execution time in milli seconds for parallel processing: " + (end1 - start1));
    }

    /**
     * sequential processing
     *
     * @param fileName
     * @return map of word and count
     * @throws IOException
     */
    public static Map<String, Long> countWords(final String fileName) throws IOException {

        return Files.lines(Paths.get(fileName))
                .flatMap(line -> Arrays.stream(line.trim().split(" ")))
                .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase().trim())
                .filter(word -> !word.isEmpty())
                .collect(groupingBy(Function.identity(), counting()));
    }

    /**
     * parallel processing
     *
     * @param fileName
     * @return map of word and count
     * @throws IOException
     */
    public static Map<String, Long> countWordsParallel(final String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName))
                .parallelStream()
                .flatMap(line -> Arrays.stream(line.trim().split(" ")).parallel())
                .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase().trim())
                .filter(word -> !word.isEmpty())
                .collect(groupingBy(Function.identity(), counting()));
    }
}
