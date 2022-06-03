import { Component } from '@angular/core';
import {CoreApiService} from "./services/core-api/core-api.service";
import {UserApiService} from "./services/user-api/user-api.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'controlled-front';

  // core
  isHttpEnabled: boolean | undefined;
  httpProperties: string | undefined;
  circuitBreakerState: string | undefined;
  getUserResult: string | undefined;

  // user
  isExceptionEnabled: boolean | undefined;
  exceptionThreshold: number | undefined;

  constructor(private coreApi: CoreApiService, private userApi: UserApiService) {
    this.coreApi.getHttpAttackIsEnabled().subscribe((data: Boolean) => {
      this.isHttpEnabled = data.valueOf();
    });
    this.coreApi.getHttpAttackProperties().subscribe((data: String) => {
      this.httpProperties = data.valueOf();
    });
    this.coreApi.getCircuitBreakerState().subscribe((data: String) => {
      this.circuitBreakerState = data.valueOf();
    });
    this.userApi.getExceptionAttackIsEnabled().subscribe((data: Boolean) => {
      this.isExceptionEnabled = data.valueOf();
    });
    this.userApi.getExceptionAttackThreshold().subscribe((data: number) => {
      this.exceptionThreshold = data.valueOf();
    });
  }

  refreshDataCore() {
    this.coreApi.getHttpAttackIsEnabled().subscribe((data: Boolean) => {
      this.isHttpEnabled = data.valueOf();
    });
    this.coreApi.getHttpAttackProperties().subscribe((data: String) => {
      this.httpProperties = data.valueOf();
    })
  }

  refreshDataUser() {
    this.userApi.getExceptionAttackIsEnabled().subscribe((data: Boolean) => {
      this.isExceptionEnabled = data.valueOf();
    });

    this.userApi.getExceptionAttackThreshold().subscribe((data: number) => {
      this.exceptionThreshold = data.valueOf();
    });
  }

  activateAttackCore() {
    this.coreApi.activateDeactivateAttack(true).subscribe((res: any) => {
      if (res !== undefined && res !== null) {}});
    this.refreshDataCore();
  }

  activateAttackUser() {
    this.userApi.activateDeactivateAttack(true).subscribe((res: any) => {
      if (res !== undefined && res !== null) {}});
    this.refreshDataUser();
  }

  deactivateAttackCore() {
    this.coreApi.activateDeactivateAttack(false).subscribe((res: any) => {
      if (res !== undefined && res !== null) {}});
    this.refreshDataCore();
  }

  deactivateAttackUser() {
    this.userApi.activateDeactivateAttack(false).subscribe((res: any) => {
      if (res !== undefined && res !== null) {}});
    this.refreshDataUser();
  }

  getUserCore() {
    this.getUserResult = "";
    this.coreApi.getUser().subscribe((res: any) => {
      if (res !== undefined && res !== null) {
        this.getUserResult = res;
      }
    }, error => {
      this.getUserResult = error.error;
    });
  }

  getUserWithRetryCore() {
    this.getUserResult = "";
    this.coreApi.getUserWithRetry().subscribe((res: any) => {
      if (res !== undefined && res !== null) {
        console.log(res.body)
        this.getUserResult = res.body;
      }
    }, error => {
      this.getUserResult = error.error;
    });
  }

  getUserWithCircuitCore() {
    this.getUserResult = "";
    this.coreApi.getUserWithCircuit().subscribe((res: any) => {
      if (res !== undefined && res !== null) {
        this.getUserResult = res.body;
      }
    }, error => {
      this.getUserResult = error.error;
    });
    this.getCircuitBreakerStateCore();
  }

  getCircuitBreakerStateCore() {
    this.coreApi.getCircuitBreakerState().subscribe((res: any) => {
      this.circuitBreakerState = res;
    })
  }
}
