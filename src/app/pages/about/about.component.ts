import { Component, OnInit } from '@angular/core';
import { AboutService, GithubUser } from './about.service';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.scss'],
  standalone: true,
  imports: [CommonModule],
  providers: [AboutService]
})
export class AboutComponent implements OnInit {
  users: { user: GithubUser | null, role: string }[] = [
    { user: null, role: 'DataBase' },
    { user: null, role: 'Documentador' },
    { user: null, role: 'Front-End' },
    { user: null, role: 'Engenheiro de Software' }
  ];
  usernames = ['tomlavez', 'Lucasskyher', 'MatheusAnthonyPereiraAbreu', 'AugustoZanoli'];

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
