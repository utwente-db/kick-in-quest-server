package nl.utwente.db.kickinquest.server;


import java.util.GregorianCalendar;

public class CleverTSP extends TSP {

        public CleverTSP(int[][] costMatrix) {
                super(costMatrix);
        }

        @Override
        public void generateSolution() {

                int startingCity = 0;

                // first city
                Node first = new CleverNode(super.costMatrix);
                first.setLevel(0);
                first.addCity(startingCity);

                queue.add(first);

                while (!queue.isEmpty()) {

                        Node next = queue.first();
                        queue.remove(next);

                        if (next.getBound() < minimumLength) {

                                int level = next.getLevel() + 1;

                                for (int city = 1; city < super.size; city++) {

                                        Node n = new CleverNode(super.costMatrix);
                                        n.setLevel(level);

                                        if (!next.getCities().contains(city)) {

                                                n.addCities(next.getCities());
                                                n.addCity(city);

                                                if (n.getLevel() == (super.size - 2)) {

                                                        // get the city that's missing
                                                        for (int i = 0; i < super.size; i++) {
                                                                if (!n.getCities().contains(i)) {
                                                                        n.addCity(i);
                                                                }
                                                        }

                                                        // complete the tour
                                                        n.addCity(startingCity);

                                                        if (n.getLength() < super.minimumLength) {
                                                                // System.out.println("found shorter path: " + n.getLength());
                                                                super.minimumLength = n.getLength();
                                                                super.optimalTourLength = n.getLength();
                                                                super.optimalTour = n.getCities();
                                                        }
                                                }
                                                else {
                                                        if (n.getBound() < this.minimumLength) {
                                                                queue.add(n);
                                                        }
                                                        else {
                                                                // System.out.println("     skipping path: " + n.getBound());
                                                        }
                                                }
                                        }
                                }
                        }
                }

                this.end = new GregorianCalendar();
        }
}