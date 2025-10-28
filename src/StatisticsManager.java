import java.util.List;
import java.util.Map;

public class StatisticsManager {
    public static double calculateAverageGPA(List<student> students) {
        if (students == null || students.isEmpty()) {
            return 0.0;
        }
        double totalGPA = 0.0;
        for (student student : students) {
            totalGPA += student.getGpa();
        }
        return totalGPA / students.size();
    }

    public static student getHighestGPA(List<student> students) {
        if (students == null || students.isEmpty()) {
            return null;
        }
        student topStudent = students.get(0);
        for (student student : students) {
            if (student.getGpa() > topStudent.getGpa()) {
                topStudent = student;
            }
        }
        return topStudent;
    }

    public static student getLowestGPA(List<student> students)
    {
        if (students == null || students.isEmpty()) {
            return null;
        }
        student bottomStudent = students.get(0);
        for (student student : students) {
            if (student.getGpa() < bottomStudent.getGpa()) {
                bottomStudent = student;
            }
        }
        return bottomStudent;
    }

    public static Map<String, Integer> countByDepartment(List<student> students)
    {
        if (students == null || students.isEmpty()) {
            return new java.util.HashMap<>();  // Return empty map instead of null
        }
        Map<String, Integer> departmentCount = new java.util.HashMap<>();
        for (student student : students) {
            String dept = student.getDepartment();
            departmentCount.put(dept, departmentCount.getOrDefault(dept, 0) + 1);
        }
        return departmentCount;
    }
    
    public static List<student> sortByGPA(List<student> students, boolean ascending)
    {
        if (students == null || students.isEmpty()) {
            return null;
        }
        students.sort((s1, s2) -> {
            if (ascending) {
                return Double.compare(s1.getGpa(), s2.getGpa());
            } else {
                return Double.compare(s2.getGpa(), s1.getGpa());
            }
        });
        return students;
    }

    public static List<student> sortByName(List<student> students)
    {
        if (students == null || students.isEmpty()) {
            return null;
        }
        students.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        return students;
    }
}