package utils.algo.ds.query.sparse_table;

import utils.algo.math.GCD;

import java.util.List;

public class GCDSparseTable<T extends Number> extends SparseTable<T> {

    public GCDSparseTable(List<T> itemList) {
        super(itemList);
    }

    @Override
    public T compute(T elem1, T elem2) {
        return GCD.of(elem1, elem2);
    }
}
