import { IMeasure } from 'app/shared/model/measure.model';

export interface IEmployee {
  id?: number;
  name?: string;
  surname?: string;
  department?: string;
  age?: number;
  measures?: IMeasure[];
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public name?: string,
    public surname?: string,
    public department?: string,
    public age?: number,
    public measures?: IMeasure[]
  ) {}
}
