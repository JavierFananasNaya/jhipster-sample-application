import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Covid19TesterSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [Covid19TesterSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
})
export class Covid19TesterHomeModule {}
