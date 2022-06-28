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
  httpAttackProperties: string | undefined;
  circuitBreakerState: string | undefined;
  getUserResult: string | undefined;

  // user
  isExceptionEnabled: boolean | undefined;
  exceptionThreshold: number | undefined;
  changedThreshold: number | undefined;
  isKillableEnabled: boolean | undefined;

  constructor(private coreApi: CoreApiService, private userApi: UserApiService) {
    this.coreApi.getHttpAttackIsEnabled().subscribe((data: Boolean) => {
      this.isHttpEnabled = data.valueOf();
    });
    this.coreApi.getHttpAttackProperties().subscribe((data: string) => {
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
    this.isKillableEnabled = false;

    this.httpAttackProperties = "{ \n" +
      "\"insertRandomAttribute\": false, \"insertSpecificAttribute\": false, \"deleteSpecificAttribute\": false, \"deleteRandomAttribute\": false, \"insertAttributeKey\": \"key\", \"insertAttributeValue\": \"value\", \"deleteAttributeKey\": \"username\"\n" +
      "}"
    this.changedThreshold = this.exceptionThreshold;
  }

  sendHttpAttackProperties() {
    if (this.httpAttackProperties?.length != 0 && this.httpAttackProperties != undefined) {
      console.log("Sending new http attack properties ...")
      this.coreApi.submitNewHttpAttackProperties(this.httpAttackProperties);
    }
    this.refreshDataCore();
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

  activateKillable() {
    this.isKillableEnabled = true;
  }

  deactivateKillable() {
    this.isKillableEnabled = false;
  }

  updateThreshold() {
    if (this.changedThreshold != undefined && this.changedThreshold > -1) {
      this.userApi.updateExceptionAttackThreshold(this.changedThreshold);
    }
    this.refreshDataUser();
  }

  getUserCore() {
    if (this.isKillableEnabled) {
      this.getUserWithKill();
    } else {
      this.getUserNormal();
    }
  }

  getUserNormal() {
    this.getUserResult = "";
    this.coreApi.getUser().subscribe((res: any) => {
      if (res !== undefined && res !== null) {
        this.getUserResult = res;
      }
    }, error => {
      this.getUserResult = error.error;
    });
  }

  getUserWithKill() {
    this.getUserResult = "";
    this.userApi.getUserWithKill().subscribe((res: any) => {
      if (res !== undefined && res !== null) {
        this.getUserResult = res;
      }
    }, error => {
      this.getUserResult = error.error;
    });
  }

  getUserAsMappedToUser() {
    this.getUserCore();
  }

  getUserAsMappedToObject() {
    this.getUserResult = "";
    this.coreApi.getUserAsObject().subscribe((res: any) => {
      if (res !== undefined && res !== null) {
        this.getUserResult = res;
      }
    }, error => {
      this.getUserResult = error.error;
    });
  }

  getUserAsMappedToUserWithGson() {
    this.getUserResult = "";
    this.coreApi.getUserWithGson().subscribe((res: any) => {
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
