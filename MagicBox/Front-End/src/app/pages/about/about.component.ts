import { Component, OnInit } from '@angular/core';
import { AboutService, GithubUser } from './about.service';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { LogoComponent } from '../../components/logo/logo.component';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.scss'],
  standalone: true,
  imports: [CommonModule, LogoComponent],
  providers: [AboutService]
})
export class AboutComponent implements OnInit {
  users: { user: GithubUser | null, role: string }[] = [
    { user: null, role: 'DataBase' },
    { user: null, role: 'Documentador' },
    { user: null, role: 'Front-End' },
    { user: null, role: 'Engenheiro de Software' },
    { user: null, role: 'Back-End' }
  ];
  usernames = ['tomlavez', 'Lucasskyher', 'MatheusAnthonyPereiraAbreu', 'AugustoZanoli', 'typedefAlejandro'];

  constructor(private aboutService: AboutService) { }

  ngOnInit() {
    this.usernames.forEach((username, idx) => {
      this.aboutService.getUser(username).subscribe(user => {
        this.users[idx].user = user;
      });
    });
  }

  openProfile(url?: string) {
    if (url) {
      window.open(url, '_blank');
    }
  }
}
