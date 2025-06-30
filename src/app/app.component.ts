import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SidebarComponent } from './components/sidebar/sidebar.component';

@Component({
  selector: 'app-root',
  template: `
        <app-sidebar></app-sidebar>
        <main class="ml-60 p-6 min-h-screen bg-[#18181b]">
            <router-outlet></router-outlet>
        </main>
    `,
  standalone: true,
  imports: [RouterModule, SidebarComponent],
})
export class AppComponent { }
