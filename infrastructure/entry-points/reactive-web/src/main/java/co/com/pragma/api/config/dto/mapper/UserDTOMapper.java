package co.com.pragma.api.config.dto.mapper;

import co.com.pragma.api.config.dto.CreateUserDTO;
import co.com.pragma.api.config.dto.UserDTO;
import co.com.pragma.model.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    UserDTO toResponse(User user);

    List<UserDTO> toResponseList(List<User> users);

    User toModel(CreateUserDTO createUserDTO);

}
