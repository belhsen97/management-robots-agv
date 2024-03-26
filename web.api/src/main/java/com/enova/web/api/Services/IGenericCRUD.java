package com.enova.web.api.Services;
import java.util.List;

public interface IGenericCRUD<T,ID> {
    List<T> selectAll ();
    T  selectById (ID id);
    T insert( T object);
    //List<T> insertAll( List<T> object);
    T update( ID id , T object);
    void delete(  ID id );
    void deleteAll( );
}
