package nl.utwente.db.kickinquest.server;

/*
 * A 'CleverNode' represents a container in the state space tree
 * 
 * It uses a 'promising' algorithm for computing a lower bound
 */
public class CleverNode extends Node implements Comparable<CleverNode> {

        public CleverNode(int[][] costMatrix) {
                super(costMatrix);
        }

        public int compareTo(CleverNode node) {

                if (this.cities.size() < node.getCities().size()) {
                        // System.out.println("t.cities.len < n.cities.len: " + this.cities.size() + ", " + node.getCities().size());
                        return 1;
                }
                else if (this.cities.size() > node.getCities().size()) {
                        // System.out.println("t.cities.len > n.cities.len: " + this.cities.size() + ", " + node.getCities().size());
                        return -1;
                }
                else if (this.cities.size() == node.getCities().size()) {

                        if (this.getBound() < node.getBound()) {
                                // System.out.println("t.bound < n.bound");
                                return -1;
                        }
                        else if (this.getBound() > node.getBound()) {
                                // System.out.println("t.bound > n.bound");
                                return 1;
                        }
                        else if (this.getBound() == node.getBound()) {
                                
                                // Add up the sum of the cities
                                int sumThis = 0;
                                for (Integer i : this.cities) {
                                        sumThis += i;
                                }

                                int sumOther = 0;
                                for (Integer i : node.getCities()) {
                                        sumOther += i;
                                }

                                if (sumThis == sumOther) {
                                        return 0;
                                }
                                else if (sumThis > sumOther) {
                                        // System.out.println("t.len > n.len");
                                        return 1;
                                }
                                else { // (sumThis < sumOther) {
                                        // System.out.println("t.len <= n.len");
                                        return -1;
                                }
                        }
                }
                return Integer.MAX_VALUE;
        }

        public boolean equals(CleverNode node) {
                return this.compareTo(node) == 0;
        }

        public int getBound() {

                int bound = 0;

                if (cities.size() == 1) {
                        for (int i = 0; i < this.size; i++) {
                                bound += minimum(i);
                        }
                }
                else {

                        for (int i = 1; i < cities.size(); i++) {
                                skip[cities.get(i - 1)] = true;
                                bound += this.costMatrix[cities.get(i - 1)][cities.get(i)];
                        }

                        skip[0] = true;
                        bound += minimum(cities.get(cities.size() - 1));

                        skip[0] = false;
                        skip[cities.get(cities.size() - 1)] = true;

                        for (int i = 1; i < this.size; i++) {
                                if (!skip[i]) {
                                        bound += minimum(i);
                                }
                        }
                }

                // System.out.println("bound (" + this.toString() + "): " + bound);
                return bound;
        }

}