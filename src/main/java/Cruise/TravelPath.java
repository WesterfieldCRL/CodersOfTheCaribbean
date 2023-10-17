package Cruise;

import java.util.ArrayList;

public class TravelPath {
    private ArrayList<Destination> travelPath;

    public TravelPath()
    {
        travelPath = new ArrayList<>();
    }

    public void addDestination(String name, int travelTime)
    {
        Destination newDestination = new Destination(name, travelTime);
        travelPath.add(newDestination);
    }

    public ArrayList<Destination> getTravelPath()
    {
        return travelPath;
    }

    public static class Destination
    {
        private String name;
        private int travelTime;

        public Destination(String name, int travelTime)
        {
            this.name = name;
            this.travelTime = travelTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getDaysToNext() {
            return travelTime;
        }

        public void setDaysToNext(int daysToNext) {
            this.travelTime = daysToNext;
        }
    }
}
