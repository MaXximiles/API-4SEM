package fatec.grupodois.endurance.controller;

import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.exception.EmailExistException;
import fatec.grupodois.endurance.exception.EmailNotFoundException;
import fatec.grupodois.endurance.exception.ExceptionHandling;
import fatec.grupodois.endurance.exception.UserNotFoundException;
import fatec.grupodois.endurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends ExceptionHandling {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> fetchUserList() {
        List<User> users = userService.fetchAllUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.addUser(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/fetch/{userId}")
    public ResponseEntity<User> fetchUserById(@PathVariable("userId") Long userId) throws UserNotFoundException {
        User user = userService.fetchUserById(userId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/fetch-by-email/{userEmail}")
    public ResponseEntity<User> fetchUserByEmail(@PathVariable("userEmail") String userEmail) throws EmailNotFoundException {
        User user = userService.fetchUserByEmail(userEmail);


        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUsuario(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId,
                                              @RequestBody User user) {
        userService.updateUser(userId, user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
