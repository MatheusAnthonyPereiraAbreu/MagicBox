import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-logo',
  template: `
    <svg version="1.0" xmlns="http://www.w3.org/2000/svg"
         width="2024.000000pt" height="3718.000000pt" viewBox="0 0 2024.000000 3718.000000"
         preserveAspectRatio="xMidYMid meet"
         [ngClass]="logoClass">
      <g transform="translate(0.000000,3718.000000) scale(0.100000,-0.100000)" stroke="none">
        <path [attr.fill]="currentColor" d="M19738 37169 c-10 -5 -18 -16 -18 -24 0 -87 -710 -563 -1977 -1326
        -1468 -884 -3988 -2337 -6295 -3630 -328 -183 -596 -337 -595 -340 6 -18 2037
        -2169 3007 -3184 1436 -1504 2315 -2408 3320 -3419 1306 -1312 1992 -1959
        2355 -2221 130 -94 203 -118 245 -80 20 18 20 34 20 7116 0 7008 0 7098 -19
        7109 -23 12 -22 12 -43 -1z"/>
        <path [attr.fill]="currentColor" d="M5195 26991 c-3248 -3249 -4934 -4942 -4925 -4945 45 -16 3727 -931
        5175 -1286 3131 -767 4595 -1102 4680 -1070 117 45 167 425 207 1560 16 462
        16 2894 0 3810 -33 1821 -79 3647 -137 5400 -8 245 -22 668 -31 940 -8 272
        -19 503 -22 513 -6 13 -1073 -1049 -4947 -4922z"/>
        <path [attr.fill]="currentColor" d="M0 15883 l0 -5928 4953 -4953 c2723 -2723 4955 -4952 4960 -4952 4 0
        7 2679 7 5953 l0 5953 -4566 4539 c-2512 2497 -4744 4715 -4960 4928 l-394
        389 0 -5929z"/>
        <path [attr.fill]="currentColor" d="M15271 16848 l-4951 -4922 0 -5953 c0 -3274 3 -5953 8 -5953 4 0
        2236 2229 4960 4953 l4952 4952 0 5923 c0 3284 -4 5922 -9 5922 -4 0 -2236
        -2215 -4960 -4922z"/>
      </g>
    </svg>
  `,
  styles: [`
    :host {
      display: inline-block;
      transition: all 0.3s ease;
    }
    
    svg {
      transition: all 0.3s ease;
    }
    
    path {
      transition: fill 0.3s ease;
    }
  `],
  standalone: true,
  imports: [CommonModule]
})
export class LogoComponent implements OnInit, OnDestroy {
  currentColor = '#8B5CF6'; // Cor padrão (roxo)
  logoClass = 'w-20 h-20';
  private themeColors = {
    purple: '#8B5CF6',
    blue: '#3B82F6',
    emerald: '#10B981',
    amber: '#F59E0B',
    rose: '#F43F5E',
    indigo: '#6366F1'
  };

  ngOnInit() {
    this.updateColor();
    // Observar mudanças no body class
    this.observeBodyClass();
    // Escutar evento customizado de mudança de tema
    window.addEventListener('themeChanged', this.handleThemeChange.bind(this) as EventListener);
  }

  ngOnDestroy() {
    window.removeEventListener('themeChanged', this.handleThemeChange.bind(this) as EventListener);
  }

  private handleThemeChange(event: Event) {
    this.updateColor();
  }

  private observeBodyClass() {
    const observer = new MutationObserver(() => {
      this.updateColor();
    });

    observer.observe(document.body, {
      attributes: true,
      attributeFilter: ['class']
    });
  }

  private updateColor() {
    // Verificar qual tema está ativo no body
    const bodyClasses = document.body.className;
    let currentTheme = 'purple'; // tema padrão

    if (bodyClasses.includes('theme-blue')) currentTheme = 'blue';
    else if (bodyClasses.includes('theme-emerald')) currentTheme = 'emerald';
    else if (bodyClasses.includes('theme-amber')) currentTheme = 'amber';
    else if (bodyClasses.includes('theme-rose')) currentTheme = 'rose';
    else if (bodyClasses.includes('theme-indigo')) currentTheme = 'indigo';
    else if (bodyClasses.includes('theme-purple')) currentTheme = 'purple';

    this.currentColor = this.themeColors[currentTheme as keyof typeof this.themeColors] || this.themeColors.purple;
  }

  // Método público para definir o tamanho do logo
  setSize(size: string) {
    this.logoClass = size;
  }
} 