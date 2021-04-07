import { ILocalite } from 'app/entities/localite/localite.model';
import { IProvince } from 'app/entities/province/province.model';
import { ITypeCommune } from 'app/entities/type-commune/type-commune.model';

export interface ICommune {
  id?: number;
  libelle?: string;
  localites?: ILocalite[] | null;
  province?: IProvince | null;
  typeCommune?: ITypeCommune | null;
}

export class Commune implements ICommune {
  constructor(
    public id?: number,
    public libelle?: string,
    public localites?: ILocalite[] | null,
    public province?: IProvince | null,
    public typeCommune?: ITypeCommune | null
  ) {}
}

export function getCommuneIdentifier(commune: ICommune): number | undefined {
  return commune.id;
}
