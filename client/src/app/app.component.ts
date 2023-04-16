import { Component } from '@angular/core';
import {NotificationService} from "./service/notification.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private notificationService: NotificationService) {
    if (sessionStorage.getItem('reloadAfterPageLoad') === 'true'){
      if (sessionStorage.getItem('notification-message')){
        let message = sessionStorage.getItem('notification-message');
        // @ts-ignore
        this.notificationService.showSnackBar(message);
        sessionStorage.removeItem('notification-message');
      }
      sessionStorage.setItem('reloadAfterPageLoad', 'false');
    }
  }
}
