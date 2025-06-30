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
    const saved = localStorage.getItem('pb-theme');
    if (saved && this.themes.some(t => t.key === saved)) {
      this.selectedTheme = saved;
      this.applyTheme(saved);
    } else {
      this.applyTheme(this.selectedTheme);
    }
  }

  setTheme(theme: string) {
    this.selectedTheme = theme;
    localStorage.setItem('pb-theme', theme);
    this.applyTheme(theme);
  }

  applyTheme(theme: string) {
    const body = document.body;
    this.themes.forEach(t => body.classList.remove('theme-' + t.key));
    body.classList.add('theme-' + theme);
  }
}
