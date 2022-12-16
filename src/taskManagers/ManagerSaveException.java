package taskManagers;

import java.io.IOException;

public class ManagerSaveException extends IOException {
    public ManagerSaveException(){
        super("File upload Error!");
    }
}
