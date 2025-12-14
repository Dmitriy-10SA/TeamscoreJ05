package electronic.scoreboard.utils.language;

public class LanguageTextPrinter {
    /**
     * Печать текста на нужном языке с переходом на следующую строку
     */
    public static void printTextWithNewLine(Language language, String ruText, String enText) {
        switch (language) {
            case RU -> System.out.println(ruText);
            case EN -> System.out.println(enText);
        }
    }

    /**
     * Печать текста на нужном языке без перехода на следующую строку
     */
    public static void printTextWithoutNewLine(Language language, String ruText, String enText) {
        switch (language) {
            case RU -> System.out.print(ruText);
            case EN -> System.out.print(enText);
        }
    }
}
