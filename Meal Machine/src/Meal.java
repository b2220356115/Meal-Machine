public class Meal {
    private String name;

    private int price;
    private double protein;
    private double carbohydrate;
    private double fat;
    private int calorie;


    public Meal(String name, int price, double protein, double carbohydrate, double fat) {
        this.name = name;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.price = price;
        calorie = (int) Math.round(4 * protein + 4 * carbohydrate + 9 * fat);
    }

    public String getName() {
        return name;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public double getFat() {
        return fat;
    }
    public int getCalorie() {
        return calorie;
    }

    public int getPrice() {
        return price;
    }


}
