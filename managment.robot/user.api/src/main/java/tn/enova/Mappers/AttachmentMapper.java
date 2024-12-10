package tn.enova.Mappers;

import tn.enova.Configures.ParameterConfig;
import tn.enova.Models.Commons.Attachment;
import tn.enova.Models.Dtos.AttachmentDto;

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