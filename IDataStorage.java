import java.io.IOException;
import java.util.List;

public interface IDataStorage 
{
    public List<student> loadData() throws IOException ;
    public boolean saveData(List<student> students) throws IOException;
    public boolean deleteData() throws IOException;
    public boolean backupData() throws IOException;
}