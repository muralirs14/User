package ECommerce.User.Service.impl;

import ECommerce.User.Config.RestTemplateConfig;
import ECommerce.User.Dto.UserDto;
import ECommerce.User.Entity.User;
import ECommerce.User.Exception.ResourceNotFoundException;
import ECommerce.User.Payload.Address;
import ECommerce.User.Repository.UserRepository;
import ECommerce.User.Service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;//annotation based dependency injection
    private ModelMapper mapper;//constructor based dependency injection

    public UserServiceImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    private RestTemplateConfig restTemplate;

    private User mapToEntity(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        return user;
    }

    private UserDto mapToDto(User user) {
        UserDto userdto = mapper.map(user, UserDto.class);
        return userdto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> user = userRepository.findAll();
        return user.stream().map(users -> mapToDto(users)).collect(Collectors.toList());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = mapToEntity(userDto);
        User users = userRepository.save(user);
        UserDto response = mapToDto(users);
        return response;
    }

    @Override
    public UserDto getUserById(long userId) {
        User users = userRepository.findById(userId).orElseThrow(() -> new
                ResourceNotFoundException("user", "userId", userId));
        User users1 = userRepository.save(users);
        return mapToDto(users1);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
        mapper.map(userDto, user);
        User users = userRepository.save(user);
        return mapper.map(users, UserDto.class);
    }

    @Override
    public void deleteUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
        userRepository.delete(user);


    }

    @Override
    public UserDto getUserWithAddressByUserId(long userId) {
        User user = userRepository.findById(userId).get();

//        ArrayList address = restTemplate.getRestTemplate().getForObject("http://localhost:8084/api/address/" +
//                userId, ArrayList.class);
        Address addresses = restTemplate.getRestTemplate().getForObject("http://USER-SERVICE/api/address/" + userId, Address.class);
       // List<Address> addresses = restTemplate.getRestTemplate().getForObject("http://localhost:8084/api/address/" + userId, List.class);
        //it will return array list the url will return address


        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setMobile(user.getMobile());
        return userDto;

//        User user = userRepository.findById(userId).get();
//                .orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
//
//        User save = userRepository.save(user);
//        return mapToDto(save);
    }
}


