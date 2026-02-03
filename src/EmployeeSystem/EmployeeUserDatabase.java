// EmployeeUserDatabase.java
package EmployeeSystem;

import Common.GenericDatabase;

public class EmployeeUserDatabase extends GenericDatabase<EmployeeUser> {

    public EmployeeUserDatabase(String filename) {
        super(filename);
    }

    @Override
    public EmployeeUser createRecordFrom(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            return null;
        }
        return new EmployeeUser(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                parts[4].trim()
        );
    }
}
