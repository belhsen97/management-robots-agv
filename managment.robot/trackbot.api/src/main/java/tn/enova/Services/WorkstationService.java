package tn.enova.Services;

import tn.enova.Models.Entitys.Workstation;

public interface WorkstationService extends IGenericCRUD<Workstation,String> {
    public Workstation selectByName(String name);
}
