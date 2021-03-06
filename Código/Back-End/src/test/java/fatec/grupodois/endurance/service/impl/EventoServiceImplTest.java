package fatec.grupodois.endurance.service.impl;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.enumeration.LocalEvento;
import fatec.grupodois.endurance.enumeration.StatusEvento;
import fatec.grupodois.endurance.exception.*;
import fatec.grupodois.endurance.repository.EventoRepository;
import fatec.grupodois.endurance.service.EventoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static fatec.grupodois.endurance.enumeration.Role.ROLE_GUEST;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventoServiceImplTest {

    @MockBean
    private EventoRepository eventoRepository;

    @Autowired
    private EventoService underTest;

    private User user;

    @BeforeEach
    void setUp() {
        this.user = User
                .builder()
                .firstName("Teste")
                .lastName("S")
                .email("teste@gmail.com")
                .cpf("973.017.940-96")
                .joinDate(new Date())
                .password("123")
                .isActive(true)
                .isNotLocked(false)
                .role(ROLE_GUEST.name())
                .authorities(ROLE_GUEST.getAuthorities())
                .profileImageUrl(null)
                .id(1L)
                .build();
    }

    @Test
    @DisplayName("Add Evento com in??cio no meio de outro Evento == Exc")
    void whenEventoOccurring_ShouldThrowExc()  {

        LocalTime open = LocalTime.of(10,00,00);
        LocalDateTime date = LocalDateTime.of(LocalDateTime.now().toLocalDate(), open);
        System.out.println(date);

        // given
        Evento event = Evento
                .builder()
                .id(1L)
                .inicio(date)
                .fim(date.plusHours(2L))
                .local(LocalEvento.OPENSPACE.name())
                .tema("Lean Agile")
                .descricao("Entenda a nova tend??ncia de arquitetura de software")
                .observacao("Necess??rio carteira de vacina????o")
                .user(user)
                .criacao(LocalDateTime.now())
                .status(StatusEvento.PENDENTE.name())
                .maxParticipantes(50)
                .totalParticipantes(1)
                .build();

        Evento event2 = Evento
                .builder()
                .id(2L)
                .inicio(date.plusMinutes(30L))
                .fim(date.plusHours(3L))
                .local(LocalEvento.OPENSPACE.name())
                .tema("Lean Agile 2")
                .descricao("Entenda a nova tend??ncia de arquitetura de software 2")
                .observacao("Necess??rio carteira de vacina????o 2")
                .user(this.user)
                .criacao(LocalDateTime.now())
                .status(StatusEvento.PENDENTE.name())
                .maxParticipantes(50)
                .totalParticipantes(1)
                .build();

        List<Evento> events = List.of(event);
        LocalDate date2 = event2.getInicio().toLocalDate();
        given(eventoRepository.findEventoByDate(date2))
                .willReturn(java.util.Optional.of(events));

        //when

        Throwable exc = assertThrows(EventIsOccurringException.class, () -> underTest.addEvento(event2));

        // then
        assertEquals("Evento ocorrendo no hor??rio de in??cio: "
                + event2.getInicio().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                + ". Sugest??o de hor??rio:08:00",
                exc.getMessage());


    }

    @Test
    @DisplayName("Add Evento com in??cio == de outro Evento == Exc")
    void whenEventoExist_ShouldThrowExc() {

        // given
        LocalTime open = LocalTime.of(10,00,00);
        LocalDateTime date = LocalDateTime.of(LocalDateTime.now().toLocalDate(), open);

        Evento event = Evento
                .builder()
                .id(1L)
                .inicio(date)
                .fim(date.plusHours(1L))
                .local(LocalEvento.OPENSPACE.name())
                .tema("Lean Agile")
                .descricao("Entenda a nova tend??ncia de arquitetura de software")
                .observacao("Necess??rio carteira de vacina????o")
                .user(this.user)
                .criacao(LocalDateTime.now())
                .status(StatusEvento.PENDENTE.name())
                .maxParticipantes(50)
                .totalParticipantes(1)
                .build();

        Evento event2 = Evento
                .builder()
                .id(2L)
                .inicio(date)
                .fim(date.plusHours(3L))
                .local(LocalEvento.OPENSPACE.name())
                .tema("Lean Agile 2")
                .descricao("Entenda a nova tend??ncia de arquitetura de software 2")
                .observacao("Necess??rio carteira de vacina????o 2")
                .user(this.user)
                .criacao(LocalDateTime.now())
                .status(StatusEvento.PENDENTE.name())
                .maxParticipantes(50)
                .totalParticipantes(1)
                .build();

        List<Evento> events = List.of(event);
        LocalDate date2 = event2.getInicio().toLocalDate();
        given(eventoRepository.findEventoByDate(date2))
                .willReturn(java.util.Optional.of(events));

        //when

        Throwable exc = assertThrows(EventoInicioExistException.class, () -> underTest.addEvento(event2));

        // then
        assertEquals("Evento j?? cadastrado com ??n??cio: "
                        + event2.getInicio().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                + ". Sugest??o de hor??rio:08:00",
                exc.getMessage());


    }

    @Test
    @DisplayName("Add Evento com in??cio fora do hor??rio de funcionamento == Exc")
    void whenEventoOutOfOpeningHours_ShouldThrowExc() {

        // given
        LocalTime open = LocalTime.of(6,00,00);
        LocalDateTime date = LocalDateTime.of(LocalDateTime.now().toLocalDate(), open);

        Evento event = Evento
                .builder()
                .id(1L)
                .inicio(date)
                .fim(LocalDateTime.now().plusHours(2L))
                .local(LocalEvento.OPENSPACE.name())
                .tema("Lean Agile")
                .descricao("Entenda a nova tend??ncia de arquitetura de software")
                .observacao("Necess??rio carteira de vacina????o")
                .user(this.user)
                .criacao(LocalDateTime.now())
                .status(StatusEvento.PENDENTE.name())
                .maxParticipantes(50)
                .totalParticipantes(1)
                .build();


        //when

        Throwable exc = assertThrows(EventOutOfOpeningHoursException.class, () -> underTest.addEvento(event));

        // then
        assertEquals("Evento fora do hor??rio de funcionamento: 08:00 ??s 22:00",
                exc.getMessage());

    }

    @Test
    @DisplayName("Add Evento com final fora do hor??rio de funcionamento == Exception")
    void whenEventoOutOfClosingHours_ShouldThrowExc() {

        // given
        LocalTime open = LocalTime.of(8,00,00);
        LocalTime close = LocalTime.of(23,00,00);
        LocalDateTime inicio = LocalDateTime.of(LocalDateTime.now().toLocalDate(), open);
        LocalDateTime fim = LocalDateTime.of(LocalDateTime.now().toLocalDate(), close);

        Evento event = Evento
                .builder()
                .id(1L)
                .inicio(inicio)
                .fim(fim)
                .local(LocalEvento.OPENSPACE.name())
                .tema("Lean Agile")
                .descricao("Entenda a nova tend??ncia de arquitetura de software")
                .observacao("Necess??rio carteira de vacina????o")
                .user(this.user)
                .criacao(LocalDateTime.now())
                .status(StatusEvento.PENDENTE.name())
                .maxParticipantes(50)
                .totalParticipantes(1)
                .build();


        //when

        Throwable exc = assertThrows(EventOutOfOpeningHoursException.class, () -> underTest.addEvento(event));

        // then
        assertEquals("Evento fora do hor??rio de funcionamento: 08:00 ??s 22:00",
                exc.getMessage());

    }

    @Test
    @DisplayName("Add Evento com inicio > final == Exception")
    void whenInicioAfterFinal_ShouldThrowExc() {

        // given
        LocalTime open = LocalTime.of(10,00,00);
        LocalTime close = LocalTime.of(9,00,00);
        LocalDateTime inicio = LocalDateTime.of(LocalDateTime.now().toLocalDate(), open);
        LocalDateTime fim = LocalDateTime.of(LocalDateTime.now().toLocalDate(), close);

        Evento event = Evento
                .builder()
                .id(1L)
                .inicio(inicio)
                .fim(fim)
                .local(LocalEvento.OPENSPACE.name())
                .tema("Lean Agile")
                .descricao("Entenda a nova tend??ncia de arquitetura de software")
                .observacao("Necess??rio carteira de vacina????o")
                .user(this.user)
                .criacao(LocalDateTime.now())
                .status(StatusEvento.PENDENTE.name())
                .maxParticipantes(50)
                .totalParticipantes(1)
                .build();


        //when

        Throwable exc = assertThrows(EventoInicioAfterException.class, () -> underTest.addEvento(event));

        // then
        assertEquals(event.getInicio()
                        + " depois de "
                        + event.getFim(),
                exc.getMessage());

    }

    @Test
    @DisplayName("Mesmo tema com letras min??sculas n??o deve dar update")
    void whenSameTema_ShouldNotUpdateEvento() throws EventoInicioAfterException, EventIsOccurringException, EventOutOfOpeningHoursException, EventoNotFoundException, EventoInicioExistException, EventDifferentDayException, MessagingException {
        // given
        LocalTime open = LocalTime.of(9,00,00);
        LocalTime close = LocalTime.of(10,00,00);
        LocalDateTime inicio = LocalDateTime.of(LocalDateTime.now().toLocalDate(), open);
        LocalDateTime fim = LocalDateTime.of(LocalDateTime.now().toLocalDate(), close);

        Evento event = Evento
                .builder()
                .id(1L)
                .inicio(inicio)
                .fim(fim)
                .local(LocalEvento.OPENSPACE.name())
                .tema("Lean Agile")
                .descricao("Entenda a nova tend??ncia de arquitetura de software")
                .observacao("Necess??rio carteira de vacina????o")
                .user(this.user)
                .criacao(LocalDateTime.now())
                .status(StatusEvento.PENDENTE.name())
                .maxParticipantes(50)
                .totalParticipantes(1)
                .build();

        Evento event2 = Evento
                .builder()
                .id(1L)
                .inicio(inicio)
                .fim(fim)
                .local(LocalEvento.OPENSPACE.name())
                .tema("lean agile")
                .descricao("Entenda a nova tend??ncia de arquitetura de software")
                .observacao("Necess??rio carteira de vacina????o")
                .user(this.user)
                .criacao(LocalDateTime.now())
                .status(StatusEvento.PENDENTE.name())
                .maxParticipantes(50)
                .totalParticipantes(1)
                .build();

        given(eventoRepository.findById(1L))
                .willReturn(java.util.Optional.of(event));

        //when
        underTest.updateEvento(1L,event2);

        //then
        ArgumentCaptor<Evento> studentArgumentCaptor =
                ArgumentCaptor.forClass(Evento.class);

        verify(eventoRepository)
                .save(studentArgumentCaptor.capture());

        Evento capturedEvento = studentArgumentCaptor.getValue();

        assertThat(capturedEvento.getTema()).isNotEqualTo(event2.getTema());

    }

    @Test
    @DisplayName("Tema diferente deve dar update")
    void whenDifferentTema_ShouldUpdateEvento() throws EventoInicioAfterException, EventIsOccurringException, EventOutOfOpeningHoursException, EventoNotFoundException, EventoInicioExistException, EventDifferentDayException, MessagingException {
        // given
        LocalTime open = LocalTime.of(9,00,00);
        LocalTime close = LocalTime.of(10,00,00);
        LocalDateTime inicio = LocalDateTime.of(LocalDateTime.now().toLocalDate(), open);
        LocalDateTime fim = LocalDateTime.of(LocalDateTime.now().toLocalDate(), close);

        Evento event = Evento
                .builder()
                .id(1L)
                .inicio(inicio)
                .fim(fim)
                .local(LocalEvento.OPENSPACE.name())
                .tema("Lean Agile")
                .descricao("Entenda a nova tend??ncia de arquitetura de software")
                .observacao("Necess??rio carteira de vacina????o")
                .user(this.user)
                .criacao(LocalDateTime.now())
                .status(StatusEvento.PENDENTE.name())
                .maxParticipantes(50)
                .totalParticipantes(1)
                .build();

        Evento event2 = Evento
                .builder()
                .id(1L)
                .inicio(inicio)
                .fim(fim)
                .local(LocalEvento.OPENSPACE.name())
                .tema("Cascate")
                .descricao("Entenda a nova tend??ncia de arquitetura de software")
                .observacao("Necess??rio carteira de vacina????o")
                .user(this.user)
                .criacao(LocalDateTime.now())
                .status(StatusEvento.PENDENTE.name())
                .maxParticipantes(50)
                .totalParticipantes(1)
                .build();

        given(eventoRepository.findById(1L))
                .willReturn(java.util.Optional.of(event));

        //when
        underTest.updateEvento(1L,event2);

        //then
        ArgumentCaptor<Evento> studentArgumentCaptor =
                ArgumentCaptor.forClass(Evento.class);

        verify(eventoRepository)
                .save(studentArgumentCaptor.capture());

        Evento capturedEvento = studentArgumentCaptor.getValue();

        assertThat(capturedEvento.getTema()).isEqualTo(event2.getTema());

    }
}