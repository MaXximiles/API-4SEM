package fatec.grupodois.endurance.service;

import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.exception.EmailNotFoundException;
import fatec.grupodois.endurance.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {


    void addUser(User user);

    User fetchUserById(Long userId) throws UserNotFoundException;

    User fetchUserByEmail(String userEmail) throws EmailNotFoundException;

    UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException;

    List<User> fetchAllUsers();

    void deleteUser(Long userID);

    void updateUser(Long userId, User user);
}
