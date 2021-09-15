package fatec.grupodois.endurance.configuration;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.StatusEvento;
import fatec.grupodois.endurance.entity.TipoUsuario;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.repository.EventoRepository;
import fatec.grupodois.endurance.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class EventoConfig {

    @Bean
    CommandLineRunner commandLineRunner(EventoRepository repo) {
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

            repo.saveAll(List.of(evento1, evento2));
        };
    }
}
