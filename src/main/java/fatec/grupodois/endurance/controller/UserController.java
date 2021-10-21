package fatec.grupodois.endurance.controller;

import fatec.grupodois.endurance.entity.HttpResponse;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.entity.UserPrincipal;
import fatec.grupodois.endurance.exception.*;
import fatec.grupodois.endurance.service.UserService;
import fatec.grupodois.endurance.utils.JWTTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static fatec.grupodois.endurance.constant.FileConstant.*;
import static fatec.grupodois.endurance.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(path = {"/", "/user"})
public class UserController extends ExceptionHandling {

    public static final String USER_DELETED_SUCCESFULLY = "Usuário deletado com sucesso";
    public static final String PASSWORD_SUCCESS = "Nova senha enviada para: ";
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager,
                          JWTTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> fetchUserList() {
        List<User> users = userService.fetchAllUsers();

        return new ResponseEntity<>(users, OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity<User> addUser(@RequestParam("firstName") String firstName,
                                        @RequestParam("lastName") String lastName,
                                        @RequestParam("email") String email,
                                        @RequestParam("cpf") String cpf,
                                        @RequestParam("role") String role,
                                        @RequestParam("isActive") String isActive,
                                        @RequestParam("isNonLocked") String isNonLocked,
                                        @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)

            throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException {


        LOGGER.info(("ACTIVE>>>>" + isActive));
        LOGGER.info("NONLOCKED>>>>>>>>" + isNonLocked);
       User newUser = userService.addNewUser(firstName, lastName, email, cpf, role,
                                                Boolean.parseBoolean(isActive), Boolean.parseBoolean(isNonLocked),
                                                profileImage);

        return new ResponseEntity<>(newUser, CREATED);
    }

    @PostMapping("/update-me")
    public ResponseEntity<User> updateCurrentUser(@RequestParam("currentEmail") String currentEmail,
                                           @RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("email") String email,
                                           @RequestParam("cpf") String cpf,
                                           @RequestParam("role") String role,
                                           @RequestParam("isActive") String isActive,
                                           @RequestParam("isNonLocked") String isNonLocked,
                                           @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)

            throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException {

        User updatedUser = userService.updateUser(currentEmail, firstName, lastName, email, cpf, role,
                Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive),
                profileImage, null);

        return new ResponseEntity<>(updatedUser, OK);
    }

    @PostMapping("/update/{adminEmail}")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseEntity<User> updateUser(@RequestParam("currentEmail") String currentEmail,
                                        @RequestParam("firstName") String firstName,
                                        @RequestParam("lastName") String lastName,
                                        @RequestParam("email") String email,
                                        @RequestParam("cpf") String cpf,
                                        @RequestParam("role") String role,
                                        @RequestParam("isActive") String isActive,
                                        @RequestParam("isNonLocked") String isNonLocked,
                                        @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                                           @PathVariable("adminEmail") String adminEmail)

            throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException {

        User updatedUser = userService.updateUser(currentEmail, firstName, lastName, email, cpf, role,
                Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive),
                profileImage, adminEmail);

        return new ResponseEntity<>(updatedUser, OK);
    }

    @PostMapping("/update-profile-image")
    public ResponseEntity<User> updateProfileImage(@RequestParam("email") String email,
                                                    @RequestParam("profileImage") MultipartFile profileImage)

            throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException {

        User updateProfileImage = userService.updateProfileImage(email, profileImage);

        return new ResponseEntity<>(updateProfileImage, OK);
    }

    @PostMapping("/update-vaccine-image")
    public ResponseEntity<User> updateVaccineImage(@RequestParam("email") String email,
                                                   @RequestParam("vaccineImage") MultipartFile vaccineImage)

            throws UserNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException, IOException {

        User user = userService.updateVaccineImage(email, vaccineImage);

        return new ResponseEntity<>(user, OK);
    }

    @GetMapping(path = "/image/{email}/vacina/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] fetchVaccineImage(@PathVariable("email") String email,
                                    @PathVariable("fileName") String fileName) throws IOException {

        return Files.readAllBytes(Paths.get(USER_FOLDER + email + VACCINE_IMAGE_FOLDER + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/{email}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] fetchProfileImage(@PathVariable("email") String email,
                                    @PathVariable("fileName") String fileName) throws IOException {

        return Files.readAllBytes(Paths.get(USER_FOLDER + email + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/profile/{email}", produces = IMAGE_JPEG_VALUE)
    public byte[] fetchTempProfileImage(@PathVariable("email") String email) throws IOException {

        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + email);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try(InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];

            while((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        } catch(IOException e) {
            throw new IOException(IMAGE_FILE_ERROR);
        }

        return byteArrayOutputStream.toByteArray();
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {

        authenticate(user.getEmail(), user.getPassword());
        User loginUser = userService.findUserByEmail(user.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws EmailExistException,
            UserNotFoundException, EmailNotFoundException,
            CpfExistException, CpfNotFoundException, MessagingException {

        user = userService.register(user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getCpf(), user.getPassword());

        return new ResponseEntity<>(user, CREATED);
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<User> fetchUserById(@PathVariable("id") Long userId) throws UserNotFoundException {

        User user = userService.findUserById(userId);

        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/fetch-by-email/{email}")
    public ResponseEntity<User> fetchUserByEmail(@PathVariable("email") String userEmail) throws EmailNotFoundException {
        User user = userService.findUserByEmail(userEmail);


        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/fetch-by-cpf/{cpf}")
    public ResponseEntity<User> fetchUserByCpf(@PathVariable("cpf") String cpf) throws EmailNotFoundException {

        User user = userService.findUserByCpf(cpf);

        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/reset-password-front/{cpf}")
    public ResponseEntity<HttpResponse> resetPasswordFront(@PathVariable("cpf") String cpf)
            throws EmailNotFoundException, MessagingException, CpfNotFoundException {

        User newPassword = userService.resetPasswordFront(cpf);

        return response(OK, PASSWORD_SUCCESS + newPassword.getEmail());
    }

    @GetMapping("/reset-password/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email)
            throws EmailNotFoundException, MessagingException {

        userService.resetPassword(email);

        return response(OK, PASSWORD_SUCCESS + email.substring(5));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {

        userService.deleteUser(id);

        return response(OK, USER_DELETED_SUCCESFULLY);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus status, String s) {

        return new ResponseEntity<>(new HttpResponse(status.value(),
                status, status.getReasonPhrase(),
                s), status);
    }


    private HttpHeaders getJwtHeader(UserPrincipal user) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));

        return headers;
    }

    private void authenticate(String email, String password) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
