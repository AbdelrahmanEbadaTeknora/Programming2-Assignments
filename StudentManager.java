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
    // public boolean addStudent(student student) 
    // public List<Student> getAllStudents() - Return all
    // public boolean updateStudent(int id, Student updatedStudent)
    // public boolean deleteStudent(int id)
    // @Override public Student searchById(int id)
    // @Override public List<Student> searchByName(String name)
    // @Override public List<Student> searchByDepartment(String dept)
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
    // private void saveStudentsTo  File()
    // public boolean isIdExists(int id)
}
