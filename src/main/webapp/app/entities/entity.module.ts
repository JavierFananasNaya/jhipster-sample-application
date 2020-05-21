import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'employee',
        loadChildren: () => import('./employee/employee.module').then(m => m.Covid19TesterEmployeeModule),
      },
      {
        path: 'measure',
        loadChildren: () => import('./measure/measure.module').then(m => m.Covid19TesterMeasureModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class Covid19TesterEntityModule {}
