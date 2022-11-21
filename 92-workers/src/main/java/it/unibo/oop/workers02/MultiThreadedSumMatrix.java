package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix{

    private final int nthread;

    public MultiThreadedSumMatrix(int n) {
        this.nthread = n;
    }
    
    private static class Worker extends Thread{

        private double[][] matrix;
        private final int startpos;
        private final int nelem;
        private long res;
        /**
         * 
         * @param matrix matrix where to calculate
         * @param startpos starting position
         * @param nelem nelem processed
         */
        Worker(double[][] matrix, int startpos, int nelem) {
            this.matrix=matrix;
            this.startpos=startpos;
            this.nelem=nelem;
        }

        @Override
        public void run() {
            System.out.println("Working from pos "+startpos+" to pos "+(startpos+nelem-1));
            for(int i=startpos; i<matrix.length && i < startpos + nelem; i++) {
                for(double d : matrix[i]) { 
                    this.res+=d;
                }
            }
        }
        /**
         * 
         * @return res of his elem processed
         */
        double getResult() {
            return this.res;
        }
    }

    @Override
    public double sum(double[][] matrix) {
        final int size = matrix.length / nthread + matrix.length % nthread;
        final List<Worker> workers = new ArrayList<>(nthread);
        for(int start=0; start < matrix.length ; start+=size) {
            workers.add(new Worker(matrix, start, size));
        }
        for(var w : workers) {
            w.start();
        }
        double sum = 0;
        for(var w : workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }

        return sum;
    }
    
}
