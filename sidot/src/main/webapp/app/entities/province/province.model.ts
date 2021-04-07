import { ICommune } from 'app/entities/commune/commune.model';
import { IRegion } from 'app/entities/region/region.model';

export interface IProvince {
  id?: number;
  libelle?: string;
  communes?: ICommune[] | null;
  region?: IRegion | null;
}

export class Province implements IProvince {
  constructor(public id?: number, public libelle?: string, public communes?: ICommune[] | null, public region?: IRegion | null) {}
}

export function getProvinceIdentifier(province: IProvince): number | undefined {
  return province.id;
}
