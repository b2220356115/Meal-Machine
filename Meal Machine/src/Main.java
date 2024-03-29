public class Main {
    public static void main(String[] args) {
        MealMachine mealMachine = new MealMachine(args);
        mealMachine.loadMachine(args[0]);
        mealMachine.purchase(args[1]);
    }
}
