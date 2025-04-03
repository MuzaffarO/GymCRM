package epam.gymcrm.mapper;

import epam.gymcrm.dto.user.UserDto;
import epam.gymcrm.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends AbstractMapper<User, UserDto> {
}
