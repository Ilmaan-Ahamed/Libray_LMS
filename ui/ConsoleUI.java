package Libray_LMS.ui;

/**
 * ConsoleUI — Premium console output styling utility.
 * Provides ANSI color support, box-drawing, tables, and branded headers.
 */
public class ConsoleUI {

    // ─── ANSI Color Codes ─────────────────────────────────────────
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String DIM = "\u001B[2m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";

    // Foreground Colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Bright Foreground Colors
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_MAGENTA = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";

    // Background Colors
    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_MAGENTA = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";

    // ─── Box-Drawing Characters ───────────────────────────────────
    private static final int DEFAULT_WIDTH = 56;

    // ─── Branding ─────────────────────────────────────────────────

    /**
     * Prints the application banner/splash screen.
     */
    public static void printBanner() {
        System.out.println();
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ╔══════════════════════════════════════════════════╗" + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ║                                                  ║" + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ║" + BRIGHT_YELLOW + "     ██╗     ███╗   ███╗ ███████╗               " + BRIGHT_CYAN + "║"
                + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ║" + BRIGHT_YELLOW + "     ██║     ████╗ ████║ ██╔════╝               " + BRIGHT_CYAN + "║"
                + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ║" + BRIGHT_YELLOW + "     ██║     ██╔████╔██║ ███████╗               " + BRIGHT_CYAN + "║"
                + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ║" + BRIGHT_YELLOW + "     ██║     ██║╚██╔╝██║ ╚════██║               " + BRIGHT_CYAN + "║"
                + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ║" + BRIGHT_YELLOW + "     ███████╗██║ ╚═╝ ██║ ███████║               " + BRIGHT_CYAN + "║"
                + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ║" + BRIGHT_YELLOW + "     ╚══════╝╚═╝     ╚═╝ ╚══════╝               " + BRIGHT_CYAN + "║"
                + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ║                                                  ║" + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ║" + BRIGHT_WHITE + "       LIBRARY  MANAGEMENT  SYSTEM                " + BRIGHT_CYAN + "║"
                + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ║" + DIM + CYAN + "              v2.0  ─  Console Edition              " + BRIGHT_CYAN + BOLD + "║"
                + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ║                                                  ║" + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "    ╚══════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    // ─── Menu Rendering ───────────────────────────────────────────

    /**
     * Prints a styled menu header.
     */
    public static void printMenuHeader(String title) {
        int width = DEFAULT_WIDTH;
        String top = "╔" + "═".repeat(width) + "╗";
        String bottom = "╠" + "═".repeat(width) + "╣";

        System.out.println();
        System.out.println(BRIGHT_CYAN + BOLD + top + RESET);
        System.out.println(BRIGHT_CYAN + BOLD + "║" + RESET +
                centerText(BRIGHT_YELLOW + BOLD + title + RESET, width) +
                BRIGHT_CYAN + BOLD + "║" + RESET);
        System.out.println(BRIGHT_CYAN + BOLD + bottom + RESET);
    }

    /**
     * Prints a single menu item.
     */
    public static void printMenuItem(int number, String icon, String label) {
        int width = DEFAULT_WIDTH;
        String content = String.format("  %s%d%s. %s %s",
                BRIGHT_WHITE + BOLD, number, RESET,
                icon,
                WHITE + label + RESET);
        // We need to pad based on visible length (without ANSI codes)
        String visible = String.format("  %d. %s %s", number, icon, label);
        int padding = width - visibleLength(visible);
        System.out.println(BRIGHT_CYAN + BOLD + "║" + RESET +
                content + " ".repeat(Math.max(0, padding)) +
                BRIGHT_CYAN + BOLD + "║" + RESET);
    }

    /**
     * Prints the menu footer.
     */
    public static void printMenuFooter() {
        int width = DEFAULT_WIDTH;
        System.out.println(BRIGHT_CYAN + BOLD + "╚" + "═".repeat(width) + "╝" + RESET);
    }

    /**
     * Prints the input prompt arrow.
     */
    public static void printPrompt(String text) {
        System.out.print(BRIGHT_GREEN + BOLD + "  ➜ " + RESET + WHITE + text + RESET);
    }

    // ─── Section Headers ──────────────────────────────────────────

    /**
     * Prints a section header (for forms, sub-sections).
     */
    public static void printSectionHeader(String title) {
        int width = DEFAULT_WIDTH;
        System.out.println();
        System.out.println(BRIGHT_MAGENTA + BOLD + "  ┌" + "─".repeat(width - 2) + "┐" + RESET);
        String padded = centerTextPlain(title, width - 2);
        System.out.println(BRIGHT_MAGENTA + BOLD + "  │" + BRIGHT_WHITE + padded + BRIGHT_MAGENTA + "│" + RESET);
        System.out.println(BRIGHT_MAGENTA + BOLD + "  └" + "─".repeat(width - 2) + "┘" + RESET);
    }

    // ─── Table Rendering ──────────────────────────────────────────

    /**
     * Prints a table header row with column names.
     */
    public static void printTableHeader(String... columns) {
        System.out.println();
        StringBuilder header = new StringBuilder();
        StringBuilder separator = new StringBuilder();
        header.append(BRIGHT_CYAN + "  ┌");
        separator.append(BRIGHT_CYAN + "  ├");

        for (int i = 0; i < columns.length; i++) {
            int colWidth = getColumnWidth(columns[i], i, columns.length);
            header.append("─".repeat(colWidth + 2));
            separator.append("─".repeat(colWidth + 2));
            if (i < columns.length - 1) {
                header.append("┬");
                separator.append("┼");
            }
        }
        header.append("┐" + RESET);
        separator.append("┤" + RESET);

        System.out.println(header);

        // Column names
        StringBuilder row = new StringBuilder();
        row.append(BRIGHT_CYAN + "  │" + RESET);
        for (int i = 0; i < columns.length; i++) {
            int colWidth = getColumnWidth(columns[i], i, columns.length);
            row.append(BOLD + BRIGHT_WHITE + " " + padRight(columns[i], colWidth) + " " + RESET);
            row.append(BRIGHT_CYAN + "│" + RESET);
        }
        System.out.println(row);
        System.out.println(separator);
    }

    /**
     * Prints a table data row.
     */
    public static void printTableRow(String... values) {
        StringBuilder row = new StringBuilder();
        row.append(BRIGHT_CYAN + "  │" + RESET);
        for (int i = 0; i < values.length; i++) {
            int colWidth = getColumnWidth(values[i], i, values.length);
            row.append(WHITE + " " + padRight(values[i], colWidth) + " " + RESET);
            row.append(BRIGHT_CYAN + "│" + RESET);
        }
        System.out.println(row);
    }

    /**
     * Prints a table footer.
     */
    public static void printTableFooter(int columnCount) {
        StringBuilder footer = new StringBuilder();
        footer.append(BRIGHT_CYAN + "  └");
        for (int i = 0; i < columnCount; i++) {
            // Use a reasonable default width for each column
            int colWidth = (i == 0) ? 6 : (i == columnCount - 1) ? 12 : 20;
            footer.append("─".repeat(colWidth + 2));
            if (i < columnCount - 1)
                footer.append("┴");
        }
        footer.append("┘" + RESET);
        System.out.println(footer);
    }

    /**
     * Prints a custom table footer with specific column widths.
     */
    public static void printTableFooterCustom(int... columnWidths) {
        StringBuilder footer = new StringBuilder();
        footer.append(BRIGHT_CYAN + "  └");
        for (int i = 0; i < columnWidths.length; i++) {
            footer.append("─".repeat(columnWidths[i] + 2));
            if (i < columnWidths.length - 1)
                footer.append("┴");
        }
        footer.append("┘" + RESET);
        System.out.println(footer);
    }

    // ─── Status Messages ──────────────────────────────────────────

    public static void success(String message) {
        System.out.println(BRIGHT_GREEN + BOLD + "  ✔ " + RESET + GREEN + message + RESET);
    }

    public static void error(String message) {
        System.out.println(BRIGHT_RED + BOLD + "  ✖ " + RESET + RED + message + RESET);
    }

    public static void warning(String message) {
        System.out.println(BRIGHT_YELLOW + BOLD + "  ⚠ " + RESET + YELLOW + message + RESET);
    }

    public static void info(String message) {
        System.out.println(BRIGHT_BLUE + BOLD + "  ℹ " + RESET + BLUE + message + RESET);
    }

    // ─── Statistics Box ───────────────────────────────────────────

    /**
     * Prints a statistics box with label-value pairs.
     */
    public static void printStatsBox(String title, String[][] stats) {
        int width = DEFAULT_WIDTH;
        System.out.println();
        System.out.println(BRIGHT_CYAN + BOLD + "  ╔" + "═".repeat(width - 2) + "╗" + RESET);

        String titlePadded = centerTextPlain(title, width - 2);
        System.out.println(BRIGHT_CYAN + BOLD + "  ║" + BRIGHT_YELLOW + BOLD + titlePadded + BRIGHT_CYAN + "║" + RESET);

        System.out.println(BRIGHT_CYAN + BOLD + "  ╠" + "═".repeat(width - 2) + "╣" + RESET);

        for (String[] stat : stats) {
            String icon = stat[0];
            String label = stat[1];
            String value = stat[2];

            String line = String.format("   %s %-22s : %s", icon, label, value);
            int padding = (width - 2) - visibleLength(line);
            System.out.println(BRIGHT_CYAN + BOLD + "  ║" + RESET +
                    WHITE + "   " + icon + " " + DIM + padRight(label, 22) + RESET +
                    WHITE + " : " + BRIGHT_WHITE + BOLD + value + RESET +
                    " ".repeat(Math.max(0, padding - 2)) +
                    BRIGHT_CYAN + BOLD + "║" + RESET);
        }

        System.out.println(BRIGHT_CYAN + BOLD + "  ╚" + "═".repeat(width - 2) + "╝" + RESET);
    }

    // ─── Dividers & Spacers ───────────────────────────────────────

    public static void printDivider() {
        System.out.println(DIM + CYAN + "  " + "─".repeat(DEFAULT_WIDTH) + RESET);
    }

    public static void printDoubleDivider() {
        System.out.println(BRIGHT_CYAN + "  " + "═".repeat(DEFAULT_WIDTH) + RESET);
    }

    public static void printBlankLine() {
        System.out.println();
    }

    // ─── Special: Goodbye ─────────────────────────────────────────

    public static void printGoodbye() {
        System.out.println();
        System.out.println(BRIGHT_CYAN + BOLD +
                "  ╔══════════════════════════════════════════════════╗" + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "  ║" + BRIGHT_YELLOW + "         Thank you for using LMS!               " + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "  ║" + DIM + WHITE + "            Goodbye & Happy Reading!              " + BRIGHT_CYAN + BOLD + "║"
                + RESET);
        System.out.println(BRIGHT_CYAN + BOLD +
                "  ╚══════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    // ─── JDBC Connection Status ───────────────────────────────────

    public static void printConnectionStatus(boolean connected) {
        if (connected) {
            System.out.println();
            System.out.println(BRIGHT_GREEN + BOLD + "  ┌──────────────────────────────────────┐" + RESET);
            System.out.println(BRIGHT_GREEN + BOLD + "  │   ✔ Database Connected Successfully  │" + RESET);
            System.out.println(BRIGHT_GREEN + BOLD + "  └──────────────────────────────────────┘" + RESET);
        } else {
            System.out.println();
            System.out.println(BRIGHT_RED + BOLD + "  ┌──────────────────────────────────────┐" + RESET);
            System.out.println(BRIGHT_RED + BOLD + "  │   ✖ Database Connection Failed       │" + RESET);
            System.out.println(BRIGHT_RED + BOLD + "  └──────────────────────────────────────┘" + RESET);
        }
    }

    // ─── Helper Methods ───────────────────────────────────────────

    /**
     * Center-aligns text within a given width (with ANSI codes in the text).
     */
    private static String centerText(String text, int width) {
        int visLen = visibleLength(stripAnsi(text));
        if (visLen >= width)
            return text;
        int leftPad = (width - visLen) / 2;
        int rightPad = width - visLen - leftPad;
        return " ".repeat(leftPad) + text + " ".repeat(rightPad);
    }

    /**
     * Center-aligns plain text within a given width.
     */
    private static String centerTextPlain(String text, int width) {
        if (text.length() >= width)
            return text.substring(0, width);
        int leftPad = (width - text.length()) / 2;
        int rightPad = width - text.length() - leftPad;
        return " ".repeat(leftPad) + text + " ".repeat(rightPad);
    }

    /**
     * Right-pads a string to a given width.
     */
    public static String padRight(String s, int width) {
        if (s == null)
            s = "";
        if (s.length() >= width)
            return s.substring(0, width);
        return s + " ".repeat(width - s.length());
    }

    /**
     * Returns the visible length of a string (ignoring ANSI escape codes).
     */
    private static int visibleLength(String s) {
        return stripAnsi(s).length();
    }

    /**
     * Strips ANSI escape codes from a string.
     */
    private static String stripAnsi(String s) {
        return s.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    /**
     * Gets a reasonable column width based on index and total columns.
     */
    private static int getColumnWidth(String value, int index, int totalColumns) {
        // Dynamic widths based on common data patterns
        if (totalColumns <= 2)
            return 25;
        if (totalColumns == 3)
            return 18;
        if (totalColumns == 4) {
            return switch (index) {
                case 0 -> 6; // ID
                case 1 -> 22; // Title/Name
                case 2 -> 15; // Author/ISBN
                case 3 -> 12; // Status
                default -> 15;
            };
        }
        if (totalColumns == 5) {
            return switch (index) {
                case 0 -> 6; // ID
                case 1 -> 20; // Title
                case 2 -> 15; // Author
                case 3 -> 13; // ISBN
                case 4 -> 10; // Status
                default -> 12;
            };
        }
        return 15;
    }

    /**
     * Enable Windows Terminal ANSI support (for older Windows versions).
     */
    public static void enableAnsiSupport() {
        // Modern Windows Terminal and VS Code terminal support ANSI natively.
        // This is a no-op but kept for compatibility.
    }
}
