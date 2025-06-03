import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-sidebar',
    template: `
        <nav class="sidebar">
            <div class="sidebar-header">
                <h1 class="text-xl font-bold text-center">PurpleBox</h1>
            </div>

            <div class="sidebar-section gap-5">
                <h3>Menu</h3>
                <a class="sidebar-link" routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
                    <i class="ph ph-house-simple text-xl"></i>
                    <span>Home</span>
                </a>
                <a class="sidebar-link" routerLink="/about" routerLinkActive="active">
                    <i class="ph ph-info text-xl"></i>
                    <span>About</span>
                </a>
                <a class="sidebar-link" routerLink="/create" routerLinkActive="active">
                    <i class="ph ph-plus-circle text-xl"></i>
                    <span>Create</span>
                </a>
                <a class="sidebar-link" routerLink="/edit" routerLinkActive="active">
                    <i class="ph ph-pencil-simple text-xl"></i>
                    <span>Edit</span>
                </a>
            </div>
        </nav>
    `,
    standalone: true,
    imports: [RouterModule]
})
export class SidebarComponent { } 