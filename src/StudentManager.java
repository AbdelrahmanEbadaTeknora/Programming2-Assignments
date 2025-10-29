import java.io.IOException;
import java.util.*;

public class StudentManager implements ISearchable {
    private List<student> students;
    private IDataStorage dataStorage;
    public StudentManager(IDataStorage storage) throws IOException {
        this.dataStorage = storage;
        this.students = dataStorage.loadData();
    }

    public boolean addStudent(student student) {
        students.add(student);
        try {
            dataStorage.saveData(students);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
            return false;
        }
    }

    public List<student> getAllStudents() {
        return students;
    }

    public boolean updateStudent(int id, student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            student student = students.get(i);
            if (student.getStudentId().equals(String.valueOf(id))) {
                updatedStudent.setStudentId(String.valueOf(id));
                students.set(i, updatedStudent);
                try {
                    dataStorage.saveData(students);
                    return true;
                } catch (IOException e) {
                    System.out.println("Error saving data: " + e.getMessage());
                    return false;
                }
            }
        }
        return false;
    }

    public boolean deleteStudent(int id) {
        for (int i = 0; i < students.size(); i++) {
            student student = students.get(i);
            if (student.getStudentId().equals(String.valueOf(id))) {
                students.remove(i);
                try {
                    dataStorage.saveData(students);
                    return true;
                } catch (IOException e) {
                    System.out.println("Error saving data: " + e.getMessage());
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public student searchById(int id) {
        for (student student : students) {
            if (student.getStudentId().equals(String.valueOf(id))) {
                return student;
            }
        }
        return null;
    }

    public student searchByFormattedId(String id) {
        for (student student : students) {
            if (student.getStudentId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    @Override
    public List<student> searchByName(String name) {
        List<student> result = new ArrayList<>();
        for (student student : students) {
            if (student.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(student);
            }
        }
        return result;
    }

    @Override
    public List<student> searchByDepartment(String dept) {
        List<student> result = new ArrayList<>();
        for (student student : students) {
            if (student.getDepartment().toLowerCase().contains(dept.toLowerCase())) {
                result.add(student);
            }
        }
        return result;
    }

    public boolean isIdExists(String id) {
        for (student student : students) {
            if (student.getStudentId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}