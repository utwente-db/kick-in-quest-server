package nl.utwente.db.kickinquest.server;

import java.util.ArrayList;
import java.util.List;

/*
 * A 'Node' represents a container in the state space tree
 * 
 * It holds the path of the cities, the level of the tree being computed
 * and a list of cities that should be skipped based on logical constraints 
 * (ie - you can't return to a previously visited city)
 * 
 * All variations of 'Nodes' inherit everything but 
 *              'getBound' - allows for comparison of different lower bound algorithms
 *              'compareTo' - allows for comparison of different sorting heuristics
 */
public abstract class Node {

        // tracks the cities that should be skipped
        protected boolean[] skip;

        // path of cities for this container in the state space tree
        protected List<Integer> cities;
        
        protected int level;
        protected int size;
        protected int[][] costMatrix;

        public Node() {
        }
        
        public Node(int[][] costMatrix) {
                this.size = costMatrix.length;
                this.costMatrix = costMatrix;
                this.cities = new ArrayList<Integer>();
                this.skip = new boolean[size + 1];
        }

        public void addCity(int city) {
                if (this.cities.size() > 0) {
                        
                        // does the path already contain the city (besides the first to complete the tour)
                        if (!this.cities.subList(1, this.cities.size()).contains(city)) {
                                cities.add(city);
                        }
                        else {
                                System.out.println("Cities already has: " + city);
                        }
                }
                else {
                        cities.add(city);
                }
        }

        public void addCities(List<Integer> cities) {
                for (Integer c : cities) {
                        this.addCity(c);
                }
        }

        public abstract int getBound();

        public int getLength() {

                int length = 0;
                for (int i = 1; i < this.cities.size(); i++) {
                        length += this.costMatrix[this.cities.get(i - 1)][this.cities.get(i)];
                }
                return length;
        }
        
        protected int minimum(int index) {
                int smallest = Integer.MAX_VALUE;
                for (int c = 0; c < this.size; c++) {
                        if (!skip[c]) {
                                if (c != index) {
                                        smallest = Math.min(smallest, this.costMatrix[index][c]);
                                }
                        }
                }
                // System.out.println("minimum(" + index + "): " + smallest);
                return smallest;
        }

        public List<Integer> getCities() {
                return cities;
        }

        public void setCities(List<Integer> cities) {
                this.cities = cities;
        }

        public int getLevel() {
                return level;
        }

        public void setLevel(int level) {
                this.level = level;
        }

        public boolean[] getSkip() {
                return skip;
        }

        public void setSkip(boolean[] skip) {
                this.skip = skip;
        }

        public int getSize() {
                return size;
        }

        public void setSize(int size) {
                this.size = size;
        }

        public int[][] getCostMatrix() {
                return costMatrix;
        }

        public void setCostMatrix(int[][] costMatrix) {
                this.costMatrix = costMatrix;
        }
}
