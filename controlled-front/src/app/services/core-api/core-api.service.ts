import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class CoreApiService {
  private REST_API_SERVER = "http://controlledcore-env.eba-mcwwj7up.eu-central-1.elasticbeanstalk.com";
  private ASSAULT = this.REST_API_SERVER + "/assault/http/";

  constructor(private httpClient: HttpClient) {}

  public getHttpAttackIsEnabled() {
    return this.httpClient.get<Boolean>(this.ASSAULT + "isEnabled").pipe(map(res => {
      return res
    }))
  }

  public getHttpAttackProperties() {
    return this.httpClient.get<String>(this.ASSAULT).pipe(map(res => {
      return res
    }))
  }

  public activateDeactivateAttack(isEnabled: boolean) {
    let url = this.ASSAULT + "isEnabled/" + isEnabled.valueOf();
    return this.httpClient.post(url, null).pipe(map(res => {
      return res;
    }));
  }

  public getCircuitBreakerState() {
    let url = this.REST_API_SERVER + "/user/circuit";
    return this.httpClient.get<String>(url).pipe(map(res => {
      return res;
    }));
  }

  public getUser() {
    let url = this.REST_API_SERVER + "/user/1";
    return this.httpClient.get<String>(url).pipe(map(res => {
      return res;
    }))
  }

  public getUserWithRetry() {
    let url = this.REST_API_SERVER + "/user/retry/1";
    return this.httpClient.get<String>(url).pipe(map(res => {
      return res;
    }))
  }

  public getUserWithCircuit() {
    let url = this.REST_API_SERVER + "/user/circuit/1";
    return this.httpClient.get<String>(url).pipe(map(res => {
      return res;
    }))
  }
}
