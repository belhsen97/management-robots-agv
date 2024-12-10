package tn.enova.Services;


import tn.enova.Models.Commons.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService  extends IGenericCRUD<Tag,String>{
    List<String> selectAllCode();
     Tag  selectByCode(String code);
}
