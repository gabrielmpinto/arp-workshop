package com.mindera.arp_workshop.rxjava.util;

public class LessonResources {

    public static class ElevatorPassenger {
        private String mName;

        public int getWeightInPounds() {
            return mWeightInPounds;
        }

        public String getName() {
            return mName;
        }

        public int mWeightInPounds;

        public ElevatorPassenger(String name, int weightInPounds) {
            mName = name;
            mWeightInPounds = weightInPounds;
        }

        public String toString() {
            return "ElevatorPassenger{" +
                "mName='" + mName + '\'' +
                ", mWeightInPounds=" + mWeightInPounds +
                '}';
        }
    }

    //A Carnival Food Object...
    public static class CarnivalFood {
        private String mName;
        public Double mPrice;

        public CarnivalFood(String name, Double price) {
            mName = name;
            mPrice = price;
        }

        @Override
        public String toString() {
            return "Food{" +
                "mName='" + mName + '\'' +
                ", mPrice=" + mPrice +
                "\n}";
        }
    }


}
