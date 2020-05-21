import { IEmployee } from 'app/shared/model/employee.model';
import { SYMPTOMS } from 'app/shared/model/enumerations/symptoms.model';

export interface IMeasure {
  id?: number;
  temperature?: number;
  symptom?: SYMPTOMS;
  duration?: number;
  contactWithInfected?: boolean;
  employee?: IEmployee;
}

export class Measure implements IMeasure {
  constructor(
    public id?: number,
    public temperature?: number,
    public symptom?: SYMPTOMS,
    public duration?: number,
    public contactWithInfected?: boolean,
    public employee?: IEmployee
  ) {
    this.contactWithInfected = this.contactWithInfected || false;
  }
}
