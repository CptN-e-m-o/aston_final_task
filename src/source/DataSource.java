package source;

import collection.CustomArrayList;

public interface DataSource<T> {
    CustomArrayList<T> getData(int count);
}