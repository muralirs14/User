package ECommerce.User.Controller;

import ECommerce.User.Dto.UserDto;
import ECommerce.User.Service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
@GetMapping("/getAll")
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }
    @PostMapping("/saveUser")
    public ResponseEntity<Object> saveUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult){
    if(bindingResult.hasErrors()){
        return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(userService.saveUser(userDto),HttpStatus.CREATED);

    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable (name="userId") long userId){
    return  ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,@PathVariable (name="userId") long userId){
    UserDto response=userService.updateUser(userDto,userId);
    return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable (name="userId") long userId){
    userService.deleteUser(userId);
    return new ResponseEntity<>("User deleted Successfully....",HttpStatus.OK);
    }

    @GetMapping("adr/{userId}")
    @CircuitBreaker(name="ratingUser",fallbackMethod ="userFallback" )

    public ResponseEntity<UserDto> getUserWithAddressByUserId(@PathVariable(name="userId")long userId){
   UserDto userDto= userService.getUserWithAddressByUserId(userId);
    return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    public List<UserDto> userFallback(@PathVariable (name="userId")long userId,Exception e){
        System.out.println
                ("Fallback method is excecuted because service is down : "+e.getMessage());
        e.printStackTrace();
        List<UserDto> response=new ArrayList<>();
        UserDto userDto=new UserDto();
        userDto.setFirstName("Service down");
        userDto.setLastName("Service down");

        return response;
    }


}
