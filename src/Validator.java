public class Validator
{
    /**
     * Validates that name contains only letters and spaces (no numbers or special characters)
     */
    public static boolean isValidName(String name)
    {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // Only letters (Arabic, English) and spaces allowed
        return name.matches("[a-zA-Z\\u0600-\\u06FF ]+");
    }

    /**
     * Validates age is between 16 and 100
     */
    public static boolean isValidAge(int age)
    {
        return age >= 16 && age <= 100;
    }

    /**
     * Validates gender is Male or Female only
     */
    public static boolean isValidGender(String gender)
    {
        if (gender == null) {
            return false;
        }
        return gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female");
    }

    /**
     * NEW: Validates Student ID format
     * - Must be a number only (no letters or special characters)
     * - Must be at least 1 digit
     * - Can't be empty
     */
    public static boolean isValidStudentId(String studentId)
    {
        if (studentId == null || studentId.trim().isEmpty()) {
            return false;
        }
        // Only digits allowed, at least 1 digit
        return studentId.matches("\\d+");
    }

    /**
     * Validates GPA is between 0.0 and 4.0
     */
    public static boolean isValidGPA(double gpa)
    {
        return gpa >= 0.0 && gpa <= 4.0;
    }

    /**
     * Validates department name
     * - Not empty
     * - Only letters and spaces (no numbers)
     */
    public static boolean isValidDepartment(String department)
    {
        if (department == null || department.trim().isEmpty()) {
            return false;
        }
        // Only letters and spaces allowed
        return department.matches("[a-zA-Z\\u0600-\\u06FF ]+");
    }

    /**
     * Get user-friendly error message for each field
     */
    public static String getErrorMessage(String field)
    {
        switch(field.toLowerCase())
        {
            case "name":
                return "Name must contain only letters and spaces (no numbers or special characters).";
            case "age":
                return "Age must be between 16 and 100.";
            case "gender":
                return "Gender must be either 'Male' or 'Female'.";
            case "studentid":
            case "id":
                return "Student ID must be a number only (e.g., 1001, 1002). No letters or special characters allowed.";
            case "gpa":
                return "GPA must be between 0.0 and 4.0.";
            case "department":
                return "Department must contain only letters and spaces (no numbers or special characters).";
            default:
                return "Invalid input for field: " + field;
        }
    }


    public static String validateStudent(String id, String name, int age, String gender,
                                         String department, double gpa)
    {
        if (!isValidStudentId(id)) {
            return getErrorMessage("id");
        }
        if (!isValidName(name)) {
            return getErrorMessage("name");
        }
        if (!isValidAge(age)) {
            return getErrorMessage("age");
        }
        if (!isValidGender(gender)) {
            return getErrorMessage("gender");
        }
        if (!isValidDepartment(department)) {
            return getErrorMessage("department");
        }
        if (!isValidGPA(gpa)) {
            return getErrorMessage("gpa");
        }
        return null; // All valid
    }


    public static boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     */
    public static boolean isAlphabetic(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        return str.matches("[a-zA-Z\\u0600-\\u06FF ]+");
    }
}