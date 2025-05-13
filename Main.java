import java.util.*;
import java.time.LocalDate;

/**
 * Core component for managing user authentication and storage using Singleton pattern.
 *
 * @author Your Name
 * @version 1.0
 * @see <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/classvars.html">Singleton Implementation</a>
 */
class UserRepository {
    private static UserRepository instance;
    private final Map<String, String> users = new HashMap<>();

    /**
     * Private constructor to prevent instantiation
     */
    private UserRepository() {}

    /**
     * Provides global access point to the singleton instance
     * @return Singleton UserRepository instance
     */
    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    /**
     * Checks if a username exists in the repository
     * @param username The username to check
     * @return true if username exists, false otherwise
     */
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    /**
     * Adds a new user to the repository
     * @param username Unique username
     * @param password User's password
     */
    public void addUser(String username, String password) {
        users.put(username, password);
    }

    /**
     * Authenticates user credentials
     * @param username Username to authenticate
     * @param password Password to verify
     * @return true if credentials match, false otherwise
     */
    public boolean authenticate(String username, String password) {
        return users.getOrDefault(username, "").equals(password);
    }
}

/**
 * Manages portfolio assets using Singleton pattern and Repository pattern
 *
 * @author Your Name
 * @version 1.0
 */
class PortfolioRepository {
    private static PortfolioRepository instance;
    private final List<Asset> assets = new ArrayList<>();

    private PortfolioRepository() {}

    /**
     * Provides global access point to the singleton instance
     * @return Singleton PortfolioRepository instance
     */
    public static synchronized PortfolioRepository getInstance() {
        if (instance == null) {
            instance = new PortfolioRepository();
        }
        return instance;
    }

    /**
     * Adds a new asset to the portfolio
     * @param asset Asset object to add
     */
    public void addAsset(Asset asset) {
        assets.add(asset);
    }

    /**
     * Removes an asset by name
     * @param name Name of the asset to remove
     * @return true if asset was removed, false otherwise
     */
    public boolean removeAsset(String name) {
        return assets.removeIf(a -> a.getName().equals(name));
    }

    /**
     * Retrieves all assets in the portfolio
     * @return List of assets (defensive copy)
     */
    public List<Asset> getAssets() {
        return new ArrayList<>(assets);
    }
}

/**
 * Manages user session state using Singleton pattern
 *
 * @author Your Name
 * @version 1.0
 */
class SessionManager {
    private static SessionManager instance;
    private boolean loggedIn = false;

    private SessionManager() {}

    /**
     * Provides global access point to the singleton instance
     * @return Singleton SessionManager instance
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Checks login status
     * @return true if user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Updates login status
     * @param loggedIn New authentication state
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}

/**
 * Represents a financial asset with immutable properties
 *
 * @author Your Name
 * @version 1.0
 */
class Asset {
    private final String name;
    private final double quantity;
    private final String type;
    private final LocalDate purchaseDate;
    private final double purchasePrice;

    /**
     * Constructs a new Asset
     * @param name Asset name
     * @param quantity Quantity owned
     * @param type Asset type (STOCKS/REAL_ESTATE/GOLD/CRYPTO)
     * @param purchaseDate Purchase date
     * @param purchasePrice Purchase price per unit
     */
    public Asset(String name, double quantity, String type,
                 LocalDate purchaseDate, double purchasePrice) {
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
    }

    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public String getType() { return type; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public double getPurchasePrice() { return purchasePrice; }
}

/**
 * Strategy interface for zakat calculation algorithms
 *
 * @author Your Name
 * @version 1.0
 */
interface ZakatCalculator {
    /**
     * Calculates zakat obligation for a portfolio
     * @param assets List of assets to consider
     * @return Total zakat amount due
     */
    double calculateZakat(List<Asset> assets);
}

/**
 * Standard zakat calculation implementation (2.5% of eligible assets)
 *
 * @author Your Name
 * @version 1.0
 */
class StandardZakatCalculator implements ZakatCalculator {
    private static final double ZAKAT_RATE = 0.025;

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculateZakat(List<Asset> assets) {
        return assets.stream()
                .filter(a -> !a.getType().equalsIgnoreCase("CRYPTO"))
                .mapToDouble(Asset::getPurchasePrice)
                .sum() * ZAKAT_RATE;
    }
}

/**
 * Command interface for user story implementations
 *
 * @author Your Name
 * @version 1.0
 */
interface UserStory {
    /**
     * Executes the user story workflow
     * @param scanner Input scanner for user interaction
     */
    void execute(Scanner scanner);

    /**
     * Indicates if authentication is required
     * @return true if login required, false otherwise
     */
    boolean requiresLogin();
}

// ===================== USER STORY IMPLEMENTATIONS ================

/**
 * US1: User Registration Implementation (Command Pattern)
 *
 * @author Your Name
 * @version 1.0
 */
class US1SignUp implements UserStory {
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d).{8,}$";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresLogin() { return false; }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n=== Sign Up ===");
        UserRepository userRepo = UserRepository.getInstance();

        String username = promptUsername(scanner, userRepo);
        String password = promptValidPassword(scanner);

        userRepo.addUser(username, password);
        System.out.println("Account created!");
    }

    private String promptUsername(Scanner scanner, UserRepository userRepo) {
        String username;
        do {
            System.out.print("Username: ");
            username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty!");
                continue;
            }
            if (userRepo.userExists(username)) {
                System.out.println("Username already exists!");
            }
        } while (username.isEmpty() || userRepo.userExists(username));
        return username;
    }

    private String promptValidPassword(Scanner scanner) {
        String password;
        do {
            System.out.print("Password (min 8 chars, 1 uppercase, 1 number): ");
            password = scanner.nextLine().trim();
            if (!password.matches(PASSWORD_REGEX)) {
                System.out.println("Invalid password format!");
            }
        } while (!password.matches(PASSWORD_REGEX));
        return password;
    }
}

/**
 * US2: User Login Implementation (Command Pattern)
 *
 * @author Your Name
 * @version 1.0
 */
class US2Login implements UserStory {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresLogin() { return false; }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n=== Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        UserRepository userRepo = UserRepository.getInstance();
        SessionManager session = SessionManager.getInstance();

        if (userRepo.authenticate(username, password)) {
            session.setLoggedIn(true);
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid credentials!");
        }
    }
}

/**
 * US3: Add Asset Implementation (Command Pattern)
 *
 * @author Your Name
 * @version 1.0
 */
class US3AddAsset implements UserStory {
    private static final Set<String> VALID_TYPES =
            Set.of("STOCKS", "REAL_ESTATE", "GOLD", "CRYPTO");

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresLogin() { return true; }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n=== Add Asset ===");
        PortfolioRepository portfolioRepo = PortfolioRepository.getInstance();

        String name = promptAssetName(scanner);
        double quantity = promptQuantity(scanner);
        String type = promptAssetType(scanner);
        LocalDate date = promptPurchaseDate(scanner);
        double price = promptPurchasePrice(scanner);

        portfolioRepo.addAsset(new Asset(name, quantity, type, date, price));
        System.out.println("Asset added!");
    }

    private String promptAssetName(Scanner scanner) {
        System.out.print("Asset Name: ");
        return scanner.nextLine().trim();
    }

    private double promptQuantity(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Quantity: ");
                double quantity = scanner.nextDouble();
                scanner.nextLine();
                if (quantity <= 0) throw new InputMismatchException();
                return quantity;
            } catch (InputMismatchException e) {
                System.out.println("Invalid quantity! Enter positive number.");
                scanner.nextLine();
            }
        }
    }

    private String promptAssetType(Scanner scanner) {
        String type;
        do {
            System.out.print("Type (STOCKS/REAL_ESTATE/GOLD/CRYPTO): ");
            type = scanner.nextLine().toUpperCase().trim();
            if (!VALID_TYPES.contains(type)) {
                System.out.println("Invalid asset type!");
            }
        } while (!VALID_TYPES.contains(type));
        return type;
    }

    private LocalDate promptPurchaseDate(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Purchase Date (YYYY-MM-DD): ");
                return LocalDate.parse(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid date format! Use YYYY-MM-DD.");
            }
        }
    }

    private double promptPurchasePrice(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Purchase Price: ");
                double price = scanner.nextDouble();
                scanner.nextLine();
                if (price <= 0) throw new InputMismatchException();
                return price;
            } catch (InputMismatchException e) {
                System.out.println("Invalid price! Enter positive number.");
                scanner.nextLine();
            }
        }
    }
}

// ... [Previous class implementations remain unchanged] ...

// ===================== USER STORY IMPLEMENTATIONS ================

// ... [Existing US1SignUp, US2Login, US3AddAsset implementations remain unchanged] ...

/**
 * US4: Remove Asset Implementation (Command Pattern)
 *
 * @author Your Name
 * @version 1.0
 */
class US4RemoveAsset implements UserStory {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresLogin() { return true; }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n=== Remove Asset ===");
        PortfolioRepository portfolioRepo = PortfolioRepository.getInstance();

        System.out.print("Enter asset name to remove: ");
        String name = scanner.nextLine().trim();

        boolean removed = portfolioRepo.removeAsset(name);
        System.out.println(removed ? "Asset removed successfully!" : "Asset not found!");
    }
}

/**
 * US8: Zakat Calculation Implementation (Command Pattern)
 *
 * @author Your Name
 * @version 1.0
 */
class US8CalculateZakat implements UserStory {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresLogin() { return true; }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n=== Zakat Calculation ===");
        PortfolioRepository portfolioRepo = PortfolioRepository.getInstance();
        ZakatCalculator calculator = new StandardZakatCalculator();

        double zakatAmount = calculator.calculateZakat(portfolioRepo.getAssets());
        System.out.printf("Total Zakat Due: $%.2f%n", zakatAmount);
    }
}

/**
 * US10: Bank Connection Implementation (Command Pattern)
 *
 * @author Your Name
 * @version 1.0
 */
class US10ConnectBank implements UserStory {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresLogin() { return true; }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n=== Connect Bank Account ===");

        System.out.print("Enter bank name: ");
        String bankName = scanner.nextLine().trim();

        System.out.print("Enter card number (16 digits): ");
        String cardNumber = scanner.nextLine().trim();

        System.out.print("Enter OTP (6 digits): ");
        String otp = scanner.nextLine().trim();

        if (cardNumber.matches("\\d{16}") && otp.matches("\\d{6}")) {
            System.out.println("Bank account connected successfully!");
        } else {
            System.out.println("Invalid input! Connection failed.");
        }
    }
}

/**
 * US11: Stock Connection Implementation (Command Pattern)
 *
 * @author Your Name
 * @version 1.0
 */
class US11ConnectStock implements UserStory {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresLogin() { return true; }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n=== Connect Stock Brokerage ===");

        System.out.print("Enter platform name (BINANCE/THNDR): ");
        String platform = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter API key: ");
        String apiKey = scanner.nextLine().trim();

        if (apiKey.length() >= 20 && Set.of("BINANCE", "THNDR").contains(platform)) {
            System.out.println("Stock account connected successfully!");
        } else {
            System.out.println("Invalid credentials! Connection failed.");
        }
    }
}

/**
 * US12: Report Export Implementation (Command Pattern)
 *
 * @author Your Name
 * @version 1.0
 */
class US12ExportReport implements UserStory {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresLogin() { return true; }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n=== Export Portfolio Report ===");
        PortfolioRepository portfolioRepo = PortfolioRepository.getInstance();

        System.out.print("Enter format (PDF/EXCEL): ");
        String format = scanner.nextLine().trim().toUpperCase();

        if (!Set.of("PDF", "EXCEL").contains(format)) {
            System.out.println("Invalid format selected!");
            return;
        }

        String report = generateReport(portfolioRepo.getAssets());
        String filename = "PortfolioReport." + format.toLowerCase();

        System.out.println("\nReport generated successfully!");
        System.out.println("File: " + filename);
        System.out.println(report);
    }

    private String generateReport(List<Asset> assets) {
        StringBuilder report = new StringBuilder();
        report.append("=== Portfolio Summary ===\n");
        report.append(String.format("Total Assets: %d%n", assets.size()));
        report.append(String.format("Total Value: $%.2f%n",
                assets.stream().mapToDouble(Asset::getPurchasePrice).sum()));
        report.append("\nAssets Details:\n");

        for (Asset asset : assets) {
            report.append(String.format("- %s: %s, Quantity: %.2f, Value: $%.2f%n",
                    asset.getName(),
                    asset.getType(),
                    asset.getQuantity(),
                    asset.getPurchasePrice()));
        }
        return report.toString();
    }
}

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<Integer, UserStory> USER_STORIES = createUserStories();

    private static Map<Integer, UserStory> createUserStories() {
        Map<Integer, UserStory> stories = new HashMap<>();
        stories.put(1, new US1SignUp());
        stories.put(2, new US2Login());
        stories.put(3, new US3AddAsset());
        stories.put(4, new US4RemoveAsset());
        stories.put(5, new US8CalculateZakat());
        stories.put(6, new US10ConnectBank());
        stories.put(7, new US11ConnectStock());
        stories.put(8, new US12ExportReport());
        return Collections.unmodifiableMap(stories);
    }

    /**
     * Main application entry point
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        displayWelcomeMessage();
        mainLoop();
    }

    private static void displayWelcomeMessage() {
        System.out.println("=== Investment Management System ===");
        System.out.println("===      Version 1.0 (2023)       ===");
    }

    private static void mainLoop() {
        while (true) {
            displayMainMenu();
            processUserChoice();
        }
    }

    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Sign Up");
        System.out.println("2. Login");
        System.out.println("3. Add Asset");
        System.out.println("4. Remove Asset");
        System.out.println("5. Calculate Zakat");
        System.out.println("6. Connect Bank");
        System.out.println("7. Connect Stock");
        System.out.println("8. Export Report");
        System.out.println("9. Exit");
        System.out.print("Choose an option: ");
    }

    private static void processUserChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 9) {
                handleExit();
            }
            handleUserStorySelection(choice);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number 1-9.");
            scanner.nextLine();
        }
    }

    private static void handleExit() {
        System.out.println("\nThank you for using the system. Goodbye!");
        System.exit(0);
    }

    private static void handleUserStorySelection(int choice) {
        UserStory userStory = USER_STORIES.get(choice);
        if (userStory == null) {
            System.out.println("Invalid choice! Please select 1-9.");
            return;
        }

        SessionManager session = SessionManager.getInstance();
        if (userStory.requiresLogin() && !session.isLoggedIn()) {
            System.out.println("Please login first!");
            return;
        }

        userStory.execute(scanner);
    }
}