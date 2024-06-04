package ECommerce.User.Service;

import ECommerce.User.Dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto saveUser(UserDto userDto);

    UserDto getUserById(long userId);

    UserDto updateUser(UserDto userDto,long userId);

    void deleteUser(long userId);

    UserDto getUserWithAddressByUserId(long userId);
}
