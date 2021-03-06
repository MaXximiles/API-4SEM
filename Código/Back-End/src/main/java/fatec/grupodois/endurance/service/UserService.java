package fatec.grupodois.endurance.service;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.exception.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {


    User register( String firstName, String FirstName, String LastName, String email, String Cpf) throws EmailNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, UserNotFoundException, MessagingException;

    User fetchUserById(Long userId) throws UserNotFoundException;

    User findUserByEmail(String email);

    User findUserByCpf(String cpf);

    User findUserById(Long id);

    User addNewUser(String firstName, String lastName,
                    String email, String cpf, String role,
                    boolean isNonLocked, boolean isActive,
                    MultipartFile profileImage) throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException, MessagingException;

    User updateUser(String currentEmail, String firstName, String lastName,
                    String email, String cpf, String role,
                    boolean isNonLocked, boolean isActive,
                    MultipartFile profileImage,
                    String adminEmail) throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException;

    User updateProfileImage(String email, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException;

    User updateVaccineImage(String email, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, CpfExistException, IOException;

    List<User> fetchAllUsers();

    List<Evento> getUserParticipacoes(Long userId);

    void deleteUser(Long id);

    void changePassword(String email, String oldPassword, String newPassword) throws EmailNotFoundException, MessagingException, SenhaFormatoInvalidoException;

    User resetPasswordFront(String cpf) throws EmailNotFoundException, CpfNotFoundException, MessagingException;
}
