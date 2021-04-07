import { IProvince } from 'app/entities/province/province.model';

export interface IRegion {
  id?: number;
  libelle?: string;
  provinces?: IProvince[] | null;
}

export class Region implements IRegion {
  constructor(public id?: number, public libelle?: string, public provinces?: IProvince[] | null) {}
}

export function getRegionIdentifier(region: IRegion): number | undefined {
  return region.id;
}
