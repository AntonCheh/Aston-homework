import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        ArrayClass<Integer> list = addElements();

        System.out.println("Размер листа после добавления элементов: " + list.size());
        print(list);

        int element = list.get(2);
        System.out.println("Получаем элемент по индексу: " + "\n" + element + "\n");

        list.addAll(Arrays.asList(4, 5, 6));
        System.out.println("Размер с коллекцией: " + list.size()); //
        print(list);

        System.out.println("Список до сортировки: ");
        print(list);
        list.sort(Comparator.naturalOrder());
        System.out.println("Список после сортировки:");
        print(list);

        System.out.println("Список после удаления: ");
        list.remove(5);
        print(list);

        list.clear();

        list.isEmpty();
    }

    public static ArrayClass<Integer> addElements() {
        ArrayClass<Integer> list = new ArrayClass<>();
        list.add(0, 10);
        list.add(1, 45);
        list.add(2, 88);
        list.add(3, 15);
        list.add(4, 29);
        list.add(5, 1);

        return list;
    }

    public static void print (ArrayClass<Integer> addElements){
        IntStream.range(0, addElements.size())
                .mapToObj(addElements::get)
                .forEach(e -> System.out.print(e + " "));
        System.out.println("\n");
    }

}