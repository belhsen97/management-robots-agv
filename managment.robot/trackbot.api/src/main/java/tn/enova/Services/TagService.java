package tn.enova.Services;

import tn.enova.Models.Entitys.Tag;

public interface TagService extends IGenericCRUD<Tag,String> {
    Tag selectByCode(String code);
}
