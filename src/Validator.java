public class Validator 
{
    public static boolean isValidName(String name)
    {
        return name != null && !name.trim().isEmpty() && name.matches("[a-zA-Z ]+");
    }

    public static boolean isValidAge(int age)
    {
        return age >= 16 && age <= 100;
    }

    public static boolean isValidGender(String gender)
    {
        return gender != null && (gender.equalsIgnoreCase("Male") ||
                                     gender.equalsIgnoreCase("Female") );
    }

    public static boolean isValidStudentId(String studentId)
    {
        return studentId != null && studentId.matches("[A-Z]{2}[0-9]{4}");
    }

    public static boolean isValidGPA(double gpa)
    {
        return gpa >= 0.0 && gpa <= 4.0;
    }

    public static boolean isValidDepartment(String deptartment)
    {
        return (deptartment != null && !deptartment.trim().isEmpty());
    }

    public static String getErrorMessage(String field)
    {
        switch(field)
        {
            case "name":
                return "Name must be non-empty and contain only letters and spaces.";
            case "age":
                return "Age must be between 16 and 100.";
            case "gender":
                return "Gender must be either 'Male' or 'Female'.";
            case "studentId":   
                return "Student ID must be in format: two uppercase letters followed by four digits (e.g., AB1234).";
            case "gpa":
                return "GPA must be between 0.0 and 4.0.";
            case "department":
                return "Department must be non-empty.";
            default:
                return "Unknown field.";
        }
    }
}
