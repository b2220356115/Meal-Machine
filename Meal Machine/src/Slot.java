public class Slot {
    private Meal meal;
    private int capacity = 10;
    private int numberOfProducts = 0;

    public Slot(Meal meal) {
        this.meal = meal;
    }

    public Meal getMeal() {
        return meal;
    }


    public boolean isCapacity(){
        if (capacity > numberOfProducts){
            return true;
        }
        return false;
    }

    public void increaseNumberOfProducts() {
        numberOfProducts++;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }
    public void decreaseNumberOfProducts() {
         numberOfProducts--;
    }


}
