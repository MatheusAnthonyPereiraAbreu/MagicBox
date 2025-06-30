import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-sidebar',
    template: `
        <nav class="sidebar">
            <div class="sidebar-header">
                <h1 class="sidebar-title">MagicBox</h1>
            </div>

            <div class="sidebar-section">
                <h3 class="sidebar-section-title">Menu</h3>
                <a class="sidebar-link" routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
                    <i class="ph ph-house-simple text-xl"></i>
                    <span class="sidebar-link-text">Home</span>
                </a>
                <a class="sidebar-link" routerLink="/relatory" routerLinkActive="active">
                    <i class="ph ph-plus-circle text-xl"></i>
                    <span class="sidebar-link-text">Relatory</span>
                </a>
                <a class="sidebar-link" routerLink="/edit" routerLinkActive="active">
                    <i class="ph ph-pencil-simple text-xl"></i>
                    <span class="sidebar-link-text">Edit</span>
                </a>
                <a class="sidebar-link" routerLink="/about" routerLinkActive="active">
                    <i class="ph ph-info text-xl"></i>
                    <span class="sidebar-link-text">About</span>
                </a>
            </div>
        </nav>
    `,
    styles: [`
        .sidebar {
            @apply fixed left-0 top-0 h-screen text-white shadow-lg p-6 rounded-r-3xl backdrop-blur-lg border border-inherit transition-all duration-300 ease-in-out;
            background: linear-gradient(135deg, #18181b 0%, #23232a 60%, #18181b 100%);
            border-left: 8px solid var(--color-primary);
            box-shadow: 0 4px 24px 0 rgba(0,0,0,0.25);
            width: 16rem; /* 256px */
            z-index: 1000;
        }

        .sidebar-header {
            @apply mb-8 border-b border-white/10 pb-4 flex items-center justify-center;
        }

        .sidebar-title {
            @apply text-xl font-bold text-center text-primary transition-all duration-300;
            color: var(--color-primary) !important;
        }

        .sidebar-section {
            @apply mb-6 flex flex-col justify-center items-center w-full gap-3;
        }

        .sidebar-section-title {
            @apply text-sm uppercase text-white/60 mb-2 px-4 transition-all duration-300;
        }

        .sidebar-link {
            @apply flex items-center gap-3 py-2 px-4 rounded-lg text-white/80 hover:bg-white/10 hover:text-white transition-all duration-200 w-full;
        }

        .sidebar-link.active {
            @apply bg-white/20 text-white;
            border-left: 4px solid var(--color-primary);
        }

        .sidebar-link-text {
            @apply transition-all duration-300;
        }

        /* Desktop - sempre expandida */
        @media (min-width: 1025px) {
            .sidebar {
                width: 16rem;
                padding: 1.5rem;
            }
        }

        /* Tablet e Mobile - colapsada por padrão, expande no hover */
        @media (max-width: 1024px) {
            .sidebar {
                width: 5rem;
                padding: 1.5rem 0.75rem;
            }

            .sidebar-title,
            .sidebar-section-title,
            .sidebar-link-text {
                opacity: 0;
                visibility: hidden;
                transform: translateX(-10px);
                transition: all 0.3s ease-in-out;
            }

            .sidebar:hover {
                width: 16rem;
                padding: 1.5rem;
            }

            .sidebar:hover .sidebar-title,
            .sidebar:hover .sidebar-section-title,
            .sidebar:hover .sidebar-link-text {
                opacity: 1;
                visibility: visible;
                transform: translateX(0);
            }
        }

        @media (max-width: 768px) {
            .sidebar {
                width: 4rem;
                padding: 1rem 0.5rem;
            }

            .sidebar:hover {
                width: 14rem;
                padding: 1rem;
            }
        }

        @media (max-width: 640px) {
            .sidebar {
                width: 3.5rem;
                padding: 0.75rem 0.25rem;
            }

            .sidebar:hover {
                width: 12rem;
                padding: 0.75rem;
            }
        }
    `],
    standalone: true,
    imports: [RouterModule]
})
export class SidebarComponent { } 