import java.util.ArrayList;

public class MealMachine {

    private ArrayList<Meal> products;
    private final int rows = 6;
    private final int columns = 4;
    private String[] args1;

    /**
     * Slot array to store all slots
     */
    Slot[][] allSlots = new Slot[rows][columns];


    /**
     * Constructor for MealMachine
     * @param args1
     */
    public MealMachine(String[] args1) {
        products = new ArrayList<>();
        this.args1 = args1;

    }

    /**
     * Method to load the machine
     * @param filename input file
     */
    public void loadMachine(String filename) {
        String[] file = FileInput.readFile(filename, true, true);
        if (file != null) {
            outerloop2:
            for (String line : file) {
                String[] mealInfo = line.split("\t");
                String name = mealInfo[0];
                int price = Integer.parseInt(mealInfo[1]);
                String[] nutrientValues = mealInfo[2].split(" ");
                double protein = Double.parseDouble(nutrientValues[0]);
                double carbohydrate = Double.parseDouble(nutrientValues[1]);
                double fat = Double.parseDouble(nutrientValues[2]);
                Meal meal = new Meal(name, price, protein, carbohydrate, fat);

                /**
                 * adding products to machine slots
                 */
                outerloop:
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        if (allSlots[i][j] == null) {
                            allSlots[i][j] = new Slot(meal);
                            allSlots[i][j].increaseNumberOfProducts();
                            break outerloop;
                        } else if ((allSlots[i][j].getMeal().getName().equals( meal.getName())) ) {
                            products.add(meal);
                            if (allSlots[i][j].isCapacity()) {
                                allSlots[i][j].increaseNumberOfProducts();
                                break outerloop;
                            }
                        }
                        if (i == rows - 1 && j == columns - 1) {
                            if (products.contains(meal)) {
                                printMachine(args1[2], "INFO: There is no available place to put " + meal.getName() + "\n");
                            } else {
                                printMachine(args1[2], "INFO: There is no available place to put " + meal.getName() +"\n");
                                printMachine(args1[2], "INFO: The machine is full!\n");
                                break outerloop2;
                            }
                        }
                    }
                }
            }

            /**
             * printing the machine contents
             */
            printMachine(args1[2], "-----Gym Meal Machine-----\n");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (allSlots[i][j] != null)  {
                        printMachine(args1[2], allSlots[i][j].getMeal().getName() + "(" + allSlots[i][j].getMeal().getCalorie() + ", " + allSlots[i][j].getNumberOfProducts() + ")___");
                    } else {
                        printMachine(args1[2], "___(0, 0)___");
                    }
                    if (j == columns - 1){
                        printMachine(args1[2], "\n");
                    }
                }

            }
            printMachine(args1[2], "----------\n");
        }
    }



    /**
     * Method to purchase from the machine
     * @param filename input file
     */
    public void purchase(String filename) {
        // Implement purchasing from file
        String[] file = FileInput.readFile(filename, true, true);
        if (file != null) {
            for (String line : file) {
                String[] purchaseInfo = line.split("\t");
                String type = purchaseInfo[0];
                int money = parseMoney(purchaseInfo[1].split(" "));
                String choiceType = purchaseInfo[2];
                int value = Integer.parseInt(purchaseInfo[3]);
                switch (choiceType) {
                    case "PROTEIN":
                    case "CARB":
                    case "FAT":
                    case "CALORIE":
                        printMachine(args1[2], "INPUT: " + type + "\t" + purchaseInfo[1] + "\t" + choiceType + "\t" + value + "\n");
                        purchaseByNutritionalValue(choiceType, value, money);
                        break;
                    case "NUMBER":
                        printMachine(args1[2], "INPUT: " + type + "\t" + purchaseInfo[1] + "\t" + choiceType + "\t" + value + "\n");
                        purchaseBySlotNumber(value, money);
                        break;
                    default:
                        printMachine(args1[2], "INFO: Invalid choice type\n");
                }
            }

            /**
             * printing the machine contents at the end
             */
            printMachine(args1[2], "-----Gym Meal Machine-----\n");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if ((allSlots[i][j] != null) && (allSlots[i][j].getNumberOfProducts() > 0)) {
                        printMachine(args1[2], allSlots[i][j].getMeal().getName() + "(" + allSlots[i][j].getMeal().getCalorie() +
                                ", " + allSlots[i][j].getNumberOfProducts() + ")___");
                    } else {
                        printMachine(args1[2], "___(0, 0)___");
                    }
                    if (j == columns - 1){
                        printMachine(args1[2], "\n");
                    }
                }
            }
            printMachine(args1[2], "----------\n");

        }
    }

    /**
     * Method to purchase by slot number
     * @param value slot number
     * @param money user money
     * @return 1 if purchase is successful, -1 if not
     */
    private int purchaseBySlotNumber(double value, int money) {
        int i = (int) (value / columns);
        int j = (int) (value % columns);
        if (i < 0 || i >= rows || j < 0 || j >= columns) {
            printMachine(args1[2], "INFO: Number cannot be accepted. Please try again with another number.\n");
            printMachine(args1[2], "RETURN: Returning your change: " + money + " TL\n");
            return -1;
        }
        if (allSlots[i][j] == null) {
            printMachine(args1[2], "INFO: This slot is empty, your money will be returned.\n");
            printMachine(args1[2], "RETURN: Returning your change: " + money + " TL\n");
            return -1;
        } else {
            if ((money >= allSlots[i][j].getMeal().getPrice()) && (allSlots[i][j].getNumberOfProducts() > 0)) {
                printMachine(args1[2], "PURCHASE: You have bought one " + allSlots[i][j].getMeal().getName() + "\n");
                printMachine(args1[2], "RETURN: Returning your change: " + (money - allSlots[i][j].getMeal().getPrice()) + " TL\n");
                allSlots[i][j].decreaseNumberOfProducts();
                if (allSlots[i][j].getNumberOfProducts() == 0) {
                    allSlots[i][j] = null;

                }
                return 1;
            }
            else if (allSlots[i][j].getNumberOfProducts() == 0) {
                printMachine(args1[2], "INFO: This slot is empty, your money will be returned.\n");
                printMachine(args1[2], "RETURN: Returning your change: " + money + " TL\n");
                return -1;
            }
            else {
                printMachine(args1[2], "INFO: Insufficient money, try again with more money.\n");
                printMachine(args1[2], "RETURN: Returning your change: " + money + " TL\n");
                return -1;
            }

        }
    }


    /**
     * Method to purchase by nutritional value
     * @param choice protein, carb, fat, calorie
     * @param value nutritional value
     * @param money user money
     * @return 1 if purchase is successful, -1 if not
     */
    private int purchaseByNutritionalValue(String choice, int value, int money) {
        int a = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (choice.equals("PROTEIN")) {
                    if (allSlots[i][j] != null && Math.abs(allSlots[i][j].getMeal().getProtein() - value) <= 5) {
                        if (money < allSlots[i][j].getMeal().getPrice()) {
                            a = 1;
                            continue;
                        }
                        if (money >= allSlots[i][j].getMeal().getPrice() && allSlots[i][j].getNumberOfProducts() > 0) {
                            printMachine(args1[2], "PURCHASE: You have bought one " + allSlots[i][j].getMeal().getName() + "\n");
                            printMachine(args1[2], "RETURN: Returning your change: " + (money - allSlots[i][j].getMeal().getPrice()) + " TL\n");
                            allSlots[i][j].decreaseNumberOfProducts();
                            if (allSlots[i][j].getNumberOfProducts() == 0) {
                                allSlots[i][j] = null;
                            }
                            return 1;
                        }

                    }
                } else if (choice.equals("CARB")) {
                    if (allSlots[i][j] != null && Math.abs(allSlots[i][j].getMeal().getCarbohydrate() - value) <= 5) {
                        if (money < allSlots[i][j].getMeal().getPrice()) {
                            a = 1;
                            continue;
                        }
                        if (money >= allSlots[i][j].getMeal().getPrice() && allSlots[i][j].getNumberOfProducts() > 0) {
                            printMachine(args1[2], "PURCHASE: You have bought one " + allSlots[i][j].getMeal().getName() + "\n");
                            printMachine(args1[2], "RETURN: Returning your change: " + (money - allSlots[i][j].getMeal().getPrice()) + " TL\n");
                            allSlots[i][j].decreaseNumberOfProducts();
                            if (allSlots[i][j].getNumberOfProducts() == 0) {
                                allSlots[i][j] = null;
                            }
                            return 1;
                        }
                    }
                } else if (choice.equals("FAT")) {
                    if (allSlots[i][j] != null && Math.abs(allSlots[i][j].getMeal().getFat() - value) <= 5) {
                        if (money < allSlots[i][j].getMeal().getPrice()) {
                            continue;
                        }
                        if (money >= allSlots[i][j].getMeal().getPrice() && allSlots[i][j].getNumberOfProducts() > 0) {
                            printMachine(args1[2], "PURCHASE: You have bought one " + allSlots[i][j].getMeal().getName() + "\n");
                            printMachine(args1[2], "RETURN: Returning your change: " + (money - allSlots[i][j].getMeal().getPrice()) + " TL\n");
                            allSlots[i][j].decreaseNumberOfProducts();
                            if (allSlots[i][j].getNumberOfProducts() == 0) {
                                allSlots[i][j] = null;
                            }
                            return 1;
                        }
                    }
                } else if (choice.equals("CALORIE")) {
                    if (allSlots[i][j] != null && Math.abs(allSlots[i][j].getMeal().getCalorie() - value) <= 5) {
                        if (money < allSlots[i][j].getMeal().getPrice()) {
                            continue;
                        }
                        if (money >= allSlots[i][j].getMeal().getPrice() && allSlots[i][j].getNumberOfProducts() > 0) {
                            printMachine(args1[2], "PURCHASE: You have bought one " + allSlots[i][j].getMeal().getName() + "\n");
                            printMachine(args1[2], "RETURN: Returning your change: " + (money - allSlots[i][j].getMeal().getPrice()) + " TL\n");
                            allSlots[i][j].decreaseNumberOfProducts();
                            if (allSlots[i][j].getNumberOfProducts() == 0) {
                                allSlots[i][j] = null;
                            }
                            return 1;
                        }
                    }
                }
            }
        }
        if (a == 1) {
            printMachine(args1[2], "INFO: Insufficient money, try again with more money.\n");
            printMachine(args1[2], "RETURN: Returning your change: " + money + " TL\n");
            return -1;
        }
        printMachine(args1[2], "INFO: Product not found, your money will be returned.\n");
        printMachine(args1[2], "RETURN: Returning your change: " + money + " TL\n");
        return -1;
    }


    /**
     * Method to parse money
     * @param money user money
     * @return total money
     */
    private int parseMoney(String[] money) {
        int total = 0;
        for (int i = 0; i < money.length; i++) {
            if (Integer.parseInt(money[i]) != 1 && Integer.parseInt(money[i]) != 5 && Integer.parseInt(money[i]) != 10 &&
                    Integer.parseInt(money[i]) != 20 && Integer.parseInt(money[i]) != 50 && Integer.parseInt(money[i]) != 100 &&
                    Integer.parseInt(money[i]) != 200) {
                printMachine(args1[2], "INFO: Invalid money type, please try again with valid money types.\n");
            }
            total += Integer.parseInt(money[i]);
        }
        return total;
    }


    /**
     * Method to print the machine
     * @param fileName output file
     * @param text output text
     */
public void printMachine(String fileName, String text) {
        FileOutput.writeToFile( fileName, text, true, false);
}

}