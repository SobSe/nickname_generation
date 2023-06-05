import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static String LETTERS = "abc";
    public static int COUNT_TEXTS = 100_000;
    public static AtomicInteger countTextsLength_3 = new AtomicInteger(0);
    public static AtomicInteger countTextsLength_4 = new AtomicInteger(0);
    public static AtomicInteger countTextsLength_5 = new AtomicInteger(0);

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[COUNT_TEXTS];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText(LETTERS, 3 + random.nextInt(3));
        }
        Thread threadPalindrome = new Thread(() -> {
            for (String text : texts) {
                checkPalindrome(text);
            }
        });
        Thread threadTheSame = new Thread(() -> {
            for (String text : texts) {
                checkSingleCharacter(text);
            }
        });
        Thread threadIncrease = new Thread(() -> {
            for (String text : texts) {
                checkSingleCharacter(text);
            }
        });

        threadIncrease.start();
        threadPalindrome.start();
        threadTheSame.start();

        try {
            threadIncrease.join();
            threadPalindrome.join();
            threadTheSame.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Красивых слов с длиной 3: %d шт\n", countTextsLength_3.get());
        System.out.printf("Красивых слов с длиной 4: %d шт\n", countTextsLength_4.get());
        System.out.printf("Красивых слов с длиной 5: %d шт\n", countTextsLength_5.get());
    }

    public static void checkPalindrome(String text) {
        char[] chars = text.toCharArray();
        boolean done = true;
        for (int i = 0; i < chars.length; i++) {
            if (i == (chars.length - 1 -i) || i > (chars.length - 1 -i)) {
                break;
            }
            if (chars[i] != chars[chars.length - 1 -i]) {
                done = false;
                break;
            }
        }
        if (done) addCount(text);
    }

    public static void checkSingleCharacter(String text) {
        boolean done = text
                .chars()
                .noneMatch((c) -> (c != text.charAt(0)));
        if (done) addCount(text);
    }

    public static void checkIncrease(String text) {
        char[] chars = text.toCharArray();
        boolean done = true;
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] > chars[i + 1]) {
                done = false;
                break;
            }
        }
        if (done) addCount(text);
    }

    public static void  addCount(String text) {
        if (text.length() == 3) {
            countTextsLength_3.incrementAndGet();
        } else if (text.length() == 4) {
            countTextsLength_4.incrementAndGet();
        } else if (text.length() == 5) {
            countTextsLength_5.incrementAndGet();
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
