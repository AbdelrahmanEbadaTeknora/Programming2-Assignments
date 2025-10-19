package AdminRoleFunctions;
import EmployeeSystem.EmployeeUser;
import EmployeeSystem.EmployeeUserDatabase;
import java.util.ArrayList;

public class AdminRole {

     private EmployeeUserDatabase database;
     public AdminRole() {
        database = new EmployeeUserDatabase("employee.txt");
        database.readFromFile();
}
public void addEmployee(String employeeId, String name, String email, String address, String phoneNumber)
 {
      if (database.contains(employeeId)) 
      {
            System.out.println(" Employee ID " + employeeId + " exists");
            return;
      }
        EmployeeUser emp = new EmployeeUser(employeeId, name, email, address, phoneNumber);
        database.insertRecord(emp);
        System.out.println("Employee added");
    }
public void removeEmployee(String key)
{
    if (!database.contains(key)) 
    {
        System.out.println(" Employee ID " + key + " does not exist");
        return;
    }
    database.deleteRecord(key);
    System.out.println("Employee removed");
}
public EmployeeUser[] getListOfEmployees()
{
   ArrayList<EmployeeUser> employees = database.returnAllRecords();
    EmployeeUser[] arr = new EmployeeUser[employees.size()];
        employees.toArray(arr);
        return arr;
}
public void logout() 
{
    database.saveToFile();
}
}

