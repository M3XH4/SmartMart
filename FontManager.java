public interface FontManager {
    String RESET = "\u001B[0m";
    String CLEAR_LINE = "\u001B[2K"; // Clear the entire line
    String MOVE_UP = "\u001B[1A";   // Move cursor up one line
    String MOVE_END = "\u001B[0G"; // Move cursor to the beginning of the line

    // TEXT DESIGNS
    String BOLD = "\u001B[1m";
    String ITALIC = "\u001B[3m";
    String UNDERLINE = "\u001B[4m";
    String STRIKETHROUGH = "\u001B[9m";
    String DOUBLY_UNDERLINE = "\u001B[21m";
    String FRAMED = "\u001B[51m";
    String ENCIRCLED = "\u001B[52m";
    String OVERLINE = "\u001B[53m";
    String ERASE_ENTIRE_SCREEN = "\u001B[2J";

    // TEXT COLORS
    String TEXT_BLACK = "\u001B[30m";
    String TEXT_RED = "\u001B[31m";
    String TEXT_GREEN = "\u001B[32m";
    String TEXT_YELLOW = "\u001B[33m";
    String TEXT_BLUE = "\u001B[34m";
    String TEXT_PURPLE = "\u001B[35m";
    String TEXT_CYAN = "\u001B[36m";
    String TEXT_WHITE = "\u001B[37m";

    // TEXT BACKGROUND COLORS
    String BACKGROUND_BLACK = "\u001B[40m";
    String BACKGROUND_RED = "\u001B[41m";
    String BACKGROUND_GREEN = "\u001B[42m";
    String BACKGROUND_YELLOW = "\u001B[43m";
    String BACKGROUND_BLUE = "\u001B[44m";
    String BACKGROUND_PURPLE = "\u001B[45m";
    String BACKGROUND_CYAN = "\u001B[46m";
    String BACKGROUND_WHITE = "\u001B[47m";

    // TEXT BRIGHT TEXT COLORS
    String TEXT_BLACK_BRIGHT = "\u001B[90m";
    String TEXT_RED_BRIGHT = "\u001B[91m";
    String TEXT_GREEN_BRIGHT = "\u001B[92m";
    String TEXT_YELLOW_BRIGHT = "\u001B[93m";
    String TEXT_BLUE_BRIGHT = "\u001B[94m";
    String TEXT_PURPLE_BRIGHT = "\u001B[95m";
    String TEXT_CYAN_BRIGHT = "\u001B[96m";
    String TEXT_WHITE_BRIGHT = "\u001B[97m";

    // TEXT BRIGHT BACKGROUND COLORS
    String BACKGROUND_BLACK_BRIGHT = "\u001B[100m";
    String BACKGROUND_RED_BRIGHT = "\u001B[101m";
    String BACKGROUND_GREEN_BRIGHT = "\u001B[102m";
    String BACKGROUND_YELLOW_BRIGHT = "\u001B[103m";
    String BACKGROUND_BLUE_BRIGHT = "\u001B[104m";
    String BACKGROUND_PURPLE_BRIGHT = "\u001B[105m";
    String BACKGROUND_CYAN_BRIGHT = "\u001B[106m";
    String BACKGROUND_WHITE_BRIGHT = "\u001B[107m";

    //COMBO PALETTE
    //TEXT COLOR, BACKGROUND, DESIGN
    String PRIMARY_COMBO = "\u001B[97;102;1m";
    String SECONDARY_COMBO = "\u001B[30;107;1m";
    String ERROR_COMBO = "\u001B[97;101;1m";
    String PERFECT_COMBO = "\u001B[97;102;1m";
    String WARNING_COMBO = "\u001B[97;103;1m";
    String WRONG_COMBO = "\u001B[91;107;1m";
    String MAYBE_COMBO = "\u001B[93;107;1m";
    String CORRECT_COMBO = "\u001B[92;107;1m";
    String RESPONSE_COMBO = "\u001B[97;40;1m";
    String tertiaryCombo = "\u001B[41;30m";
}
