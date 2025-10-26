import java.util.List;

public interface ISearchable 
{
    public student searchById(int id);
    public List<student> searchByName(String name);
    public List<student> searchByDepartment(String dept);
}
