export interface IStudent {
  id?: number;
  name?: string;
  age?: number;
  rollNo?: string;
  address?: string | null;
}

export const defaultValue: Readonly<IStudent> = {};
