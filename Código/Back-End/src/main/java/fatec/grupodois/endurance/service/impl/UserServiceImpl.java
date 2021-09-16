package fatec.grupodois.endurance.service.impl;

import fatec.grupodois.endurance.entity.UserPrincipal;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.exception.EmailNotFoundException;
import fatec.grupodois.endurance.exception.UserNotFoundException;
import fatec.grupodois.endurance.repository.UserRepository;
import fatec.grupodois.endurance.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Qualifier("UserService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
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
    public User fetchUserByEmail(String userEmail) throws EmailNotFoundException {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if(user.isEmpty()) {
            LOGGER.error("Usuário com email " + userEmail + " não encontrado.");
            throw new EmailNotFoundException("Usuário com email " +
                     userEmail +
                    " não encontrado.");
        }

        return userRepository.findByEmail(userEmail).get();
    }

    @Override
    public List<User> fetchAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long usuarioID) {

        userRepository.deleteById(usuarioID);
    }

    @Override
    public void updateUser(Long userId, User user) {

        User userDb = userRepository.findById(userId).get();

        if(StringUtils.isNotEmpty(user.getUserFirstName()) &&
                StringUtils.isNotBlank(user.getUserFirstName()) &&
                !"".equalsIgnoreCase(user.getUserFirstName())) {

            userDb.setUserFirstName(user.getUserFirstName());
        }

        if(StringUtils.isNotEmpty(user.getUserLastName()) &&
                StringUtils.isNotBlank(user.getUserLastName()) &&
                !"".equalsIgnoreCase(user.getUserLastName())) {
            userDb.setUserLastName(user.getUserLastName());
        }

        if(StringUtils.isNotEmpty(user.getUserRg()) &&
                StringUtils.isNotBlank(user.getUserRg()) &&
                !"".equalsIgnoreCase(user.getUserRg())) {
            userDb.setUserRg(user.getUserRg());
        }

        if(StringUtils.isNotEmpty(user.getUserEmail()) &&
                StringUtils.isNotBlank(user.getUserEmail()) &&
                !"".equalsIgnoreCase(user.getUserEmail())) {
            userDb.setUserEmail(user.getUserEmail());
        }


        userRepository.save(userDb);
    }


    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByEmail(userEmail);

        if(userOpt.isEmpty()) {
            LOGGER.error("User com email " + userEmail + " não encontrado.");
            throw new UsernameNotFoundException("Usuário com email " +
                    userEmail +
                    " não encontrado.");
        }

        User user = userRepository.findByEmail(userEmail).get();
        user.setUserLastLoginDateDisplay(user.getUserLastLoginDate());
        user.setUserLastLoginDate(new Date());
        userRepository.save(user);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        LOGGER.info("Returning found user by email: " + userEmail);

        return userPrincipal;
    }
}
