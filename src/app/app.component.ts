import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SidebarComponent } from './components/sidebar/sidebar.component';

@Component({
  selector: 'app-root',
  template: `
        <app-sidebar></app-sidebar>
        <main class="ml-64 p-6">
            <router-outlet></router-outlet>
        </main>
    `,
  standalone: true,
  imports: [RouterModule, SidebarComponent],
})
export class AppComponent { }
