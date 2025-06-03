import { Component } from '@angular/core';

@Component({
  selector: 'app-create',
  template: `
        <div class="p-6">
            <h1 class="text-3xl font-bold text-text-heading mb-4">Criar Novo</h1>
            <p class="text-text-primary">Página de criação de novo item.</p>
        </div>
    `,
  standalone: true
})
export class CreateComponent { }
