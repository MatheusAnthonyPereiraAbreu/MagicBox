import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

const THEME_COLORS = [
  { key: 'purple', label: 'Roxo', color: 'bg-card-purple-text' },
  { key: 'blue', label: 'Azul', color: 'bg-card-blue-text' },
  { key: 'emerald', label: 'Verde', color: 'bg-card-emerald-text' },
  { key: 'amber', label: 'Âmbar', color: 'bg-card-amber-text' },
  { key: 'rose', label: 'Rosa', color: 'bg-card-rose-text' },
  { key: 'indigo', label: 'Índigo', color: 'bg-card-indigo-text' },
];

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  standalone: true,
  imports: [CommonModule]
})
export class EditComponent implements OnInit {
  themes = THEME_COLORS;
  selectedTheme = 'purple';

  ngOnInit() {
    // Aplicar tema padrão ao inicializar
    this.applyTheme(this.selectedTheme);
  }

  setTheme(theme: string) {
    this.selectedTheme = theme;
    this.applyTheme(theme);
  }

  applyTheme(theme: string) {
    const body = document.body;
    this.themes.forEach(t => body.classList.remove('theme-' + t.key));
    body.classList.add('theme-' + theme);

    // Disparar evento customizado para notificar outros componentes
    const themeChangeEvent = new CustomEvent('themeChanged', {
      detail: { theme: theme }
    });
    window.dispatchEvent(themeChangeEvent);
  }
}
