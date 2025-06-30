import { Component, OnInit, OnDestroy, ViewChild, ElementRef, ChangeDetectorRef, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { CarouselModule } from 'primeng/carousel';
import { LogoComponent } from '../../components/logo/logo.component';
import { fromEvent, debounceTime, Subject, takeUntil } from 'rxjs';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
    standalone: true,
    imports: [CommonModule, NgxChartsModule, CarouselModule, LogoComponent]
})
export class HomeComponent implements OnInit, OnDestroy, AfterViewInit {
    @ViewChild('artistasChart') artistasChart: any;
    @ViewChild('musicasChart') musicasChart: any;
    @ViewChild('albunsChart') albunsChart: any;
    @ViewChild('generosChart') generosChart: any;

    // Estatísticas rápidas (mock)
    stats = [
        { label: 'Artistas', value: 24, icon: '🎤' },
        { label: 'Músicas', value: 120, icon: '🎵' },
        { label: 'Álbuns', value: 36, icon: '💿' },
        { label: 'Países', value: 6, icon: '🌎' }
    ];

    // Países (mock)
    flags = [
        { name: 'Brasil', img: 'https://flagcdn.com/br.svg' },
        { name: 'Estados Unidos', img: 'https://flagcdn.com/us.svg' },
        { name: 'França', img: 'https://flagcdn.com/fr.svg' },
        { name: 'Alemanha', img: 'https://flagcdn.com/de.svg' },
        { name: 'Japão', img: 'https://flagcdn.com/jp.svg' },
        { name: 'Reino Unido', img: 'https://flagcdn.com/gb.svg' }
    ];

    // Gráficos (mock)
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

    colorScheme: any = { domain: [] };
    chartView: [number, number] = [350, 220];
    areaChartView: [number, number] = [700, 250];
    chartHeight = 220;
    areaChartHeight = 250;

    // Propriedades responsivas pré-calculadas
    carouselVisible = 5;
    private resizeObserver: ResizeObserver | null = null;

    private destroy$ = new Subject<void>();

    selectedCountry = 'Brasil';

    constructor(private cdr: ChangeDetectorRef) { }

    ngOnInit() {
        this.updateResponsiveValues();
        this.updateColorScheme();
        window.addEventListener('themeChanged', () => {
            this.updateColorScheme();
        });
        // Debounced resize listener
        fromEvent(window, 'resize')
            .pipe(
                debounceTime(200),
                takeUntil(this.destroy$)
            )
            .subscribe(() => {
                this.handleResize();
            });
    }

    ngAfterViewInit() {
        // Configura ResizeObserver para detectar mudanças no container
        this.setupResizeObserver();
    }

    ngOnDestroy() {
        if (this.resizeObserver) {
            this.resizeObserver.disconnect();
        }
        this.destroy$.next();
        this.destroy$.complete();
    }

    private setupResizeObserver() {
        if (typeof ResizeObserver !== 'undefined') {
            this.resizeObserver = new ResizeObserver(entries => {
                for (const entry of entries) {
                    if (entry.target.classList.contains('chart-container')) {
                        this.handleChartResize();
                        break;
                    }
                }
            });

            // Observa os containers dos gráficos
            setTimeout(() => {
                const chartContainers = document.querySelectorAll('.chart-container');
                chartContainers.forEach(container => {
                    this.resizeObserver?.observe(container);
                });
            }, 100);
        }
    }

    private handleResize() {
        // Atualiza as dimensões primeiro
        this.updateResponsiveValues();

        // Força recompilação dos gráficos
        this.handleChartResize();
    }

    private handleChartResize() {
        // Aguarda o DOM se ajustar
        setTimeout(() => {
            // Força recompilação após um pequeno delay
            setTimeout(() => {
                this.forceChartResize();
            }, 100);
        }, 100);
    }

    private forceChartResize() {
        // Força recompilação de cada gráfico
        const charts = [
            this.artistasChart,
            this.musicasChart,
            this.albunsChart,
            this.generosChart
        ];

        charts.forEach(chart => {
            if (chart && chart.update) {
                try {
                    chart.update();
                } catch (error) {
                    console.warn('Erro ao atualizar gráfico:', error);
                }
            }
        });

        // Força detecção de mudanças
        this.cdr.detectChanges();
    }

    private updateResponsiveValues() {
        const width = window.innerWidth;

        // Carrossel
        if (width < 640) {
            this.carouselVisible = 3;
        } else if (width < 1024) {
            this.carouselVisible = 4;
        } else {
            this.carouselVisible = 5;
        }

        // Gráficos de pizza
        if (width < 640) {
            this.chartView = [300, 200];
            this.chartHeight = 200;
        } else if (width < 1024) {
            this.chartView = [350, 220];
            this.chartHeight = 220;
        } else {
            this.chartView = [400, 250];
            this.chartHeight = 250;
        }

        // Gráfico de área
        if (width < 640) {
            this.areaChartView = [300, 200];
            this.areaChartHeight = 200;
        } else if (width < 1024) {
            this.areaChartView = [700, 220];
            this.areaChartHeight = 220;
        } else {
            this.areaChartView = [1200, 250];
            this.areaChartHeight = 250;
        }
    }

    updateColorScheme() {
        const style = getComputedStyle(document.body);
        const main = style.getPropertyValue('--color-primary').trim() || '#8B5CF6';
        const dark = style.getPropertyValue('--color-primary-dark').trim() || '#6D28D9';
        const accent = style.getPropertyValue('--color-accent').trim() || '#f59e0b';
        const secondary = style.getPropertyValue('--color-secondary').trim() || '#06b6d4';
        this.colorScheme = { domain: [main, dark, accent, secondary] };
    }

    onSelectCountry(country: string) {
        this.selectedCountry = country;
    }
} 