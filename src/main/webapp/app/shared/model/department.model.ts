import dayjs from 'dayjs';

export interface IDepartment {
  id?: number;
  name?: string;
  description?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
}

export const defaultValue: Readonly<IDepartment> = {};
