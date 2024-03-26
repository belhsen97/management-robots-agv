package com.enova.web.api.Mappers;

import com.enova.web.api.Models.Dtos.AttachmentDto;
import com.enova.web.api.Models.Dtos.UserDto;
import com.enova.web.api.Models.Entitys.User;

public class UserMapper {
    public static User mapToEntity(UserDto userDto){
        return User.builder()
                .createdAt(userDto.getCreatedAt())
                .username(userDto.getUsername())
                .role(userDto.getRole())
                .enabled(userDto.isEnabled())
                .code(userDto.getCode())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .matricule(userDto.getMatricule())
                .phone(userDto.getPhone())
                .email(userDto.getEmail())
                .gender(userDto.getGender())
                .build();
    }
    public static UserDto mapToDto(User user){
        final AttachmentDto attachmentDto =  (user.getPhoto()==null ? null :  AttachmentMapper.mapToDto( user.getPhoto() ) );

        return UserDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .username(user.getUsername())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .code(user.getCode())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .matricule(user.getMatricule())
                .phone(user.getPhone())
                .email(user.getEmail())
                .gender(user.getGender())
                .photo(attachmentDto)
                .build();
    }
}