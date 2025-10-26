import java.io.IOException;
import java.util.List;

public class StudentManager 
{
    private List<student> students ;
    private IDataStorage dataStorage ;
    private int nextId ;

    public StudentManager(IDataStorage storage) throws IOException
    {
        this.dataStorage = storage ;
        this.students = dataStorage.loadData() ;
        this.nextId = generateNextId() ;
    }
    public boolean addStudent(student student) 
    {
        student.setStudentId(String.valueOf(nextId)) ;
        nextId++ ;
        students.add(student) ;
        try {
            dataStorage.saveData(students) ;
            return true ;
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
            return false ;
        }
    }
    public List<student> getAllStudents() 
    {
        return students ;
    }
    public boolean updateStudent(int id, student updatedStudent)
    {
        for (int i = 0; i < students.size(); i++) {
            student student = students.get(i);
            if (student.getStudentId().equals(String.valueOf(id))) {
                updatedStudent.setStudentId(String.valueOf(id)) ;
                students.set(i, updatedStudent);
                try {
                    dataStorage.saveData(students) ;
                    return true ;
                } catch (IOException e) {
                    System.out.println("Error saving data: " + e.getMessage());
                    return false ;
                }
            }
        }
        return false ;
    }
    public boolean deleteStudent(int id)
    {
        for (int i = 0; i < students.size(); i++) {
            student student = students.get(i);
            if (student.getStudentId().equals(String.valueOf(id))) {
                students.remove(i);
                try {
                    dataStorage.saveData(students) ;
                    return true ;
                } catch (IOException e) {
                    System.out.println("Error saving data: " + e.getMessage());
                    return false ;
                }
            }
        }
        return false ;
    }
    public student searchById(int id)
    {
        for (student student : students) {
            if (student.getStudentId().equals(String.valueOf(id))) {
                return student;
            }
        }
        return null ;
    }
    public List<student> searchByName(String name)
    {
        List<student> result = new java.util.ArrayList<>() ;
        for (student student : students) {
            if (student.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(student) ;
            }
        }
        return result ;
    }
    public List<student> searchByDepartment(String dept)
    {
        List<student> result = new java.util.ArrayList<>() ;
        for (student student : students) {
            if (student.getDepartment().toLowerCase().contains(dept.toLowerCase())) {
                result.add(student) ;
            }
        }
        return result ;
    }
    public int generateNextId() 
    {
        int maxId = 0;
        for (student student : students) {
            String idStr = student.getStudentId();
            try {
                int id = Integer.parseInt(idStr);
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Ignore non-integer IDs
            }
        }
        return maxId + 1;
    }
    private void loadStudentsFromFile()
    {
        try {
            this.students = dataStorage.loadData() ;
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
            this.students = new java.util.ArrayList<>() ;
        }
    }
    private void saveStudentsToFile()
    {
        try {
            dataStorage.saveData(students) ;
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    public boolean isIdExists(int id)
    {
        for (student student : students) {
            if (student.getStudentId().equals(String.valueOf(id))) {
                return true ;
            }
        }
        return false ;
    }
}
