package fatec.grupodois.endurance.service.impl;

import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.entity.UserPrincipal;
import fatec.grupodois.endurance.enumeration.Role;
import fatec.grupodois.endurance.exception.*;
import fatec.grupodois.endurance.repository.UserRepository;
import fatec.grupodois.endurance.service.EmailService;
import fatec.grupodois.endurance.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static fatec.grupodois.endurance.constant.FileConstant.*;
import static fatec.grupodois.endurance.constant.UserImplConstant.*;
import static fatec.grupodois.endurance.enumeration.Role.ROLE_GUEST;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@Transactional
@Qualifier("UserService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public User register(String firstName, String lastName, String email, String cpf, String password) throws
            EmailExistException,
            CpfExistException, CpfNotFoundException, UserNotFoundException {
        validateNewCpfAndEmail(EMPTY, email, cpf);

        String encodedPassword = encodePassword(password);

        User user = User
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .cpf(cpf)
                .joinDate(new Date())
                .password(encodedPassword)
                .isActive(true)
                .isNotLocked(true)
                .role(ROLE_GUEST.name())
                .authorities(ROLE_GUEST.getAuthorities())
                .profileImageUrl(getTemporaryProfileImageUrl(email))
                .build();

        LOGGER.info("User>>>>" + user.toString());
        LOGGER.info("New user password>>>>" + password);
        userRepository.save(user);
        return user;
    }

    @Override
    public User addNewUser(String firstName, String lastName,
                           String email, String cpf,
                           String role, boolean isNonLocked,
                           boolean isActive, MultipartFile profileImage)
            throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException {

        validateNewCpfAndEmail(EMPTY, email, cpf);

        String password = generatePassword();
        String encodedPassword = encodePassword(password);

        User user = User
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .cpf(cpf)
                .joinDate(new Date())
                .password(encodedPassword)
                .isActive(isActive)
                .isNotLocked(isNonLocked)
                .role(getRoleEnumName(role).name())
                .authorities(getRoleEnumName(role).getAuthorities())
                .profileImageUrl(getTemporaryProfileImageUrl(email))
                .build();

        LOGGER.info("User>>>>" + user.toString());
        LOGGER.info("New user password>>>>" + password);
        LOGGER.info("IMAGE " + user.getProfileImageUrl());
        userRepository.save(user);
        if(profileImage != null) {
            saveProfileImage(user, profileImage);
        }
        return user;
    }

    @Override
    public User fetchUserById(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) {
            LOGGER.error("User com id " + userId + " não encontrado.");
            throw new UserNotFoundException("Usuário " +
                    "com id " +
                    userId +
                    " não encontrado.");
        }

        return userRepository.findById(userId).get();
    }

    @Override
    public User findUserByEmail(String email) { return userRepository.findUserByEmail(email); }

    @Override
    public User findUserByCpf(String cpf) { return userRepository.findUserByCpf(cpf); }

    @Override
    public User findUserById(Long id) { return userRepository.findUserById(id); }

    @Override
    public User updateUser(String currentEmail, String newFirstName,
                           String newLastName, String newEmail,
                           String cpf, String role,
                           boolean isNonLocked, boolean isActive, MultipartFile profileImage,
                           String adminEmail)
            throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException {

        User currentUser = validateNewCpfAndEmail(currentEmail, newEmail, cpf);
        boolean flag = false;
        if(adminEmail!=null) {
            User isAdmin = userRepository.findUserByEmail(adminEmail);
            if(isAdmin.getRole().equals("ROLE_ADMIN")) {
                flag = true;
            }
        }

        if(StringUtils.isNotEmpty(newFirstName) &&
                StringUtils.isNotBlank(newFirstName) &&
                !currentUser.getFirstName().equalsIgnoreCase(newFirstName)) {

            currentUser.setFirstName(newFirstName);
        }

        if(StringUtils.isNotEmpty(newLastName) &&
                StringUtils.isNotBlank(newLastName) &&
                !currentUser.getLastName().equalsIgnoreCase(newLastName)) {

            currentUser.setLastName(newLastName);
        }

        if(StringUtils.isNotEmpty(newEmail) &&
                StringUtils.isNotBlank(newEmail) &&
                !currentUser.getEmail().equalsIgnoreCase(newEmail)) {
            currentUser.setEmail(newEmail);
        }

        if(isActive != currentUser.isActive()) {
            currentUser.setActive(isActive);
        }

        if(isNonLocked != currentUser.isNotLocked()) {
            currentUser.setNotLocked(isNonLocked);
        }

        if(flag) {
            currentUser.setRole(getRoleEnumName(role).name());
            currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        }

        userRepository.save(currentUser);
        saveProfileImage(currentUser, profileImage);

        return currentUser;
    }

    @Override
    public User updateProfileImage(String email, MultipartFile profileImage)
            throws UserNotFoundException, EmailExistException,
            CpfExistException, IOException {

        User user = validateNewCpfAndEmail(email, null, null);
        saveProfileImage(user, profileImage);
        return user;
    }

    @Override
    public User updateVaccineImage(String email, MultipartFile vaccineImage)
            throws UserNotFoundException, EmailExistException,
            CpfExistException, IOException {

        User user = validateNewCpfAndEmail(email, null, null);
        saveVaccineImage(user, vaccineImage);
        return user;
    }

    @Override
    public List<User> fetchAllUsers() { return userRepository.findAll(); }

    @Override
    public void deleteUser(Long usuarioID) { userRepository.deleteById(usuarioID); }

    @Override
    public void resetPassword(String email) throws EmailNotFoundException, MessagingException {

        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(USER_NOT_FOUND_BY_EMAIL + email);
        }

        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        emailService.sendNewPasswordEmail(user.getFirstName(), password, email);
    }

    @Override
    public User resetPasswordFront(String cpf) throws CpfNotFoundException, MessagingException {
        User user = userRepository.findUserByCpf(cpf);
        if (user == null) {
            throw new CpfNotFoundException(USER_NOT_FOUND_BY_CPF + cpf);
        }

        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        emailService.sendNewPasswordEmail(user.getFirstName(), password, user.getEmail());
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> userOpt = userRepository.findByEmail(email);

        if(userOpt.isEmpty()) {
            LOGGER.error(USER_NOT_FOUND_BY_EMAIL + email);
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_EMAIL + email);
        }

        User user = userRepository.findByEmail(email).get();
        user.setLastLoginDateDisplay(user.getLastLoginDate());
        user.setLastLoginDate(new Date());
        userRepository.save(user);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        LOGGER.info("Returning found user by email: " + email);

        return userPrincipal;
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException {
        if(profileImage != null) {
            Path userFolder = Paths.get(USER_FOLDER + user.getEmail()).toAbsolutePath().normalize();
            if(!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getEmail() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(),
                    userFolder.resolve(user.getEmail() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getEmail()));
            userRepository.save(user);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private void saveVaccineImage(User user, MultipartFile vaccineImage) throws IOException {
        if(vaccineImage != null) {
            Path userFolder = Paths.get(USER_FOLDER + user.getEmail() + VACCINE_IMAGE_FOLDER + FORWARD_SLASH).toAbsolutePath().normalize();
            if(!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getEmail() + VACCINE_IMAGE + DOT + JPG_EXTENSION));
            Files.copy(vaccineImage.getInputStream(),
                    userFolder.resolve(user.getEmail() + VACCINE_IMAGE + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setVaccineImage(setVaccineImageUrl(user.getEmail()));
            userRepository.save(user);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + vaccineImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String email) {

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DEFAULT_USER_IMAGE_PATH + email + FORWARD_SLASH + email + DOT + JPG_EXTENSION)
                .toUriString();
    }

    private String setVaccineImageUrl(String email) {

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DEFAULT_USER_IMAGE_PATH + email + FORWARD_SLASH + VACCINE_IMAGE_FOLDER + FORWARD_SLASH + email + VACCINE_IMAGE + DOT + JPG_EXTENSION)
                .toUriString();
    }

    private Role getRoleEnumName(String role) { return Role.valueOf(role.toUpperCase()); }

    private String getTemporaryProfileImageUrl(String email) {

        return TEMP_PROFILE_IMAGE_BASE_URL + email + "/?set=set2";
    }

    private String encodePassword(String password) {

        return passwordEncoder.encode(password);
    }

    private String generatePassword() {

        return RandomStringUtils.randomAlphanumeric(10);
    }

    private User validateNewCpfAndEmail(String currentEmail, String newEmail, String newCpf)
            throws EmailExistException, CpfExistException,
            UserNotFoundException {
        User userByNewEmail = findUserByEmail(newEmail);
        User userByNewCpf = findUserByCpf(newCpf);
        if(StringUtils.isNotBlank(currentEmail)) {
            User currentUser = findUserByEmail(currentEmail);
            if(currentUser == null) {
                throw new UserNotFoundException(USER_NOT_FOUND_BY_EMAIL + currentEmail);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXIST);
            }
            if(userByNewCpf != null && !currentUser.getId().equals(userByNewCpf.getId())) {
                throw new CpfExistException(CPF_ALREADY_EXIST );
            }
            return currentUser;
        } else {
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXIST);
            }
            if(userByNewCpf != null) {
                throw new CpfExistException(CPF_ALREADY_EXIST );
            }
            return null;
        }
    }
}
