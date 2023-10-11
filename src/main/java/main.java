public class main {
    public static int numRooms = 40;

    public static void main(String[] args) {
        Cruise c = new Cruise("Burmuda Triangle");

        c.readCruiseRooms("cruise1.csv");

        c.printCruise();
    }
}