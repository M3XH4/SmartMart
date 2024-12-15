import java.util.*;

public interface GlobalInterface extends FontManager  {
    int width = 223;

    static void centerString(String color, String text) {
        int textLength = text.length();
        int totalPadding = width - textLength;

        if (totalPadding < 0) {
            return;
        }

        int paddingLeft = totalPadding / 2;
        int paddingRight = totalPadding - paddingLeft;

        System.out.println(color + " ".repeat(paddingLeft) + text + " ".repeat(paddingRight) + RESET);
    }
    static void centerStringLine(String color, String text) {
        int textLength = text.length();
        int totalPadding = width - textLength;

        if (totalPadding < 0) {
            return;
        }

        int paddingLeft = (totalPadding / 2);
        int paddingRight = (totalPadding - paddingLeft) - 2;

        System.out.println(color + "|" + " ".repeat(paddingLeft) + text + " ".repeat(paddingRight) + "|" + RESET);
    }
    static String centerColumnString(String text, int fill_width) {
        int textLength = text.length();
        int totalPadding = fill_width - textLength;

        if (totalPadding < 0) {
            return null;
        }

        int paddingLeft = totalPadding / 2;
        int paddingRight = totalPadding - paddingLeft;

        return " ".repeat(paddingLeft) + text + " ".repeat(paddingRight);
    }
    static String printPaddingLeft(String color, int spaces) {
        int totalPadding = width - spaces;

        if (totalPadding < 0) {
            return null;
        }
        int paddingLeft = totalPadding / 2;
        return color + " ".repeat(paddingLeft);
    }
    static String printPaddingRight(String color, int spaces) {
        int totalPadding = width - spaces;

        if (totalPadding < 0) {
            return null;
        }
        int paddingLeft = totalPadding / 2;
        int paddingRight = totalPadding - paddingLeft;

        return color + " ".repeat(paddingRight) + RESET;
    }

    static void printBackgroundColor(String color, int spaces) {
        System.out.println(color + " ".repeat(Math.max(0, spaces)) + RESET);
    }
    static void printBackgroundColor(String color) {
        System.out.println(color + " ".repeat(width) + RESET);
    }
    static void printBackgroundColor(String color, String text) {
        System.out.println(color + text + " ".repeat(Math.max(0, width - text.length())) + RESET);
    }
    static void printHorizontalLine(String color) {
        System.out.println(color + "=".repeat(Math.max(0, width)) + RESET);
    }
    static String putSpaceGaps(int spaces, String text) {
        int textLength = spaces - text.length();
        return text + " ".repeat(Math.max(0, textLength));
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
