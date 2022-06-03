package edu.seb.controlled.user.mapper;

import edu.seb.controlled.user.entity.User;
import edu.seb.controlled.user.mapper.to.UserTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User fromTO(UserTO source);
    UserTO toTO(User source);
}
