import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
//import { monitorEventLoopDelay } from 'perf_hooks';

@Component({
  selector: 'app-eventos',
  templateUrl: './eventos.component.html',
  styleUrls: ['./eventos.component.css'],
})
export class EventosComponent implements OnInit {
  // Lista de Eventos
  API_URL: string = environment.apiUrl;

  constructor(private httpClient: HttpClient) {}

  onDateClick(res) {}

  ngOnInit() {}
}
