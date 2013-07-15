package nl.utwente.db.kickinquest.server;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeSet;

public abstract class TSP {

        protected int size;

        // used for empirical analysis
        protected GregorianCalendar start;
        protected GregorianCalendar end;
        
        protected int optimalTourLength;
        protected List<Integer> optimalTour;

        protected int[][] costMatrix;

        protected int minimumLength = Integer.MAX_VALUE;

        protected TreeSet<Node> queue;

        public TSP(int[][] costMatrix) {
                
                this.start = new GregorianCalendar();
                
                this.costMatrix = costMatrix;
                this.size = costMatrix.length;
                this.optimalTour = new ArrayList<Integer>();
                this.queue = new TreeSet<Node>();
        }
        
        public abstract void generateSolution();

        public void printCostMatrix(int[][] costMatrix) {

                StringBuffer matrix = new StringBuffer();
                matrix.append(System.getProperty("line.separator"));

                StringBuffer header = new StringBuffer("      ");
                StringBuffer separator = new StringBuffer("     -");
                for (int i = 0; i < costMatrix.length; i++) {
                        header.append(String.format("%1$#3s", i));
                        separator.append("---");
                }

                matrix.append(header);
                matrix.append(System.getProperty("line.separator"));
                matrix.append(separator);
                matrix.append(System.getProperty("line.separator"));

                StringBuffer rows = new StringBuffer();
                for (int i = 0; i < costMatrix.length; i++) {
                        rows.append(String.format("%1$#3s ", i));
                        rows.append(" | ");
                        for (int j = 0; j < costMatrix[i].length; j++) {
                                if (i != j) {
                                        rows.append(String.format("%1$#3s ", costMatrix[i][j]));
                                }
                                else {
                                        rows.append("  -");
                                }
                        }
                        rows.append(System.getProperty("line.separator"));
                }

                matrix.append(rows);
                System.out.println(matrix);
        }

        public int getOptimalTourLength() {
                return optimalTourLength;
        }

        public void setOptimalTourLength(int optimalTourLength) {
                this.optimalTourLength = optimalTourLength;
        }

        public List<Integer> getOptimalTour() {
                return optimalTour;
        }

        public String printOptimalTour() {

                StringBuffer sb = new StringBuffer();
                sb.append("[" + this.getOptimalTour().get(0) + "]");
                sb.append(" --" + this.costMatrix[this.getOptimalTour().get(0)][this.getOptimalTour().get(1)] + "--> ");
                for (int i = 1; i < this.getOptimalTour().size() - 1; i++) {
                        sb.append("[" + this.getOptimalTour().get(i) + "] --" + this.costMatrix[this.getOptimalTour().get(i)][this.getOptimalTour().get(i + 1)] + "--> ");
                }
                sb.append("[" + this.getOptimalTour().get(0) + "]");

                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));

                StringBuffer chartData = new StringBuffer();
                for (Integer i : this.getOptimalTour()) {
                        chartData.append(i + "-%3E");
                }
                // sb.append("For an experimental graph (without costs), enter the following URL in your browser");
                // sb.append(System.getProperty("line.separator"));
                // sb.append("http://chart.apis.google.com/chart?cht=gv:dot&chl=digraph{" + chartData.substring(0, chartData.length() - 4) + "}");
                return sb.toString();
        }
        
        public long getElapsedTime() {
                return this.end.getTimeInMillis() - this.start.getTimeInMillis();
        }
}
