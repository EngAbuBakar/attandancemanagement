import dayjs from 'dayjs';

export interface ILibrary {
  id?: number;
  name?: string | null;
  code?: number | null;
  block?: string | null;
  isVisible?: boolean | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<ILibrary> = {
  isVisible: false,
  isDeleted: false,
};
