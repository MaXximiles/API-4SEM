package fatec.grupodois.endurance.configuration;

import fatec.grupodois.endurance.constant.FileConstant;
import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.enumeration.LocalEvento;
import fatec.grupodois.endurance.enumeration.StatusEvento;
import fatec.grupodois.endurance.repository.EventoRepository;
import fatec.grupodois.endurance.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static fatec.grupodois.endurance.constant.FileConstant.TEMP_PROFILE_IMAGE_BASE_URL;
import static fatec.grupodois.endurance.enumeration.Role.ROLE_ADMIN;
import static fatec.grupodois.endurance.enumeration.Role.ROLE_GUEST;

@Configuration
public class EventoConfig {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    CommandLineRunner commandLineRunner(EventoRepository repo, UserRepository repo2) {
        return args -> {

            String password = "12345";
            String encodedPassword = encodePassword(password);

            User user = User
                    .builder()
                    .firstName("Jeferson")
                    .lastName("Tadeu das Neves")
                    .email("jefh.neves@gmail.com")
                    .cpf("97301794096")
                    .joinDate(new Date())
                    .password(encodedPassword)
                    .isActive(true)
                    .isNotLocked(true)
                    .role(ROLE_ADMIN.name())
                    .authorities(ROLE_ADMIN.getAuthorities())
                    .profileImageUrl(TEMP_PROFILE_IMAGE_BASE_URL+"jefh.neves@gmail.com")
                    .vaccineImage(null)
                    .id(1L)
                    .build();

            User user2 = User
                    .builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("john.doe@gmail.com")
                    .cpf("94652025092")
                    .joinDate(new Date())
                    .password(encodedPassword)
                    .isActive(true)
                    .isNotLocked(true)
                    .role(ROLE_GUEST.name())
                    .authorities(ROLE_GUEST.getAuthorities())
                    .profileImageUrl(TEMP_PROFILE_IMAGE_BASE_URL+"john.doe@gmail.com")
                    .vaccineImage(null)
                    .id(2L)
                    .build();

            User user3 = User
                    .builder()
                    .firstName("Alice")
                    .lastName("Doe")
                    .email("alice.doe@gmail.com")
                    .cpf("50711886008")
                    .joinDate(new Date())
                    .password(encodedPassword)
                    .isActive(true)
                    .isNotLocked(true)
                    .role(ROLE_GUEST.name())
                    .authorities(ROLE_GUEST.getAuthorities())
                    .profileImageUrl(TEMP_PROFILE_IMAGE_BASE_URL+"alice.doe@gmail.com")
                    .vaccineImage(null)
                    .id(3L)
                    .build();

            User user4 = User
                    .builder()
                    .firstName("Bob")
                    .lastName("Doe")
                    .email("bob.doe@gmail.com")
                    .cpf("14880584070")
                    .joinDate(new Date())
                    .password(encodedPassword)
                    .isActive(true)
                    .isNotLocked(true)
                    .role(ROLE_GUEST.name())
                    .authorities(ROLE_GUEST.getAuthorities())
                    .profileImageUrl(TEMP_PROFILE_IMAGE_BASE_URL+"bob.doe@gmail.com")
                    .vaccineImage(null)
                    .id(4L)
                    .build();

            LocalTime open = LocalTime.of(10,00,00);
            LocalDateTime date = LocalDateTime.of(LocalDateTime.now().toLocalDate(), open);

            Evento event = Evento
                    .builder()
                    .id(1L)
                    .inicio(date)
                    .fim(date.plusHours(1L))
                    .local(LocalEvento.LOUNGE.name())
                    .tema("Lean Agile")
                    .descricao("Entenda a nova tendência de arquitetura de software")
                    .observacao("Necessário carteira de vacinação")
                    .user(user)
                    .criacao(LocalDateTime.now())
                    .status(StatusEvento.PENDENTE.name())
                    .maxParticipantes(50)
                    .totalParticipantes(0)
                    .build();

            Evento event2 = Evento
                    .builder()
                    .id(2L)
                    .inicio(date.plusMinutes(120L))
                    .fim(date.plusHours(3L))
                    .local(LocalEvento.OPENSPACE.name())
                    .tema("Kanban")
                    .descricao("Entenda a nova tendência de arquitetura de software 2")
                    .observacao("Necessário carteira de vacinação 2")
                    .user(user2)
                    .criacao(LocalDateTime.now())
                    .status(StatusEvento.PENDENTE.name())
                    .maxParticipantes(50)
                    .totalParticipantes(0)
                    .build();

            repo2.saveAll(List.of(user,user2,user3,user4));

            repo.saveAll(List.of(event,event2));
        };
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
