import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LogoComponent } from '../logo/logo.component';

@Component({
  selector: 'app-splash-screen',
  template: `
    <div class="splash-screen" [class.fade-out]="isFadingOut">
      <div class="splash-content">
        <div class="logo-container">
          <app-logo></app-logo>
        </div>
        <h1 class="app-title">MagicBox</h1>
        <p class="app-subtitle">Sua central de estatísticas musicais</p>
      </div>
    </div>
  `,
  styles: [`
    .splash-screen {
      position: fixed;
      top: 0;
      left: 0;
      width: 100vw;
      height: 100vh;
      background: linear-gradient(135deg, #18181b 0%, #23232a 60%, #18181b 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 9999;
      transition: opacity 0.8s ease-out, visibility 0.8s ease-out;
    }

    .splash-screen.fade-out {
      opacity: 0;
      visibility: hidden;
    }

    .splash-content {
      text-align: center;
      animation: fadeInUp 1s ease-out;
    }

    .logo-container {
      margin-bottom: 2rem;
      animation: logoFloat 3s ease-in-out infinite;
    }

    .logo-container app-logo {
      width: 120px !important;
      height: 120px !important;
      display: block;
      margin: 0 auto;
    }

    .app-title {
      font-size: 3.5rem;
      font-weight: 900;
      margin-bottom: 1rem;
      background: linear-gradient(135deg, var(--color-primary), var(--color-primary-dark));
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      animation: titleGlow 2s ease-in-out infinite alternate;
    }

    .app-subtitle {
      font-size: 1.2rem;
      color: #d1d5db;
      font-weight: 300;
      opacity: 0.8;
      animation: subtitleFade 2s ease-in-out infinite alternate;
    }

    @keyframes fadeInUp {
      from {
        opacity: 0;
        transform: translateY(30px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    @keyframes logoFloat {
      0%, 100% {
        transform: translateY(0px);
      }
      50% {
        transform: translateY(-10px);
      }
    }

    @keyframes titleGlow {
      from {
        filter: drop-shadow(0 0 10px rgba(var(--color-primary-rgb), 0.3));
      }
      to {
        filter: drop-shadow(0 0 20px rgba(var(--color-primary-rgb), 0.6));
      }
    }

    @keyframes subtitleFade {
      from {
        opacity: 0.6;
      }
      to {
        opacity: 1;
      }
    }

    /* Responsividade */
    @media (max-width: 768px) {
      .logo-container app-logo {
        width: 80px !important;
        height: 80px !important;
      }
      
      .app-title {
        font-size: 2.5rem;
      }
      
      .app-subtitle {
        font-size: 1rem;
      }
    }
  `],
  standalone: true,
  imports: [CommonModule, LogoComponent]
})
export class SplashScreenComponent implements OnInit {
  @Output() splashComplete = new EventEmitter<void>();
  isFadingOut = false;

  ngOnInit() {
    // Aguardar 5 segundos e depois fazer fade out
    setTimeout(() => {
      this.isFadingOut = true;

      // Aguardar a animação de fade out terminar e emitir evento
      setTimeout(() => {
        this.splashComplete.emit();
      }, 800); // Tempo da transição CSS
    }, 3000);
  }
} 