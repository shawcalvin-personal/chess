package ui;

/**
 * This class contains constants and functions relating to ANSI Escape Sequences that are useful in the Client display
 */
public class EscapeSequences {

    private static final String UNICODE_ESCAPE = "\u001b";
    public static final String SET_TEXT_BOLD = UNICODE_ESCAPE + "[1m";
    public static final String SET_TEXT_FAINT = UNICODE_ESCAPE + "[2m";
    private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
    private static final String SET_BG_COLOR = UNICODE_ESCAPE + "[48;5;";
    public static final String SET_TEXT_COLOR_SYSTEM_PROMPT = SET_TEXT_COLOR + "66m";
    public static final String SET_TEXT_COLOR_SYSTEM_MESSAGE = SET_TEXT_COLOR + "73m";
    public static final String SET_TEXT_COLOR_WEBSOCKET_MESSAGE = SET_TEXT_COLOR + "67m";
    public static final String SET_TEXT_COLOR_WEBSOCKET_ERROR = SET_TEXT_COLOR + "125m";
    public static final String SET_TEXT_COLOR_LIGHT_GREY = SET_TEXT_COLOR + "242m";
    public static final String RESET_BG_COLOR = "\u001B[0m";

    public static final String KING = " ♚ ";
    public static final String QUEEN = " ♛ ";
    public static final String BISHOP = " ♝ ";
    public static final String KNIGHT = " ♞ ";
    public static final String ROOK = " ♜ ";
    public static final String PAWN = " ♟ ";
    public static final String EMPTY = " \u2003 ";
    public static final String SET_LIGHT_SQUARE_COLOR = SET_BG_COLOR + "75m";
    public static final String SET_DARK_SQUARE_COLOR = SET_BG_COLOR + "24m";
    public static final String SET_DARK_HIGHLIGHT_SQUARE_COLOR = SET_BG_COLOR + "131m";
    public static final String SET_LIGHT_HIGHLIGHT_SQUARE_COLOR = SET_BG_COLOR + "217m";
    public static final String SET_DARK_PIECE_COLOR = SET_TEXT_COLOR + "0m";
    public static final String SET_LIGHT_PIECE_COLOR = SET_TEXT_COLOR + "255m";

}
