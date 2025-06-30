import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { SplashScreenComponent } from './components/splash-screen/splash-screen.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  template: `
    <app-splash-screen *ngIf="showSplash" (splashComplete)="onSplashComplete()"></app-splash-screen>
    
    <div *ngIf="!showSplash" class="app-content">
      <app-sidebar></app-sidebar>
      <main class="main-content p-6 min-h-screen bg-[#18181b]">
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styles: [`
    .app-content {
      animation: fadeIn 0.5s ease-out;
    }
    
    .main-content {
      margin-left: 16rem; /* 256px - largura padrão da sidebar */
      transition: margin-left 0.3s ease-in-out;
    }
    
    /* Responsividade para o main content */
    @media (max-width: 1024px) {
      .main-content {
        margin-left: 5rem; /* 80px - largura colapsada da sidebar */
      }
    }
    
    @media (max-width: 768px) {
      .main-content {
        margin-left: 4rem; /* 64px */
      }
    }
    
    @media (max-width: 640px) {
      .main-content {
        margin-left: 3.5rem; /* 56px */
      }
    }
    
    @keyframes fadeIn {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }
  `],
  standalone: true,
  imports: [RouterModule, SidebarComponent, SplashScreenComponent, CommonModule],
})
export class AppComponent implements OnInit {
  showSplash = false; // Começa como false para aplicar o tema primeiro

  ngOnInit() {
    // Aplicar tema padrão se não houver nenhum
    if (!document.body.className.includes('theme-')) {
      document.body.classList.add('theme-purple');
    }

    // Pequeno delay para garantir que tudo esteja carregado
    setTimeout(() => {
      this.showSplash = true;
    }, 100);
  }

  onSplashComplete() {
    this.showSplash = false;
  }
}
