import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'about',
    loadComponent: () => import('./pages/about/about.component').then(m => m.AboutComponent)
  },
  {
    path: 'relatory',
    loadComponent: () => import('./pages/relatory/relatory.component').then(m => m.RelatoryComponent)
  },
  {
    path: 'edit',
    loadComponent: () => import('./pages/edit/edit.component').then(m => m.EditComponent)
  }
];