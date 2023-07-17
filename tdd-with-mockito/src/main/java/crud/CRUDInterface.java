package crud;

import java.io.Serializable;

public interface CRUDInterface<T extends IdObject> {
	
    void create(T object);
    
    T read(String id);
    
    void update(String id, T object);
    
    void delete(String id);
}
