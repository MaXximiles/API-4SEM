package fatec.grupodois.endurance.service;

import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.exception.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {


    void addUser(User user) throws EmailExistException;

    User register(String FirstName, String LastName, String email, String Cpf) throws EmailNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, UserNotFoundException, MessagingException;

    User fetchUserById(Long userId) throws UserNotFoundException;

    User fetchUserByEmail(String userEmail) throws EmailNotFoundException;

    User findUserByEmail(String email);

    User findUserByCpf(String cpf);

    User findUserById(Long id);

    User addNewUser(String firstName, String lastName,
                    String email, String cpf, String role,
                    boolean isNonLocked, boolean isActive,
                    MultipartFile profileImage) throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException;

    User updateUser(String currentEmail, String firstName, String lastName,
                    String email, String cpf, String role,
                    boolean isNonLocked, boolean isActive,
                    MultipartFile profileImage) throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException;

    User updateProfileImage(String email, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException;

    /*username = email*/
    UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException;

    List<User> fetchAllUsers();

    void deleteUser(Long id);

    void resetPassword(String email) throws EmailNotFoundException, MessagingException;

    /*void updateUser(Long userId, User user);*/
}
