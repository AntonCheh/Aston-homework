import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class ArrayClass<T> {
    private static final int LENGTH = 10;
    private Object[] elements;
    private int size;

    public ArrayClass() {
        elements = new Object[LENGTH];
        size = 0;
    }

    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        guaranteedLength(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    public boolean addAll(Collection<? extends T> c) {
        Object[] array = c.toArray();
        int newLength = array.length;
        guaranteedLength(size + newLength);
        System.arraycopy(array, 0, elements, size, newLength);
        size += newLength;
        return newLength != 0;
    }

    public void clear() {
        elements = new Object[LENGTH];
        size = 0;
        System.out.println("Лист после очистки: " + size());
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) elements[index];
    }

    public int size() {
        return size;
    }

    public void isEmpty() {
        System.out.println(size == 0 ? EmptyOrNot.EMPTY : EmptyOrNot.HASDATA);
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T removedElement = (T) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
        return removedElement;
    }

    public void sort(Comparator<? super T> c) {
        T[] array = (T[]) Arrays.copyOf(elements, size);
        quickSort(array, 0, size - 1, c);
        System.arraycopy(array, 0, elements, 0, size);
    }

    private void quickSort(T[] array, int low, int high, Comparator<? super T> c) {
        if (low < high) {
            int pivotIndex = segment(array, low, high, c);
            quickSort(array, low, pivotIndex - 1, c);
            quickSort(array, pivotIndex + 1, high, c);
        }
    }

    private int segment(T[] array, int low, int high, Comparator<? super T> c) {
        T pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (c.compare(array[j], pivot) <= 0) {
                i++;
                changePlace(array, i, j);
            }
        }
        changePlace(array, i + 1, high);
        return i + 1;
    }

    private void changePlace(T[] array, int one, int two) {
        T temp = array[one];
        array[one] = array[two];
        array[two] = temp;
    }

    private void guaranteedLength(int minLength) {
        int oldCapacity = elements.length;
        if (minLength > oldCapacity) {
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            if (newCapacity < minLength) {
                newCapacity = minLength;
            }
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }
}
