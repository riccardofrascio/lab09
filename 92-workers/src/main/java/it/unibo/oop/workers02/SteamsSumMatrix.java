package it.unibo.oop.workers02;

import java.util.stream.Stream;

public class SteamsSumMatrix implements SumMatrix{
    
    private final int nthread;

    public SteamsSumMatrix(int n) {
        this.nthread = n;
    }

    @Override
    public double sum(double[][] matrix) {
        final int size = matrix.length / nthread + matrix.length % nthread;
        return Stream
                .iterate(0, start -> start+size)
                .limit(nthread)
                .parallel()
                .mapToDouble(start -> {
                    double result = 0;
                    for (int i = start; i < matrix.length && i < start + size; i++) {
                        for (final double d : matrix[i]) {
                            result += d;
                        }
                    }
                    return result;
                })
                .sum();
    }

}
