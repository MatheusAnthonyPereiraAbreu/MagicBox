import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';

export const routes: Routes = [
  { 
    path: '', 
    component: HomeComponent 
  },
  { 
    path: 'about', 
    loadComponent: () => 
      import('./pages/about/about.component').then(m => m.AboutComponent)
  },
  {
    path: 'create',
    loadComponent: () =>
      import('./pages/create/create.component').then(m => m.CreateComponent)
  },
  {
    path: 'edit',
    loadComponent: () =>
      import('./pages/edit/edit.component').then(m => m.EditComponent)
  },
  {
    path: '**', 
    redirectTo: ''  // Redireciona para home se rota não existir
  }
];