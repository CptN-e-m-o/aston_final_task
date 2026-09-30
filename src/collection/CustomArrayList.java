package collection;

import java.util.*;

public class CustomArrayList<T> implements List<T> {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 10;

    public CustomArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public boolean add(T element) {
        if (size == elements.length) grow();
        elements[size++] = element;
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }

    @SuppressWarnings ("unchecked")
    @Override 
    public T set(int index, T element) {
        checkIndex(index);
        T oldValue = (T) elements[index];
        elements[index] = element;
        return oldValue;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings ("unchecked")
    @Override 
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor = 0;
            public boolean hasNext() { return cursor < size; }
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (T) elements[cursor++];
            }
        };
    }

    private void grow() {
        int newCapacity = elements.length * 2;
        elements = Arrays.copyOf(elements, newCapacity);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер: " + size);
        }
    }

    @Override public boolean contains(Object o) { for (int i = 0; i < size; i++) if (Objects.equals(elements[i], o)) return true; return false; }
    @Override public Object[] toArray() { return Arrays.copyOf(elements, size); }
    @SuppressWarnings("unchecked")
    @Override
    public <E> E[] toArray(E[] a) {
        if (a.length < size) {
            return (E[]) Arrays.copyOf(elements, size, a.getClass());
        }

        System.arraycopy(elements, 0, a, 0, size);

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }
    @Override public boolean remove(Object o) { for (int i = 0; i < size; i++) { if (Objects.equals(elements[i], o)) { remove(i); return true; } } return false; }
    @Override public boolean containsAll(Collection<?> c) { for (Object e : c) if (!contains(e)) return false; return true; }
    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;

        for (T element : c) {
            add(element);
            modified = true;
        }

        return modified;
    }
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Индекс: " + index + ", Размер: " + size
            );
        }

        boolean modified = false;

        for (T element : c) {
            add(index, element);
            index++;
            modified = true;
        }

        return modified;
    }
    @Override public boolean removeAll(Collection<?> c) { boolean modified = false; for (Object o : c) while (remove(o)) modified = true; return modified; }
    @Override public boolean retainAll(Collection<?> c) { boolean modified = false; for (int i = 0; i < size; i++) if (!c.contains(elements[i])) { remove(i--); modified = true; } return modified; }
    @Override public void clear() { Arrays.fill(elements, 0, size, null); size = 0; }
    @Override public int indexOf(Object o) { for (int i = 0; i < size; i++) if (Objects.equals(elements[i], o)) return i; return -1; }
    @Override public int lastIndexOf(Object o) { for (int i = size - 1; i >= 0; i--) if (Objects.equals(elements[i], o)) return i; return -1; }
    @Override public ListIterator<T> listIterator() { throw new UnsupportedOperationException(); }
    @Override public ListIterator<T> listIterator(int index) { throw new UnsupportedOperationException(); }
    @Override public List<T> subList(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }
    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (size == elements.length) grow();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }
    @SuppressWarnings ("unchecked")
    @Override 
    public T remove(int index) {
        checkIndex(index);
        T removed = (T) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) System.arraycopy(elements, index + 1, elements, index, numMoved);
        elements[--size] = null;
        return removed;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof List<?> other)) {
            return false;
        }

        if (size != other.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (!Objects.equals(elements[i], other.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        for (int i = 0; i < size; i++) {
            Object element = elements[i];
            hashCode = 31 * hashCode + (element == null ? 0 : element.hashCode());
        }

        return hashCode;
    }
}
