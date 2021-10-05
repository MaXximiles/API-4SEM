import { ThrowStmt } from '@angular/compiler';
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import {
  CalendarOptions,
  DateSelectArg,
  EventClickArg,
  EventApi,
  EventDropArg,
  EventInput,
} from '@fullcalendar/angular';
import ptBRlocale from '@fullcalendar/core/locales/pt-br';
import { Evento } from 'src/app/model/event';
import { EventoService } from 'src/app/service/evento.service';
import { ModalComponent } from '../modal/modal.component';
import { ModalConfig } from '../modal/modal.config';
import { INITIAL_EVENTS, createEventId } from './event-utils';

@Component({
  selector: 'app-full-calendar',
  templateUrl: './full-calendar.component.html',
  styleUrls: ['./full-calendar.component.css'],
})
export class FullCalendarComponent implements OnInit {
  // Pegando o componente html com o @ViewChild
  @ViewChild('modalInsert') private modalInsertComponent: ModalComponent;
  @ViewChild('modalEdit') private modalEditComponent: ModalComponent;
  @ViewChild('newEventForm') private insertForm: NgForm;

  // Eventos
  private eventInput: EventInput[] = [];

  // Configuração dos modais
  modalInsertConfig: ModalConfig = {
    modalTitle: 'Inserir um Evento',
    dismissButtonLabel: 'Confirmar Evento',
    closeButtonLabel: 'Fechar',
    onDismiss: () => {
      this.insertForm.ngSubmit.emit();
      return true;
    },
  };
  modalEditConfig: ModalConfig = {
    modalTitle: 'Editar um Evento',
    dismissButtonLabel: 'Salvar Alterações',
    closeButtonLabel: 'Fechar',
  };

  // Configuração do calendário
  calendarVisible = true;
  calendarOptions: CalendarOptions = {
    aspectRatio: 2,
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek',
    },
    initialView: 'dayGridMonth',
    // Esse aqui é o problema, o calendaário n tá acompanhando as informações
    events: (info, successCallback, failureCallback) => {
      this.refreshCalendar();
      successCallback(this.eventInput);
    },
    weekends: true,
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEvents: true,
    locales: [ptBRlocale],
    locale: 'pt-br',
    // As chamadas de função tem que ter o ".bind(this)", se não não funciona
    select: this.handleDateSelect.bind(this),
    eventClick: this.handleEventClick.bind(this),
    eventsSet: this.handleEvents.bind(this),
    eventDrop: this.handleEventChangeTime.bind(this),
    eventResize: this.handleEventChangeTime.bind(this),
    /* you can update a remote database when these fire:
    eventAdd:
    eventChange:
    eventRemove:
    */
  };
  currentEvents: EventApi[] = [];

  constructor(private eventoService: EventoService) {}

  ngOnInit(): void {}

  refreshCalendar(): void {
    this.eventoService.fetchAllEvents().subscribe((eventos) => {
      eventos.forEach((evento) => {
        this.addEventoToFullcalendar(evento);
      });

      this.calendarOptions.events = this.eventInput;
    });
  }

  // Adiciona um evento vindo do backend para o fullcalendar no formato dele
  private addEventoToFullcalendar(evento: Evento) {
    var eventInputTemp: EventInput = {
      id: evento.id.toString(),
      title: evento.descricao,
      start: evento.inicio,
      end: evento.fim,
      allDay: false,
    };

    this.eventInput.push(eventInputTemp);
  }

  // Função que é chamada quando o usuário finaliza o modal de insert para inserir o evento na API (OK)
  public onAddNewEvent(eventForm: NgForm): void {
    const formData = this.eventoService.createEventFormData(eventForm.value);
    this.eventoService.addEvent(formData).subscribe((result) => {
      this.refreshCalendar();
    });
  }

  // Abre o modal e insere o evento na API (OK)
  handleDateSelect(selectInfo: DateSelectArg) {
    this.modalInsertComponent.open().then((result) => {
      if (result) {
      }
    });

    const title = prompt('Please enter a new title for your event');
    const calendarApi = selectInfo.view.calendar;

    calendarApi.unselect(); // clear date selection

    if (title) {
      calendarApi.addEvent({
        id: createEventId(),
        title,
        start: selectInfo.startStr,
        end: selectInfo.endStr,
        allDay: selectInfo.allDay,
      });
    }
  }

  // TODO - Quando clicar no evento, abrir o modal de edição (o atributo "clickInfo" tem as informações necessárias)
  handleEventClick(clickInfo: EventClickArg) {
    if (
      confirm(
        `Are you sure you want to delete the event '${clickInfo.event.title}'`
      )
    ) {
      clickInfo.event.remove();
    }
  }

  // Função é chamada quando o usuário arrasta o evento pra outra data ou expande o horário do evento
  handleEventChangeTime(eventInfo: EventDropArg) {
    if (eventInfo.event !== eventInfo.oldEvent) {
      if (!confirm('Alterar?')) {
        eventInfo.revert();
      }
    }
  }

  handleEvents(events: EventApi[]) {
    this.currentEvents = events;
  }
}
