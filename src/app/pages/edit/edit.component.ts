import { Component } from '@angular/core';

@Component({
  selector: 'app-edit',
  template: `
        <div class="p-6">
            <h1 class="text-3xl font-bold text-text-heading mb-4">Editar</h1>
            <p class="text-text-primary">Página de edição de item.</p>
        </div>
    `,
  standalone: true
})
export class EditComponent { }
