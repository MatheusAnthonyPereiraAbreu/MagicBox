import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { CarouselModule } from 'primeng/carousel';
import { LogoComponent } from '../../components/logo/logo.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  standalone: true,
  imports: [CommonModule, NgxChartsModule, CarouselModule, LogoComponent]
})
export class HomeComponent implements OnInit {
  // Bandeiras dos países (exemplo)
  flags = [
    { name: 'Brasil', img: 'https://flagcdn.com/br.svg' },
    { name: 'Estados Unidos', img: 'https://flagcdn.com/us.svg' },
    { name: 'França', img: 'https://flagcdn.com/fr.svg' },
    { name: 'Alemanha', img: 'https://flagcdn.com/de.svg' },
    { name: 'Japão', img: 'https://flagcdn.com/jp.svg' },
    { name: 'Reino Unido', img: 'https://flagcdn.com/gb.svg' }
  ];

  // Dados dos gráficos de pizza
  artistasData = [
    { name: 'Artista 1', value: 30 },
    { name: 'Artista 2', value: 50 },
    { name: 'Artista 3', value: 40 },
    { name: 'Artista 4', value: 20 }
  ];
  musicasData = [
    { name: 'Música 1', value: 80 },
    { name: 'Música 2', value: 60 },
    { name: 'Música 3', value: 90 },
    { name: 'Música 4', value: 100 }
  ];
  albunsData = [
    { name: 'Álbum 1', value: 45 },
    { name: 'Álbum 2', value: 60 },
    { name: 'Álbum 3', value: 55 },
    { name: 'Álbum 4', value: 80 }
  ];

  // Gráfico de área com mais dados
  generosData = [
    {
      name: 'Pop',
      series: [
        { name: 'Jan', value: 20 },
        { name: 'Fev', value: 35 },
        { name: 'Mar', value: 25 },
        { name: 'Abr', value: 50 },
        { name: 'Mai', value: 40 },
        { name: 'Jun', value: 60 },
        { name: 'Jul', value: 55 },
        { name: 'Ago', value: 70 }
      ]
    },
    {
      name: 'Rock',
      series: [
        { name: 'Jan', value: 15 },
        { name: 'Fev', value: 30 },
        { name: 'Mar', value: 20 },
        { name: 'Abr', value: 45 },
        { name: 'Mai', value: 35 },
        { name: 'Jun', value: 55 },
        { name: 'Jul', value: 50 },
        { name: 'Ago', value: 65 }
      ]
    },
    {
      name: 'Sertanejo',
      series: [
        { name: 'Jan', value: 10 },
        { name: 'Fev', value: 25 },
        { name: 'Mar', value: 15 },
        { name: 'Abr', value: 40 },
        { name: 'Mai', value: 30 },
        { name: 'Jun', value: 50 },
        { name: 'Jul', value: 45 },
        { name: 'Ago', value: 60 }
      ]
    }
  ];

  colorScheme = 'cool';
  view: [number, number] = [400, 250];
  viewArea: [number, number] = [1480, 265];

  ngOnInit() {
    // Additional initialization logic if needed
  }
}
