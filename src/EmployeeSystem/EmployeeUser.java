package EmployeeSystem;

public class EmployeeUser
{
    //employeeId, Name, Email, Address, PhoneNumber 
    private String employeeId;
    private String Name;
    private String Email;
    private String Address;
    private String PhoneNumber;

    public EmployeeUser(String employeeId, String name, String email, String address, String phoneNumber) 
    {
        this.employeeId = employeeId ;
        this.Name = name ;
        this.Email = email;
        this.Address = address;
        this.PhoneNumber = phoneNumber;
    }

     public String lineRepresentation()
     {
        return employeeId + ',' + Name + ',' + Email + ',' + Address + ',' + PhoneNumber;
     }

     public String getSearchKey()
     {
        return employeeId;
     }
}