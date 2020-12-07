package lesson7;

import kotlin.NotImplementedError;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     * <p>
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    // Время: O(N*M)
    // Память: S(N*M)
    public static String longestCommonSubSequence(String first, String second) {
        int firstLength = first.length();
        int secondLength = second.length();
        int[][] array = new int[firstLength][secondLength];
        StringBuilder result = new StringBuilder();
        for (int j = 0; j < secondLength; j++) {
            for (int i = 0; i < firstLength; i++) {
                // если элементы равны, то увеличиваем на 1 значение выше по диагонали
                if (first.charAt(i) == second.charAt(j)) {
                    if (i == 0 || j == 0) array[i][j] = 1;
                    else array[i][j] = array[i - 1][j - 1] + 1;
                } else {
                    // если не равны, то вычисляем максимум из элементов на 1 выше и на 1 левее
                    // (отдельные условия для левого и верхнего краев таблицы)
                    // делается это для того чтобы запоминать максимальную длину подпоследовательности
                    if (i == 0) {
                        if (j == 0) array[i][j] = 0;
                        else array[i][j] = array[i][j - 1];
                    } else if (j == 0) array[i][j] = array[i - 1][j];
                    else array[i][j] = Math.max(array[i - 1][j], array[i][j - 1]);
                }
            }
        }
        int i = firstLength - 1;
        int j = secondLength - 1;
        // заполняем результат с конца таблицы
        while (i >= 0 && j >= 0) {
            if (j > 0 && array[i][j] == array[i][j - 1]) j--;
            else if (i > 0 && array[i][j] == array[i - 1][j]) i--;
            else {
                if (i == 0 || j == 0) {
                    if (first.charAt(i) == second.charAt(j)) result.append(first.charAt(i));
                } else result.append(first.charAt(i));
                i--;
                j--;
            }
        }
        return result.reverse().toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     * <p>
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    // Время: O(N*LogN)
    // Память: S(N)
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        int size = list.size();
        if (size <= 1) return list;
        int[] minEndElements = new int[size + 1];
        // minEndElements[i] - наименьшее число, на которое оканчивается возрастающая подпоследовательность длины i
        int[] index = new int[size];
        // index[i] - индекс в массиве minEndElements для каждого i-того элемента из списка
        // устанавливаем начальные условия для бинарного поиска по массиву minEndElements
        minEndElements[0] = Integer.MIN_VALUE;
        for (int i = 1; i <= size - 1; i++) {
            minEndElements[i] = Integer.MAX_VALUE;
        }
        // массив хранит длину максимальной подпоследовательности
        int[] maxIndex = new int[size];
        for (int i = 0; i < size; i++) {
            // бинарный поиск элемента по массиву minEndElements
            int left = 0;
            int right = size;
            while (right != left + 1) {
                int mid = (left + right) / 2;
                if (minEndElements[mid] < list.get(i)) {
                    left = mid;
                } else right = mid;
            }
            minEndElements[right] = list.get(i);
            if (i == 0) maxIndex[i] = right - 1;
            else maxIndex[i] = Math.max(right - 1, maxIndex[i - 1]);
            index[i] = right - 1;
        }
        // запоминаем последний элемент который добавили в результат
        int last = Integer.MAX_VALUE;
        // запоминаем элемент который будем вносить в результат
        int element = last;
        // запоминаем индекс добавленного элемента, чтобы начать наш следующий поиск с него
        int iElement = 0;
        // запоминаем длину подпоследовательности которую ищем в следующем поиске
        int max = maxIndex[size - 1];
        List<Integer> subSequence = new ArrayList<>();
        for (int i = size - 1; i >= 0; i--) {
            if (index[i] == max) {
                if (list.get(i) < last) {
                    element = list.get(i);
                    iElement = i;
                }
                if (i == 0 || maxIndex[i - 1] != maxIndex[i]) {
                    // добавляем элемент и начинаем следующий поиск
                    subSequence.add(0, element);
                    last = element;
                    i = iElement;
                    max--;
                }
            }
        }
        return subSequence;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     * <p>
     * В файле с именем inputName задано прямоугольное поле:
     * <p>
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     * <p>
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     * <p>
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
