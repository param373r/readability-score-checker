package readability;


public class Text {
    String text;
    int characterCount;
    int wordCount;
    int sentenceCount;
    int syllableCount;
    int polysyllableCount;
    double ARI = 0;
    double FK = 0;
    double SMOG = 0;
    double CL = 0;

    public int getWordCount() {
        return wordCount;
    }
    public int getSentenceCount() {
        return sentenceCount;
    }
    public int getCharacterCount() {
        return characterCount;
    }
    public int getSyllableCount() {
        return syllableCount;
    }
    public int getPolysyllableCount() {
        return polysyllableCount;
    }

    public Text(String text) {
        this.text = text;
        calculateProperties();
    }

    public void calculateScore(String scoreType) {
        switch (scoreType) {
            case "ARI" ->
                    ARI = 4.71 * ((double) characterCount / wordCount) + 0.5 * ((double) wordCount / sentenceCount) - 21.43;
            case "FK" ->
                    FK = 0.39 * ((double) wordCount / sentenceCount) + 11.8 * ((double) syllableCount / wordCount) - 15.59;
            case "SMOG" -> SMOG = 1.043 * (Math.sqrt(polysyllableCount * ((double) 30 / sentenceCount)) + 3.1291);
            case "CL" -> CL = 0.0588 * (100.0 * characterCount / wordCount) - 0.296 * (100.0 * sentenceCount / wordCount) - 15.8;
            case "all" -> {
                ARI = 4.71 * ((double) characterCount / wordCount) + 0.5 * ((double) wordCount / sentenceCount) - 21.43;
                FK = 0.39 * ((double) wordCount / sentenceCount) + 11.8 * ((double) syllableCount / wordCount) - 15.59;
                SMOG = 1.043 * (Math.sqrt(polysyllableCount * ((double) 30 / sentenceCount)) + 3.1291);
                CL = 0.0588 * (100.0 * characterCount / wordCount) - 0.296 * (100.0 * sentenceCount / wordCount) - 15.8;
            }
        }
    }

    void calculateProperties() {
        wordCount = 1;
        char[] characters = text.toCharArray();
        for (char character : characters) {
            if (character == ' ') {
                wordCount++;
            } else if (character == '?' || character == '!' || character == '.') {
                sentenceCount++;
            }

            if (character != ' ' && character != '\n' && character != '\t') {
                characterCount++;
            }
        }
        if (characters[characters.length - 1] != '.') {
            sentenceCount++;
        }
        String[] words = text.split(" ");
        for (String word : words) {
            int currentSyllableCount = findSyllable(word);
            syllableCount += currentSyllableCount;
            if (currentSyllableCount > 2) {
                polysyllableCount++;
            }

        }
    }

    private static int findSyllable(String word) {
        int counter = 0;
        boolean wasVowel = false;
        for (int i = 0; i < word.length(); i++) {
            char c = Character.toLowerCase(word.charAt(i));
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y') {
                if (!wasVowel) {
                    wasVowel = true;
                    counter++;;
                }
            } else {
                wasVowel = false;
            }
        }
        if (word.charAt(word.length() - 1) == 'e') {
            counter--;
        }
        if (counter == 0) {
            counter = 1;
        }
        return counter;
    }

    public int calculateAgeGroup(double score) {
        int scoreAbsolute = (int) Math.ceil(score);
        return switch (scoreAbsolute) {
            case 1 -> 6;
            case 2 -> 7;
            case 3 -> 8;
            case 4 -> 9;
            case 5 -> 10;
            case 6 -> 11;
            case 7 -> 12;
            case 8 -> 13;
            case 9 -> 14;
            case 10 -> 15;
            case 11 -> 16;
            case 12 -> 17;
            case 13 -> 18;
            case 14 -> 22;
            default -> 0;
        };
    }
    public void printProperties() {

        int divideBy = 0;
        int ageARI = 0, ageFK = 0, ageSMOG = 0, ageCL = 0;
        if (ARI != 0) {
            ageARI = calculateAgeGroup(ARI);
            System.out.printf("\nAutomated Readability Index: %.2f (about %d-year-olds)\n", ARI, ageARI);
            divideBy++;
        }
        if (FK != 0) {
            ageFK = calculateAgeGroup(FK);
            System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds)\n", FK, ageFK);
            divideBy++;
        }
        if (SMOG != 0) {
            ageSMOG = calculateAgeGroup(SMOG);
            System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds)\n", SMOG, ageSMOG);
            divideBy++;
        }
        if (CL != 0) {
            ageCL = calculateAgeGroup(CL);
            System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds)\n", CL, ageCL);
            divideBy++;
        }

        System.out.printf("This text should be understood in average by %.2f-year-olds\n", (double) (ageARI + ageCL + ageFK + ageSMOG) / divideBy);

    }
}
