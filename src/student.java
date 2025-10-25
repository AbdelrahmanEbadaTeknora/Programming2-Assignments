public class student extends person {
    private String studentId;
    private String department;
    private double gpa;

    public student(String name,
            int age, String gender, String studentId,
            String department, double gpa) {
        super(name, age, gender);
        this.studentId = studentId;
        this.department = department;
        this.gpa = gpa;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getGpa() {
        return this.gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    @Override public String toString() 
    {
        return "Student{name='" + name + "', age=" + age +
                ", gender " + gender +
                ", studentId='" + studentId + "', department='" + department +
                "', gpa=" + gpa + "}";
    }

    public String toCSV() 
    {
        return name + "," + age + "," + gender + "," +
                studentId + "," + department + "," + gpa;  
    }

    public static student fromCSV(String csvLine) 
    {
        String[] parts = csvLine.split(",");
        String name = parts[0];
        int age = Integer.parseInt(parts[1]);
        String gender = parts[2];
        String studentId = parts[3];    
        String department = parts[4];
        double gpa = Double.parseDouble(parts[5]);  
        return new student(name, age, gender, studentId, department, gpa);
    }
}
