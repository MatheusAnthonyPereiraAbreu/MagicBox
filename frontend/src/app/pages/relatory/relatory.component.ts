import { Component } from '@angular/core';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { CarouselModule } from 'primeng/carousel';

@Component({
  selector: 'app-relatory',
  templateUrl: './relatory.component.html',
  styleUrls: ['./relatory.component.scss'],
  standalone: true,
  imports: [NgxChartsModule, CarouselModule]
})
export class RelatoryComponent {
  colorScheme = 'cool';

  pieData = [
    { name: 'Categoria 1', value: 35 },
    { name: 'Categoria 2', value: 55 },
    { name: 'Categoria 3', value: 10 }
  ];

  // Gráfico de Barras
  barData = [
    { name: 'Janeiro', value: 30 },
    { name: 'Fevereiro', value: 80 },
    { name: 'Março', value: 45 },
    { name: 'Abril', value: 60 }
  ];

  // Gráfico de Linha
  lineData = [
    {
      name: 'Vendas',
      series: [
        { name: '2021', value: 50 },
        { name: '2022', value: 80 },
        { name: '2023', value: 65 }
      ]
    }
  ];

  // Gráfico de Área
  areaData = [
    {
      name: 'Usuários',
      series: [
        { name: 'Jan', value: 20 },
        { name: 'Fev', value: 40 },
        { name: 'Mar', value: 35 },
        { name: 'Abr', value: 60 }
      ]
    }
  ];

  view: [number, number] = [800, 600];

  selectedTab = 0;

  baixarPDF() {
    window.open('http://localhost:5000/baixar-pdf', '_blank');
  }
}
