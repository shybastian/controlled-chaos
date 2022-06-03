package edu.seb.controlled.core.mapper;

import edu.seb.controlled.core.entity.User;
import edu.seb.controlled.core.mapper.io.UserTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User fromTO(UserTO source);
    UserTO toTO(User source);
}
