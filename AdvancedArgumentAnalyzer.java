import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Advanced Argument Analyzer
 * A comprehensive utility for processing, validating, and analyzing command-line arguments
 * with support for flags, options, help documentation, and advanced argument parsing.
 * 
 * Features:
 * - Argument validation and type checking
 * - Flag and option parsing (-flag, --option=value)
 * - Help documentation generation
 * - Argument statistics and analysis
 * - Error handling and user-friendly messages
 * 
 * @author Muhammad Yamman Hammad
 * @version 2.0
 * @since 2024
 */
public class AdvancedArgumentAnalyzer {
    
    // Constants for argument parsing
    private static final String SHORT_FLAG_PREFIX = "-";
    private static final String LONG_FLAG_PREFIX = "--";
    private static final String OPTION_SEPARATOR = "=";
    private static final String HELP_FLAG = "--help";
    private static final String VERSION_FLAG = "--version";
    
    // Application metadata
    private static final String APP_NAME = "Command Line Arguments Processor";
    private static final String APP_VERSION = "2.0";
    private static final String APP_AUTHOR = "Muhammad Yamman Hammad";
    
    // Storage for parsed arguments
    private static Map<String, String> options = new HashMap<>();
    private static Map<String, Boolean> flags = new HashMap<>();
    private static String[] positionalArgs;
    
    /**
     * Main method - Entry point of the application
     * @param args Command line arguments to process
     */
    public static void main(String[] args) {
        try {
            System.out.println("=".repeat(60));
            System.out.println("    " + APP_NAME + " v" + APP_VERSION);
            System.out.println("=".repeat(60));
            
            // Check for special flags first
            if (containsHelp(args)) {
                displayHelp();
                return;
            }
            
            if (containsVersion(args)) {
                displayVersion();
                return;
            }
            
            // Process and analyze arguments
            if (args.length == 0) {
                handleNoArguments();
            } else {
                processArguments(args);
                displayArgumentAnalysis(args);
                demonstrateArgumentFeatures(args);
            }
            
        } catch (Exception e) {
            System.err.println("Error processing arguments: " + e.getMessage());
            System.err.println("Use --help for usage information.");
        }
    }
    
    /**
     * Handles the case when no arguments are provided
     */
    private static void handleNoArguments() {
        System.out.println("📝 No command-line arguments provided.");
        System.out.println();
        System.out.println("💡 Usage Examples:");
        System.out.println("   java CommandLineArguments hello world");
        System.out.println("   java CommandLineArguments --verbose -n 5 --output=file.txt data.csv");
        System.out.println("   java CommandLineArguments --help");
        System.out.println();
        System.out.println("🔍 Try running with --help for detailed usage information.");
    }
    
    /**
     * Processes and parses command line arguments
     * @param args Array of command line arguments
     */
    private static void processArguments(String[] args) {
        System.out.println("🔄 Processing " + args.length + " command-line argument(s)...");
        System.out.println();
        
        // Parse arguments into different categories
        parseArguments(args);
        
        // Display basic argument information
        displayBasicArguments(args);
        
        // Display parsed flags and options
        displayParsedArguments();
    }
    
    /**
     * Parses arguments into flags, options, and positional arguments
     * @param args Array of command line arguments
     */
    private static void parseArguments(String[] args) {
        java.util.List<String> positional = new java.util.ArrayList<>();
        
        for (String arg : args) {
            if (arg.startsWith(LONG_FLAG_PREFIX)) {
                if (arg.contains(OPTION_SEPARATOR)) {
                    // Long option with value (--option=value)
                    String[] parts = arg.split(OPTION_SEPARATOR, 2);
                    options.put(parts[0].substring(2), parts[1]);
                } else {
                    // Long flag (--flag)
                    flags.put(arg.substring(2), true);
                }
            } else if (arg.startsWith(SHORT_FLAG_PREFIX)) {
                // Short flag (-f)
                flags.put(arg.substring(1), true);
            } else {
                // Positional argument
                positional.add(arg);
            }
        }
        
        positionalArgs = positional.toArray(new String[0]);
    }
    
    /**
     * Displays basic argument information
     * @param args Array of command line arguments
     */
    private static void displayBasicArguments(String[] args) {
        System.out.println("📋 Command-Line Arguments:");
        System.out.println("-".repeat(40));
        
        for (int i = 0; i < args.length; i++) {
            String argType = getArgumentType(args[i]);
            System.out.printf("  [%d] %-15s %s%n", i + 1, argType, args[i]);
        }
        System.out.println();
    }
    
    /**
     * Displays parsed flags and options
     */
    private static void displayParsedArguments() {
        if (!flags.isEmpty()) {
            System.out.println("🚩 Parsed Flags:");
            System.out.println("-".repeat(40));
            flags.forEach((flag, value) -> 
                System.out.printf("  %-15s : %s%n", flag, value ? "enabled" : "disabled"));
            System.out.println();
        }
        
        if (!options.isEmpty()) {
            System.out.println("⚙️  Parsed Options:");
            System.out.println("-".repeat(40));
            options.forEach((option, value) -> 
                System.out.printf("  %-15s : %s%n", option, value));
            System.out.println();
        }
        
        if (positionalArgs.length > 0) {
            System.out.println("📍 Positional Arguments:");
            System.out.println("-".repeat(40));
            for (int i = 0; i < positionalArgs.length; i++) {
                System.out.printf("  [%d] %s%n", i + 1, positionalArgs[i]);
            }
            System.out.println();
        }
    }
    
    /**
     * Displays comprehensive argument analysis
     * @param args Array of command line arguments
     */
    private static void displayArgumentAnalysis(String[] args) {
        System.out.println("📊 Argument Analysis:");
        System.out.println("-".repeat(40));
        
        // Count different types of arguments
        int flagCount = 0, optionCount = 0, positionalCount = 0;
        int totalLength = 0;
        String longestArg = "";
        String shortestArg = args.length > 0 ? args[0] : "";
        
        for (String arg : args) {
            totalLength += arg.length();
            
            if (arg.length() > longestArg.length()) {
                longestArg = arg;
            }
            if (arg.length() < shortestArg.length()) {
                shortestArg = arg;
            }
            
            if (arg.startsWith(LONG_FLAG_PREFIX) && arg.contains(OPTION_SEPARATOR)) {
                optionCount++;
            } else if (arg.startsWith(SHORT_FLAG_PREFIX) || arg.startsWith(LONG_FLAG_PREFIX)) {
                flagCount++;
            } else {
                positionalCount++;
            }
        }
        
        System.out.printf("  Total Arguments     : %d%n", args.length);
        System.out.printf("  Flags               : %d%n", flagCount);
        System.out.printf("  Options             : %d%n", optionCount);
        System.out.printf("  Positional Args     : %d%n", positionalCount);
        System.out.printf("  Average Length      : %.1f characters%n", 
                         args.length > 0 ? (double) totalLength / args.length : 0);
        System.out.printf("  Longest Argument    : \"%s\" (%d chars)%n", 
                         longestArg, longestArg.length());
        System.out.printf("  Shortest Argument   : \"%s\" (%d chars)%n", 
                         shortestArg, shortestArg.length());
        System.out.println();
    }
    
    /**
     * Demonstrates advanced argument processing features
     * @param args Array of command line arguments
     */
    private static void demonstrateArgumentFeatures(String[] args) {
        System.out.println("🔍 Advanced Features:");
        System.out.println("-".repeat(40));
        
        // Argument validation
        validateArguments(args);
        
        // Pattern matching
        findPatterns(args);
        
        // Data type detection
        detectDataTypes(args);
        
        System.out.println();
    }
    
    /**
     * Validates arguments for common patterns and issues
     * @param args Array of command line arguments
     */
    private static void validateArguments(String[] args) {
        int validEmails = 0, validUrls = 0, validNumbers = 0;
        
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
        Pattern urlPattern = Pattern.compile("^https?://[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(/.*)?$");
        Pattern numberPattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        
        for (String arg : args) {
            if (emailPattern.matcher(arg).matches()) validEmails++;
            if (urlPattern.matcher(arg).matches()) validUrls++;
            if (numberPattern.matcher(arg).matches()) validNumbers++;
        }
        
        if (validEmails > 0 || validUrls > 0 || validNumbers > 0) {
            System.out.println("  Validation Results:");
            if (validEmails > 0) System.out.printf("    📧 Valid Emails   : %d%n", validEmails);
            if (validUrls > 0) System.out.printf("    🌐 Valid URLs     : %d%n", validUrls);
            if (validNumbers > 0) System.out.printf("    🔢 Valid Numbers  : %d%n", validNumbers);
        }
    }
    
    /**
     * Finds common patterns in arguments
     * @param args Array of command line arguments
     */
    private static void findPatterns(String[] args) {
        Map<String, Integer> extensions = new HashMap<>();
        int upperCaseCount = 0, lowerCaseCount = 0, mixedCaseCount = 0;
        
        for (String arg : args) {
            // Count case patterns
            if (arg.equals(arg.toUpperCase()) && arg.matches(".*[A-Za-z].*")) {
                upperCaseCount++;
            } else if (arg.equals(arg.toLowerCase()) && arg.matches(".*[A-Za-z].*")) {
                lowerCaseCount++;
            } else if (arg.matches(".*[A-Za-z].*")) {
                mixedCaseCount++;
            }
            
            // Find file extensions
            if (arg.contains(".") && !arg.startsWith("-")) {
                String extension = arg.substring(arg.lastIndexOf("."));
                extensions.put(extension, extensions.getOrDefault(extension, 0) + 1);
            }
        }
        
        if (upperCaseCount > 0 || lowerCaseCount > 0 || mixedCaseCount > 0) {
            System.out.println("  Case Patterns:");
            if (upperCaseCount > 0) System.out.printf("    🔤 UPPERCASE      : %d%n", upperCaseCount);
            if (lowerCaseCount > 0) System.out.printf("    🔡 lowercase      : %d%n", lowerCaseCount);
            if (mixedCaseCount > 0) System.out.printf("    🔀 MixedCase      : %d%n", mixedCaseCount);
        }
        
        if (!extensions.isEmpty()) {
            System.out.println("  File Extensions:");
            extensions.forEach((ext, count) -> 
                System.out.printf("    📄 %-10s    : %d%n", ext, count));
        }
    }
    
    /**
     * Detects data types of arguments
     * @param args Array of command line arguments
     */
    private static void detectDataTypes(String[] args) {
        int integers = 0, decimals = 0, booleans = 0, strings = 0;
        
        for (String arg : args) {
            if (arg.startsWith("-")) continue; // Skip flags
            
            try {
                Integer.parseInt(arg);
                integers++;
            } catch (NumberFormatException e1) {
                try {
                    Double.parseDouble(arg);
                    decimals++;
                } catch (NumberFormatException e2) {
                    if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
                        booleans++;
                    } else {
                        strings++;
                    }
                }
            }
        }
        
        if (integers > 0 || decimals > 0 || booleans > 0 || strings > 0) {
            System.out.println("  Data Types:");
            if (integers > 0) System.out.printf("    🔢 Integers       : %d%n", integers);
            if (decimals > 0) System.out.printf("    💯 Decimals       : %d%n", decimals);
            if (booleans > 0) System.out.printf("    ✅ Booleans       : %d%n", booleans);
            if (strings > 0) System.out.printf("    📝 Strings        : %d%n", strings);
        }
    }
    
    /**
     * Determines the type of a command line argument
     * @param arg The argument to analyze
     * @return String describing the argument type
     */
    private static String getArgumentType(String arg) {
        if (arg.startsWith(LONG_FLAG_PREFIX)) {
            return arg.contains(OPTION_SEPARATOR) ? "(Long Option)" : "(Long Flag)";
        } else if (arg.startsWith(SHORT_FLAG_PREFIX)) {
            return "(Short Flag)";
        } else {
            return "(Positional)";
        }
    }
    
    /**
     * Checks if help flag is present in arguments
     * @param args Array of command line arguments
     * @return true if help flag is found
     */
    private static boolean containsHelp(String[] args) {
        return Arrays.asList(args).contains(HELP_FLAG) || Arrays.asList(args).contains("-h");
    }
    
    /**
     * Checks if version flag is present in arguments
     * @param args Array of command line arguments
     * @return true if version flag is found
     */
    private static boolean containsVersion(String[] args) {
        return Arrays.asList(args).contains(VERSION_FLAG) || Arrays.asList(args).contains("-v");
    }
    
    /**
     * Displays comprehensive help documentation
     */
    private static void displayHelp() {
        System.out.println("📖 " + APP_NAME + " - Help Documentation");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("DESCRIPTION:");
        System.out.println("  A comprehensive utility for processing and analyzing command-line");
        System.out.println("  arguments with support for flags, options, and advanced parsing.");
        System.out.println();
        System.out.println("USAGE:");
        System.out.println("  java CommandLineArguments [OPTIONS] [FLAGS] [ARGUMENTS...]");
        System.out.println();
        System.out.println("FLAGS:");
        System.out.println("  -h, --help          Show this help message and exit");
        System.out.println("  -v, --version       Show version information and exit");
        System.out.println("  -flag               Short flag (can be any name)");
        System.out.println("  --flag              Long flag (can be any name)");
        System.out.println();
        System.out.println("OPTIONS:");
        System.out.println("  --option=value      Long option with value");
        System.out.println("  --output=file.txt   Example: specify output file");
        System.out.println("  --count=10          Example: specify count value");
        System.out.println();
        System.out.println("EXAMPLES:");
        System.out.println("  java CommandLineArguments hello world");
        System.out.println("    → Process two positional arguments");
        System.out.println();
        System.out.println("  java CommandLineArguments --verbose -q --output=result.txt data.csv");
        System.out.println("    → Process flags, options, and positional arguments");
        System.out.println();
        System.out.println("  java CommandLineArguments user@email.com https://example.com 42 3.14");
        System.out.println("    → Analyze different data types and patterns");
        System.out.println();
        System.out.println("FEATURES:");
        System.out.println("  • Argument parsing and categorization");
        System.out.println("  • Data type detection and validation");
        System.out.println("  • Pattern recognition (emails, URLs, numbers)");
        System.out.println("  • Statistical analysis of arguments");
        System.out.println("  • File extension detection");
        System.out.println("  • Case pattern analysis");
        System.out.println();
    }
    
    /**
     * Displays version information
     */
    private static void displayVersion() {
        System.out.println("📦 " + APP_NAME);
        System.out.println("Version: " + APP_VERSION);
        System.out.println("Author: " + APP_AUTHOR);
        System.out.println("Year: 2025");
        System.out.println();
        System.out.println("A professional command-line argument processor written in Java.");
    }
}
