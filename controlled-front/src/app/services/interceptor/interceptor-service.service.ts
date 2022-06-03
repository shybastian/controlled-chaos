import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InterceptorService {

  constructor() {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // if (!request.headers.has('Content-Type')) {
    //   request = request.clone({headers: request.headers.set('Content-Type', 'application/json')});
    // }
    if (!request.headers.has('Access-Control-Allow-Origin')) {
      request = request.clone({headers: request.headers.set('Access-Control-Allow-Origin', '*')})
    }
    return next.handle(request)
  }
}
