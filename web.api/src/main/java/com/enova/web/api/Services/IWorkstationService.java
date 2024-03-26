package com.enova.web.api.Services;

import com.enova.web.api.Models.Entitys.Workstation;

public interface IWorkstationService extends IGenericCRUD<Workstation,String> {
    public Workstation selectByName(String name);
}
