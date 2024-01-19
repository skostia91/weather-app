package by.shylau.weatherapp.mapper;

import by.shylau.weatherapp.dto.UserDTO;
import by.shylau.weatherapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "login", source = "login")
    @Mapping(target = "password", source = "password")
    User userDTOToUser(UserDTO userDTO);
}