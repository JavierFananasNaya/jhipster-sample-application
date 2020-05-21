import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMeasure, Measure } from 'app/shared/model/measure.model';
import { MeasureService } from './measure.service';
import { MeasureComponent } from './measure.component';
import { MeasureDetailComponent } from './measure-detail.component';
import { MeasureUpdateComponent } from './measure-update.component';

@Injectable({ providedIn: 'root' })
export class MeasureResolve implements Resolve<IMeasure> {
  constructor(private service: MeasureService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMeasure> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((measure: HttpResponse<Measure>) => {
          if (measure.body) {
            return of(measure.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Measure());
  }
}

export const measureRoute: Routes = [
  {
    path: '',
    component: MeasureComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Measures',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MeasureDetailComponent,
    resolve: {
      measure: MeasureResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Measures',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MeasureUpdateComponent,
    resolve: {
      measure: MeasureResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Measures',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MeasureUpdateComponent,
    resolve: {
      measure: MeasureResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Measures',
    },
    canActivate: [UserRouteAccessService],
  },
];
