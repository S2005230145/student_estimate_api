package utils;

import java.time.*;
import java.util.*;
import java.util.Calendar;

public class MonthDaysCalculator {

    public static void main(String[] args) {
        // 测试数据
        int year = 2024;
        int month = 2; // 2月

        System.out.println("=== 计算指定年月天数 ===");
        System.out.println("年份: " + year + "，月份: " + month);

        // 测试所有方法
        testAllMethods(year, month);

        // 测试边界情况
        testEdgeCases();

        // 测试闰年
        testLeapYears();
    }

    /**
     * 方法1: 使用Java 8的YearMonth（最推荐）
     */
    public static int getDaysByYearMonthJava8(int year, int month) {
        // 验证输入
        if (year < 1) {
            throw new IllegalArgumentException("年份必须大于0");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("月份必须在1-12之间");
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }

    /**
     * 方法2: 使用Calendar（传统方法）
     */
    public static int getDaysByYearMonthCalendar(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Calendar月份从0开始
        calendar.set(Calendar.DATE, 1); // 设置为第一天

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 方法3: 手动计算
     */
    public static int getDaysByYearMonthManual(int year, int month) {
        // 定义每个月的天数
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // 如果是2月，判断闰年
        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        }

        return daysInMonth[month - 1];
    }

    /**
     * 方法4: 使用LocalDate
     */
    public static int getDaysByLocalDate(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        return date.lengthOfMonth();
    }

    /**
     * 方法5: 一行代码解决方案
     */
    public static int getDaysOneLine(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    /**
     * 判断闰年
     */
    public static boolean isLeapYear(int year) {
        // 闰年规则：能被4整除但不能被100整除，或者能被400整除
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 测试所有方法
     */
    public static void testAllMethods(int year, int month) {
        System.out.println("\n=== 测试各种实现方法 ===");

        // 方法1: Java 8
        int days1 = getDaysByYearMonthJava8(year, month);
        System.out.println("1. Java 8 YearMonth: " + days1 + " 天");

        // 方法2: Calendar
        int days2 = getDaysByYearMonthCalendar(year, month);
        System.out.println("2. Calendar: " + days2 + " 天");

        // 方法3: 手动计算
        int days3 = getDaysByYearMonthManual(year, month);
        System.out.println("3. 手动计算: " + days3 + " 天");

        // 方法4: LocalDate
        int days4 = getDaysByLocalDate(year, month);
        System.out.println("4. LocalDate: " + days4 + " 天");

        // 方法5: 一行代码
        int days5 = getDaysOneLine(year, month);
        System.out.println("5. 一行代码: " + days5 + " 天");

        // 验证结果是否一致
        boolean allSame = (days1 == days2 && days2 == days3 && days3 == days4 && days4 == days5);
        System.out.println("所有方法结果一致: " + allSame);
    }

    /**
     * 测试边界情况
     */
    public static void testEdgeCases() {
        System.out.println("\n=== 边界情况测试 ===");

        // 测试1月
        System.out.println("2023年1月: " + getDaysByYearMonthJava8(2023, 1) + " 天");

        // 测试12月
        System.out.println("2023年12月: " + getDaysByYearMonthJava8(2023, 12) + " 天");

        // 测试无效月份
        try {
            getDaysByYearMonthJava8(2023, 13);
        } catch (IllegalArgumentException e) {
            System.out.println("无效月份测试: " + e.getMessage());
        }

        // 测试无效年份
        try {
            getDaysByYearMonthJava8(0, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("无效年份测试: " + e.getMessage());
        }
    }

    /**
     * 测试闰年
     */
    public static void testLeapYears() {
        System.out.println("\n=== 闰年测试 ===");

        int[] leapYears = {2000, 2004, 2008, 2012, 2016, 2020, 2024};
        int[] nonLeapYears = {1900, 2100, 2001, 2002, 2003, 2005};

        System.out.println("闰年2月天数:");
        for (int year : leapYears) {
            int days = getDaysByYearMonthJava8(year, 2);
            System.out.println(year + "年: " + days + " 天");
        }

        System.out.println("\n非闰年2月天数:");
        for (int year : nonLeapYears) {
            int days = getDaysByYearMonthJava8(year, 2);
            System.out.println(year + "年: " + days + " 天");
        }
    }
}
