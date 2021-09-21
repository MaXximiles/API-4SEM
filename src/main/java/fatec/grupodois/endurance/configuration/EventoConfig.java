package fatec.grupodois.endurance.configuration;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.StatusEvento;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.repository.EventoRepository;
import fatec.grupodois.endurance.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static fatec.grupodois.endurance.enumeration.Role.ROLE_GUEST;

@Configuration
public class EventoConfig {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    CommandLineRunner commandLineRunner(EventoRepository repo, UserRepository repo2) {
        return args -> {
            Evento evento1 = Evento.builder()
                    .eventoTema("Teste1")
                    .eventoDescricao("Descricao1")
                    .eventoLocal("Local1")
                    .eventoCriacao(LocalDateTime.now())
                    .eventoInicio(LocalDateTime.of(LocalDate.of(2021, 10, 5), LocalTime.of(14, 30, 0)))
                    .eventoFim(LocalDateTime.of(LocalDate.of(2021, 10, 5), LocalTime.of(15, 30, 0)))
                    .eventoStatus(StatusEvento.PENDENTE)
                    .eventoObservacao("AGENDAMENTO ENVIADO")
                    .build();

            Evento evento2 = Evento.builder()
                    .eventoTema("Teste2")
                    .eventoDescricao("Descricao2")
                    .eventoLocal("Local2")
                    .eventoCriacao(LocalDateTime.now())
                    .eventoInicio(LocalDateTime.of(LocalDate.of(2021, 11, 5), LocalTime.of(14, 30, 0)))
                    .eventoFim(LocalDateTime.of(LocalDate.of(2021, 11, 5), LocalTime.of(15, 30, 0)))
                    .eventoStatus(StatusEvento.PENDENTE)
                    .eventoObservacao("AGENDAMENTO ENVIADO")
                    .build();

            String password = "12345";
            String encodedPassword = encodePassword(password);

            User user = User
                    .builder()
                    .firstName("Teste")
                    .lastName("S")
                    .email("jefh.neves@gmail.com")
                    .cpf("973.017.940-96")
                    .joinDate(new Date())
                    .password(encodedPassword)
                    .isActive(true)
                    .isNotLocked(true)
                    .role(ROLE_GUEST.name())
                    .authorities(ROLE_GUEST.getAuthorities())
                    .profileImageUrl("https://robohash.org/jefh/?set=set2")
                    .id(1L)
                    .build();
        /* criar mais users:
            User user2 = User
                    .builder()
                    .firstName("Teste")
                    .lastName("S")
                    .email("jefh.neves@gmail.com")
                    .cpf("973.017.940-96")
                    .joinDate(new Date())
                    .password(encodedPassword)
                    .isActive(true)
                    .isNotLocked(true)
                    .role(ROLE_GUEST.name())
                    .authorities(ROLE_GUEST.getAuthorities())
                    .profileImageUrl("https://robohash.org/jefh/?set=set2")
                    .id(1L)
                    .build();*/
        /*passar pro repo2 salvar */
            repo2.saveAll(List.of(user));

            repo.saveAll(List.of(evento1, evento2));
        };
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
