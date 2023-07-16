package readability;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        Scanner scan = new Scanner(file);
        scan.useDelimiter("\\Z");
        String string = scan.next();
        scan.close();
        Text text = new Text(string);


        System.out.println("The text is");
        System.out.println(string + "\n");
        System.out.printf("Words: %d\n", text.getWordCount());
        System.out.printf("Sentences: %d\n", text.getSentenceCount());
        System.out.printf("Characters: %d\n", text.getCharacterCount());
        System.out.printf("Syllables: %d\n", text.getSyllableCount());
        System.out.printf("Polysyllables: %d\n", text.getPolysyllableCount());

        System.out.print("\nEnter the score you want to calculate (ARI, FK, SMOG, CL, all): ");

        scan = new Scanner(System.in);
        String scoreType = scan.nextLine();

        text.calculateScore(scoreType);
        text.printProperties();
    }

}
