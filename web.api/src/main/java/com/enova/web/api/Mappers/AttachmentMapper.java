package com.enova.web.api.Mappers;

import com.enova.web.api.Configures.ParameterConfig;
import com.enova.web.api.Models.Dtos.AttachmentDto;
import com.enova.web.api.Models.Entitys.Attachment;

public class AttachmentMapper {
    public static AttachmentDto mapToDto(Attachment attachment  ){
        String downloadURl = (attachment.getFileName()==null? null: ParameterConfig.host_ContextPath  +attachment.getId());
        return AttachmentDto.builder()
                .fileName(attachment.getFileName())
                .downloadURL(downloadURl)
                .fileType(attachment.getFileType())
                .fileSize(attachment.getFileSize())
                .build();
    }
}