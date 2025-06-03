import { Component } from '@angular/core';

@Component({
  selector: 'app-about',
  template: `
        <div class="p-6">
            <h1 class="text-3xl font-bold text-text-heading mb-4">Sobre</h1>
            <p class="text-text-primary">Página sobre o PurpleBox.</p>
        </div>
    `,
  standalone: true
})
export class AboutComponent { }
