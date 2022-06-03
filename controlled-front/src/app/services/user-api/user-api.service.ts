import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserApiService {

  private REST_API_SERVER = "http://controlleduser-env.eba-kcfnqjzn.eu-central-1.elasticbeanstalk.com";
  private ASSAULT = this.REST_API_SERVER + "/assault/exceptions";

  constructor(private httpClient: HttpClient) { }

  public getExceptionAttackIsEnabled() {
    return this.httpClient.get<Boolean>(this.ASSAULT + "/isEnabled").pipe(map(res => {
      return res
    }))
  }

  public getExceptionAttackThreshold() {
    return this.httpClient.get<number>(this.ASSAULT + "/threshold").pipe(map(res => {
      return res
    }))
  }

  public activateDeactivateAttack(isEnabled: boolean) {
    let url = this.ASSAULT + "/isEnabled/" + isEnabled.valueOf();
    return this.httpClient.post(url, null).pipe(map(res => {
      return res;
    }));
  }

  public updateExceptionAttackThreshold(threshold: number) {
    let url = this.ASSAULT + "/threshold/" + threshold.valueOf();
    return this.httpClient.post(url, null).pipe(map(res => {
      return res;
    }));
  }


}
