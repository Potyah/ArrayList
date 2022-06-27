package arraylist;

import java.util.*;

public class ArrayList<T> implements List<T> {
    private T[] items;
    private int size;
    private int modCount;

    @SuppressWarnings("unchecked")
    public ArrayList() {
        items = (T[]) new Object[10];
    }

    public ArrayList(Collection<? extends T> c) {
        if (c == null) {
            throw new NullPointerException("The specified collection is null");
        }

        //noinspection unchecked
        items = (T[]) c.toArray();
        size = c.size();
    }

    public ArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("The specified initial capacity is negative");
        }

        //noinspection unchecked
        items = (T[]) new Object[initialCapacity];
    }

    public void trimToSize() {
        if (size < items.length) {
            items = Arrays.copyOf(items, size);
        }
    }

    public void ensureCapacity(int minCapacity) {
        if (minCapacity > items.length) {
            items = Arrays.copyOf(items, minCapacity);
        }
    }

    @Override
    public boolean add(T t) {
        add(size, t);

        return true;
    }

    @Override
    public void add(int index, T t) {
        rangeCheckForAdd(index);

        if (size >= items.length) {
            if (size > 0) {
                ensureCapacity(size * 2);
            } else {
                ensureCapacity(10);
            }
        }

        System.arraycopy(items, index, items, index + 1, size - index);
        items[index] = t;

        ++size;
        ++modCount;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addAll(size, c);
    }

    // Добавить все элементы collection в список начиная с индекса
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (c == null) {
            throw new NullPointerException("The specified collection is null");
        }

        if (c.isEmpty()) {
            return false;
        }

        rangeCheckForAdd(index);

        ensureCapacity(size + c.size());

        System.arraycopy(items, index, items, index + c.size(), size - index);

        int i = index;

        for (T t : c) {
            items[i] = t;

            ++i;
        }

        size += c.size();

        ++modCount;

        return c.size() != 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            items[i] = null;
        }

        size = 0;

        ++modCount;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("The specified collection is null");
        }

        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public T get(int index) {
        rangeCheck(index);

        return items[index];
    }

    @Override
    public T set(int index, T t) {
        rangeCheck(index);

        T result = items[index];
        items[index] = t;

        return result;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, items[i])) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(o, items[i])) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "{ }";
        }

        StringBuilder stringBuilder = new StringBuilder("{");

        for (int i = 0; i < size; i++) {
            stringBuilder.append(items[i]).append(", ");
        }

        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "}");

        return stringBuilder.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new MyListIterator();
    }

    private class MyListIterator implements Iterator<T> {
        private int currentIndex = -1;
        private final int iteratorModCount = modCount;

        public boolean hasNext() {
            return currentIndex + 1 < size;
        }

        public T next() {
            if (iteratorModCount != modCount) {
                throw new ConcurrentModificationException("The collection element is changed");
            }

            if (!hasNext()) {
                throw new NoSuchElementException("The collection element is ended");
            }

            ++currentIndex;

            return items[currentIndex];
        }
    }

    @Override
    public T remove(int index) {
        rangeCheck(index);

        T removedItem = items[index];

        if (index < size - 1) {
            System.arraycopy(items, index + 1, items, index, size - index - 1);
        }

        items[size - 1] = null;
        --size;

        ++modCount;

        return removedItem;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);

        if (index != -1) {
            remove(index);

            return true;
        }

        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("The specified collection is null");
        }

        if (c.isEmpty()) {
            return false;
        }

        boolean isResult = false;

        for (int i = 0; i < size; i++) {
            if (c.contains(items[i])) {
                remove(items[i]);
                i--;

                isResult = true;
            }
        }

        return isResult;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("The specified collection is null");
        }

        if (c.isEmpty()) {
            return false;
        }

        boolean isResult = false;

        for (int i = 0; i < size; i++) {
            if (!c.contains(items[i])) {
                remove(items[i]);
                i--;

                isResult = true;
            }
        }

        return isResult;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(items, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a == null) {
            throw new NullPointerException("The specified array is null");
        }

        if (a.length < size) {
            //noinspection unchecked
            return (T1[]) Arrays.copyOf(items, size, a.getClass());
        }

        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(items, 0, a, 0, size);

        if (a.length > size)
            a[size] = null;

        return a;
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of range, permissible value from 0 to " + (size - 1));
        }
    }

    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of range, permissible value from 0 to " + size);
        }
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }
}