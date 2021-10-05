import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import {
  CalendarOptions,
  DateSelectArg,
  EventClickArg,
  EventApi,
  EventDropArg,
  EventInput,
  FullCalendarComponent as FullCalendar,
} from '@fullcalendar/angular';
import ptBRlocale from '@fullcalendar/core/locales/pt-br';
import { NotificationType } from 'src/app/enum/notification-type.enum';
import { Evento } from 'src/app/model/event';
import { EventoService } from 'src/app/service/evento.service';
import { NotificationService } from 'src/app/service/notification.service';
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
  @ViewChild('calendar') private fullcalendar: FullCalendar;
  @ViewChild('modalEvento') private modalComponent: ModalComponent;
  @ViewChild('eventForm') private eventForm: NgForm;

  modalConfig: ModalConfig = null;
  // Configurações do modal
  modalInsertConfig: ModalConfig = {
    modalTitle: 'Inserir um Evento',
    dismissButtonLabel: 'Confirmar Evento',
    closeButtonLabel: 'Fechar',
    onDismiss: () => {
      this.eventForm.ngSubmit.emit();
      return true;
    },
  };
  modalEditConfig: ModalConfig = {
    modalTitle: 'Editar um Evento',
    dismissButtonLabel: 'Salvar Alterações',
    closeButtonLabel: 'Fechar',
    onDismiss: () => {
      return true;
    },
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
      this.eventoService.fetchAllEvents().subscribe((eventos) => {
        const events: EventInput[] = [];
        eventos.forEach((evento) => {
          var eventInputTemp: EventInput = {
            id: evento.id.toString(),
            title: evento.descricao,
            start: evento.inicio,
            end: evento.fim,
            allDay: false,
          };

          events.push(eventInputTemp);
        });

        successCallback(events);
      });
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

  constructor(
    private eventoService: EventoService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {}

  // Função que é chamada quando o usuário finaliza o modal de insert para inserir o evento na API (OK)
  public onAddNewEvent(eventForm: NgForm): void {
    const formData = this.eventoService.createEventFormData(eventForm.value);
    this.eventoService.addEvent(formData).subscribe(
      (response: Evento) => {
        this.fullcalendar.getApi().refetchEvents();

        this.sendNotification(
          NotificationType.SUCCESS,
          `O evento ${response.descricao} foi marcado com de "${response.inicio}" até "${response.fim}" com sucesso`
        );
      },
      (errorResponse: HttpErrorResponse) => {
        this.sendNotification(
          NotificationType.ERROR,
          errorResponse.error.message
        );
      }
    );
  }

  public onEditEvent(eventForm: NgForm): void {}

  // Abre o modal e insere o evento na API (OK)
  handleDateSelect(selectInfo: DateSelectArg) {
    this.openModalInsert();

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
    this.openModalEdit();
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

  openModalInsert(): void {
    this.modalConfig = this.modalInsertConfig;
    this.modalComponent.open().then(() => {
      this.eventForm.resetForm();
    });
  }

  openModalEdit(): void {
    this.modalConfig = this.modalEditConfig;
    this.modalComponent.open().then(() => {
      this.eventForm.resetForm();
    });
  }

  private sendNotification(
    notificationType: NotificationType,
    message: string
  ): void {
    if (message) {
      this.notificationService.myNofity(notificationType, message);
    } else {
      this.notificationService.myNofity(
        notificationType,
        'Um erro ocorreu. Por favor tente novamente'
      );
    }
  }
}
