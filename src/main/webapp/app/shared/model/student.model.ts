export interface IStudent {
  id?: number;
  name?: string;
  age?: number | null;
  rollNumber?: string;
}

export const defaultValue: Readonly<IStudent> = {};
