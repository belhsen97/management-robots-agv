package com.enova.web.api.Services;

import com.enova.web.api.Entitys.Tag;
import com.enova.web.api.Entitys.Workstation;
import com.enova.web.api.Libs.IGenericCRUD;

import java.util.List;

public interface IWorkstationService extends IGenericCRUD<Workstation,String> {
    public Workstation selectByName(String name);
    public List<Tag> selectAllTags();
}
