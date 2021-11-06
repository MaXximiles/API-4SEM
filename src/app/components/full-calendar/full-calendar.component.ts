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
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { NotificationType } from 'src/app/enum/notification-type.enum';
import { Evento } from 'src/app/model/event';
import { User } from 'src/app/model/user';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { EventoService } from 'src/app/service/evento.service';
import { NotificationService } from 'src/app/service/notification.service';
import { UserService } from 'src/app/service/user.service';
import { ModalComponent } from '../modal/modal.component';
import { ModalConfig } from '../modal/modal.config';

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
  baseInvitedUsers: User[] = [];
  invitedUsers: User[] = [];
  selectedUser: User = {};
  unfilteredUsers: User[] = [];
  users: User[] = [];
  addedUsers: User[] = [];
  deletedUsers: User[] = [];
  editEvent: Evento = new Evento();
  filter: string = '';

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
    modalTitle: `Editar ${this.editEvent.tema}`,
    dismissButtonLabel: 'Salvar Alterações',
    closeButtonLabel: 'Fechar',
    onDismiss: () => {
      this.eventForm.ngSubmit.emit();
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
            title: evento.tema,
            start: evento.inicio,
            end: evento.fim,
            color: evento.participantes.find(
              (user) => user.id === this.authService.getUserFromLocalCache().id
            )
              ? '#00bcd4'
              : '#ff9800',
            backgroundColor: evento.participantes.find(
              (user) => user.id === this.authService.getUserFromLocalCache().id
            )
              ? '#00bcd4'
              : '#ff9800',
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
    private notificationService: NotificationService,
    private userService: UserService,
    private authService: AuthenticationService
  ) {}

  ngOnInit(): void {
    this.fetchUsers();
  }

  // Função que é chamada quando o usuário finaliza o modal de insert para inserir o evento na API (OK)
  public onAddNewEvent(eventForm: NgForm): void {
    const formData = this.eventoService.createEventFormData(eventForm.value);
    this.eventoService.addEvent(formData).subscribe(
      (response: Evento) => {
        this.addedUsers.forEach((user) => {
          this.eventoService.addGuest(user, response.id).subscribe((result) => {
            console.log(result);
          });
        });

        this.fullcalendar.getApi().refetchEvents();

        this.sendNotification(
          NotificationType.SUCCESS,
          `O evento ${response.descricao} foi marcado com de "${moment(
            response.inicio
          ).format('DD/MM/YYYY hh:mm:ss')}" até "${moment(response.fim).format(
            'DD/MM/YYYY hh:mm:ss'
          )}" com sucesso`
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

  public onEditEvent(eventForm: NgForm): void {
    const formData = this.eventoService.createEventFormData(eventForm.value);

    this.eventoService.updateEvent(formData).subscribe(
      (response: Evento) => {
        this.addedUsers.forEach((user) => {
          this.eventoService
            .addGuest(user, response.id)
            .subscribe((result: Evento) => {
              console.log(result);
            });
        });

        this.deletedUsers.forEach((user) => {
          this.eventoService
            .removeGuest(user, response.id)
            .subscribe((result: Evento) => {
              console.log(result);
            });
        });

        this.fullcalendar.getApi().refetchEvents();

        this.sendNotification(
          NotificationType.SUCCESS,
          `O evento ${response.descricao} foi atualizado de "${moment(
            response.inicio
          ).format('DD/MM/YYYY hh:mm:ss')}" até "${moment(response.fim).format(
            'DD/MM/YYYY hh:mm:ss'
          )}" com sucesso`
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

  // Function that receives a list of users and a event id, and sends the users one by one to the API to addGuest. It must return a boolean Observable when it has sent all the users, true if all succeeded, false if any failed. It must wait one to finish to send the next user.
  addGuests(users: User[], eventId: number): Observable<boolean> {
    return new Observable((observer) => {
      users.forEach((user) => {
        this.eventoService.addGuest(user, eventId).subscribe(
          () => {},
          (errorResponse: HttpErrorResponse) => {
            this.sendNotification(
              NotificationType.ERROR,
              errorResponse.error.message
            );
          }
        );
      });

      observer.next(true);
    });
  }

  // Function that receives a list of users and a event id, and sends the users one by one to the API to removeGuest. It must return a boolean Observable when it has sent all the users, true if all succeeded, false if any failed. It must wait for one to finish to send the next user.
  removeGuests(users: User[], eventId: number): Observable<boolean> {
    return new Observable((observer) => {
      users.forEach((user) => {
        this.eventoService.removeGuest(user, eventId).subscribe(
          (response) => {},
          (errorResponse: HttpErrorResponse) => {
            this.sendNotification(
              NotificationType.ERROR,
              errorResponse.error.message
            );
          }
        );
      });

      observer.next(true);
    });
  }

  // Abre o modal e insere o evento na API (OK)
  handleDateSelect(selectInfo: DateSelectArg) {
    (this.authService.getUserFromLocalCache().role === 'ROLE_ADMIN' ||
      this.authService.getUserFromLocalCache().role === 'ROLE_ORACLE') &&
      this.openModalInsert();
    const calendarApi = selectInfo.view.calendar;
    calendarApi.unselect(); // clear date selection
  }

  // Quando clicar no evento, abrir o modal de edição (o atributo "clickInfo" tem as informações necessárias)
  handleEventClick(clickInfo: EventClickArg) {
    this.eventoService
      .fetchEventById(Number.parseInt(clickInfo.event.id))
      .subscribe(
        (evento) => {
          if (
            this.authService.getUserFromLocalCache().role === 'ROLE_ADMIN' ||
            (this.authService.getUserFromLocalCache().role === 'ROLE_ORACLE' &&
              evento.user.id === this.authService.getUserFromLocalCache().id)
          ) {
            this.openModalEdit(evento);
          } else {
            if (
              evento.participantes.find((participante) => {
                return (
                  participante.id ===
                  this.authService.getUserFromLocalCache().id
                );
              })
            ) {
              if (
                confirm(
                  `Deseja retirar sua participação do evento "${evento.tema}"?`
                )
              ) {
                this.eventoService
                  .removeGuest(
                    this.authService.getUserFromLocalCache(),
                    Number.parseInt(clickInfo.event.id)
                  )
                  .subscribe(
                    (response) => {
                      this.fullcalendar.getApi().refetchEvents();

                      this.sendNotification(
                        NotificationType.SUCCESS,
                        `Você se desinscreveu para o evento ${response.descricao}`
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
            } else {
              if (confirm(`Deseja participar do evento "${evento.tema}"?`)) {
                this.eventoService
                  .addGuest(
                    this.authService.getUserFromLocalCache(),
                    Number.parseInt(clickInfo.event.id)
                  )
                  .subscribe(
                    (response) => {
                      this.fullcalendar.getApi().refetchEvents();

                      this.sendNotification(
                        NotificationType.SUCCESS,
                        `Você se inscreveu para o evento ${response.descricao}`
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
            }
          }
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(
            NotificationType.ERROR,
            errorResponse.error.message
          );
        }
      );
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
      this.editEvent = new Evento();
      this.invitedUsers = [];
      this.addedUsers = [];
      this.deletedUsers = [];
      this.baseInvitedUsers = [];
      this.eventForm.resetForm();
    });
  }

  openModalEdit(evento: Evento): void {
    this.editEvent = evento;
    this.baseInvitedUsers = evento.participantes;
    this.invitedUsers = evento.participantes;
    this.modalConfig = this.modalEditConfig;
    this.modalComponent.open().then(() => {
      this.editEvent = new Evento();
      this.invitedUsers = [];
      this.addedUsers = [];
      this.deletedUsers = [];
      this.baseInvitedUsers = [];
      this.eventForm.resetForm();
    });
  }

  onDeleteUser(deletedUser: User): void {
    // If deletedUser exists in the baseInvitedUsers, remove it from the invitedUsers array and add it to the deletedUsers array
    if (this.baseInvitedUsers.find((user) => user.id === deletedUser.id)) {
      this.deletedUsers.push(deletedUser); // Add to deletedUsers
    } else {
      // If deletedUser doesn't exist in the baseInvitedUsers, it was to be added, therefore, remove it from the addedUsers and invitedUsers array
      this.addedUsers = this.addedUsers.filter(
        (user) => user.id !== deletedUser.id
      ); // Remove from addedUsers
    }

    this.invitedUsers = this.invitedUsers.filter(
      (user) => user.id !== deletedUser.id
    ); // Remove from invitedUsers

    this.filterUsers(this.filter);
  }

  onAddUser(addedUser: User): void {
    // If addedUser exists in the deletedUsers, remove it from the deletedUsers array. If it doesn't, add it to the addedUsers array and invitedUsers array
    if (this.deletedUsers.find((user) => user.id === addedUser.id)) {
      this.deletedUsers = this.deletedUsers.filter(
        (user) => user.id !== addedUser.id
      ); // Remove from deletedUsers
    } else {
      this.addedUsers.push(addedUser); // Add to addedUsers
    }

    this.invitedUsers.push(addedUser); // Add to invitedUsers

    this.filterUsers(this.filter);
  }

  onSelectUser(selectedUser): void {}

  filterUsers(filter): void {
    if (!filter.length) {
      this.users = [];

      return;
    }

    this.users = this.unfilteredUsers.filter((unfilteredUser) => {
      if (
        `${unfilteredUser.firstName} ${unfilteredUser.lastName}`.includes(
          filter
        ) ||
        unfilteredUser.email.includes(filter)
      ) {
        if (this.invitedUsers.find((user) => user.id === unfilteredUser.id)) {
          return false;
        }

        return true;
      }

      return false;
    });
  }

  fetchUsers() {
    this.userService.fetchAllUsers().subscribe((users: User[]) => {
      this.unfilteredUsers = users;
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
