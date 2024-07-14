import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayClassTest {

    private ArrayClass<Integer> list;

    @BeforeEach
    void setUp() {
        list = new ArrayClass<>();
    }

    @Test
    void testAdd() {
        list.add(0, 10);
        list.add(1, 20);
        list.add(1, 15);
        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(15, list.get(1));
        assertEquals(20, list.get(2));
    }

    @Test
    void testAddAll() {
        List<Integer> elements = Arrays.asList(10, 20, 30);
        assertTrue(list.addAll(elements));
        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void testClear() {
        list.add(0, 10);
        list.add(1, 20);
        list.clear();
        assertEquals(0, list.size());
    }

    @Test
    void testGet() {
        list.add(0, 10);
        list.add(1, 20);
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }

    @Test
    void testSize() {
        list.add(0, 10);
        list.add(1, 20);
        assertEquals(2, list.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(list.size() == 0);
        list.add(0, 10);
        assertFalse(list.size() == 0);
    }

    @Test
    void testRemoveByIndex() {
        list.add(0, 10);
        list.add(1, 20);
        list.add(2, 30);
        assertEquals(20, list.remove(1));
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
    }

    @Test
    void testSort() {
        list.add(0, 30);
        list.add(1, 10);
        list.add(2, 20);
        list.sort(Comparator.naturalOrder());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }
}