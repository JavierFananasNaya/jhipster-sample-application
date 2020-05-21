import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMeasure, Measure } from 'app/shared/model/measure.model';
import { MeasureService } from './measure.service';
import { IEmployee } from 'app/shared/model/employee.model';
import { EmployeeService } from 'app/entities/employee/employee.service';

@Component({
  selector: 'jhi-measure-update',
  templateUrl: './measure-update.component.html',
})
export class MeasureUpdateComponent implements OnInit {
  isSaving = false;
  employees: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    temperature: [],
    symptom: [],
    duration: [],
    contactWithInfected: [],
    employee: [],
  });

  constructor(
    protected measureService: MeasureService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ measure }) => {
      this.updateForm(measure);

      this.employeeService.query().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    });
  }

  updateForm(measure: IMeasure): void {
    this.editForm.patchValue({
      id: measure.id,
      temperature: measure.temperature,
      symptom: measure.symptom,
      duration: measure.duration,
      contactWithInfected: measure.contactWithInfected,
      employee: measure.employee,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const measure = this.createFromForm();
    if (measure.id !== undefined) {
      this.subscribeToSaveResponse(this.measureService.update(measure));
    } else {
      this.subscribeToSaveResponse(this.measureService.create(measure));
    }
  }

  private createFromForm(): IMeasure {
    return {
      ...new Measure(),
      id: this.editForm.get(['id'])!.value,
      temperature: this.editForm.get(['temperature'])!.value,
      symptom: this.editForm.get(['symptom'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      contactWithInfected: this.editForm.get(['contactWithInfected'])!.value,
      employee: this.editForm.get(['employee'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeasure>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }
}
