package utils;

public class BaseUtil {
    public static Pair<Integer,Integer> getGradeClass(int number) {
        if (number < 1) {
            return null;
        }

        int n = number - 1;

        int grade = (n >> 2) + 1;

        int clazz = (n & 3) + 1;

        return new Pair<>(clazz,grade);
    }
}
